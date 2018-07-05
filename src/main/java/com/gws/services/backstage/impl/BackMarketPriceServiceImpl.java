package com.gws.services.backstage.impl;

import com.alibaba.fastjson.JSON;
import com.gws.common.constants.backstage.ErrorMsg;
import com.gws.common.constants.backstage.RedisConfig;
import com.gws.common.constants.backstage.Symbol;
import com.gws.dto.backstage.PageDTO;
import com.gws.entity.backstage.MarketPrice;
import com.gws.entity.backstage.MarketPriceBO;
import com.gws.entity.backstage.MarketPriceHistory;
import com.gws.entity.backstage.price.Price;
import com.gws.entity.backstage.price.PriceResult;
import com.gws.repositories.master.backstage.BackMarketPriceHistoryMaster;
import com.gws.repositories.master.backstage.BackMarketPriceMaster;
import com.gws.repositories.slave.backstage.BackMarketPriceHistorySlave;
import com.gws.repositories.slave.backstage.BackMarketPriceSlave;
import com.gws.services.backstage.BackMarketPriceService;
import com.gws.utils.cache.IdGlobalGenerator;
import com.gws.utils.http.HttpRequest;
import com.gws.utils.http.LangReadUtil;
import com.gws.utils.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/21.
 */
@Service
public class BackMarketPriceServiceImpl implements BackMarketPriceService {

    private final BackMarketPriceSlave backMarketPriceSlave;

    private final BackMarketPriceMaster backMarketPriceMaster;

    private final BackMarketPriceHistorySlave backMarketPriceHistorySlave;

    private final BackMarketPriceHistoryMaster backMarketPriceHistoryMaster;

    private final IdGlobalGenerator idGlobalGenerator;

    private final RedisUtil redisUtil;

    /**
     * 设置最新价格的接口
     */
    @Value("${kline.profile.setting}")
    private String settingprice;

    /**
     * 获取最新价格的接口
     */
    @Value("${kline.data.price}")
    private String gettingprice;

    @Autowired
    public BackMarketPriceServiceImpl(BackMarketPriceSlave backMarketPriceSlave, BackMarketPriceMaster backMarketPriceMaster, BackMarketPriceHistorySlave backMarketPriceHistorySlave, BackMarketPriceHistoryMaster backMarketPriceHistoryMaster, IdGlobalGenerator idGlobalGenerator, RedisUtil redisUtil) {
        this.backMarketPriceSlave = backMarketPriceSlave;
        this.backMarketPriceMaster = backMarketPriceMaster;
        this.backMarketPriceHistorySlave = backMarketPriceHistorySlave;
        this.backMarketPriceHistoryMaster = backMarketPriceHistoryMaster;
        this.idGlobalGenerator = idGlobalGenerator;
        this.redisUtil = redisUtil;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePrice(MarketPriceBO marketPriceBO) {
        //向柯凡的接口处发送最新的市价设置参数
        Price price = Price.getInstance(marketPriceBO.getBuyCnySgd(),
                                        marketPriceBO.getSellCnySgd(),
                                        marketPriceBO.getBuySgdUsd(),
                                        marketPriceBO.getSellSgdUsd(),
                                        marketPriceBO.getSellSpread(),
                                        marketPriceBO.getBuySpread());
        String jsonResult = HttpRequest.sendPost(settingprice, JSON.toJSONString(price));
        PriceResult priceResult = JSON.parseObject(jsonResult, PriceResult.class);
        if(priceResult.getCode()!=200){
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.SET_PRICE_NETWORK_ERROR));
        }

        MarketPrice one = backMarketPriceSlave.findOne(1L);
        if(one==null){
            //第一次平台定价将数据插入
            MarketPrice marketPrice = new MarketPrice();
            marketPrice.setId(1L);
            marketPrice.setBuyCnySgd(marketPriceBO.getBuyCnySgd());
            marketPrice.setSellCnySgd(marketPriceBO.getSellCnySgd());
            marketPrice.setBuySgdUsd(marketPriceBO.getBuySgdUsd());
            marketPrice.setSellSgdUsd(marketPriceBO.getSellSgdUsd());
            marketPrice.setSellSpread(marketPriceBO.getSellSpread());
            marketPrice.setBuySpread(marketPriceBO.getBuySpread());
            backMarketPriceMaster.save(marketPrice);
        }else{
            //后期都是更新平台的定价
            Integer currentTime = (int)(System.currentTimeMillis()/1000);
            MarketPrice marketPrice = new MarketPrice();
            marketPrice.setBuyCnySgd(marketPriceBO.getBuyCnySgd());
            marketPrice.setSellCnySgd(marketPriceBO.getSellCnySgd());
            marketPrice.setBuySgdUsd(marketPriceBO.getBuySgdUsd());
            marketPrice.setSellSgdUsd(marketPriceBO.getSellSgdUsd());
            marketPrice.setSellSpread(marketPriceBO.getSellSpread());
            marketPrice.setBuySpread(marketPriceBO.getBuySpread());
            marketPrice.setUtime(currentTime);
            backMarketPriceMaster.updateById(marketPrice,1L,"buyCnySgd","sellCnySgd","buySgdUsd","sellSgdUsd","sellSpread","buySpread","utime");
        }

        //不管怎么样每次都要将数据放入历史市价表中（注意是BTY/USDG的买入价和卖出价）
        MarketPriceHistory marketPriceHistory = new MarketPriceHistory();
        marketPriceHistory.setId(idGlobalGenerator.getSeqId(MarketPriceHistory.class));
        //获取当前买入的bty/usdg价格-->TODO
        marketPriceHistory.setBuyBtyUsdg(marketPriceBO.getBuyBtyUsdg());
        //获取当前卖出的bty/usdg价格-->TODO
        marketPriceHistory.setSellBtyUsdg(marketPriceBO.getSellBtyUsdg());
        marketPriceHistory.setBuySpread(marketPriceBO.getBuySpread());
        marketPriceHistory.setSellSpread(marketPriceBO.getSellSpread());
        backMarketPriceHistoryMaster.save(marketPriceHistory);

        //更新市价后需要将redis的缓存清了
        redisUtil.delete(RedisConfig.MARKET_PRICE);
    }

    @Override
    public PageDTO queryPriceHistory(MarketPriceBO marketPriceBO) {
        Integer page = marketPriceBO.getPage();
        Integer rowNum = marketPriceBO.getRowNum();

        Sort sort = new Sort(Sort.Direction.DESC,"ctime");
        Pageable pageable = new PageRequest(page - 1, rowNum,sort);

        Page<MarketPriceHistory> marketPriceHistoryPage = backMarketPriceHistorySlave.findAll(pageable);

        List<MarketPriceHistory> list = marketPriceHistoryPage == null ? Collections.EMPTY_LIST : marketPriceHistoryPage.getContent();
        long totalPage = marketPriceHistoryPage == null ? 0 : marketPriceHistoryPage.getTotalElements();
        return PageDTO.getPagination(totalPage,list);
    }

    @Override
    public MarketPrice queryPrice(MarketPriceBO marketPriceBO) {
        MarketPrice one = backMarketPriceSlave.findOne(1L);
        if(one==null){
            one = new MarketPrice();
        }
        return one;
    }
}
