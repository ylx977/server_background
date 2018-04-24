package com.gws.services.backstage.impl;

import com.gws.entity.backstage.MarketPrice;
import com.gws.entity.backstage.MarketPriceBO;
import com.gws.repositories.master.backstage.BackMarketPriceMaster;
import com.gws.repositories.slave.backstage.BackMarketPriceSlave;
import com.gws.services.backstage.BackMarketPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/21.
 */
@Service
public class BackMarketPriceServiceImpl implements BackMarketPriceService {

    private final BackMarketPriceSlave backMarketPriceSlave;

    private final BackMarketPriceMaster backMarketPriceMaster;

    @Autowired
    public BackMarketPriceServiceImpl(BackMarketPriceSlave backMarketPriceSlave, BackMarketPriceMaster backMarketPriceMaster) {
        this.backMarketPriceSlave = backMarketPriceSlave;
        this.backMarketPriceMaster = backMarketPriceMaster;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePrice(MarketPriceBO marketPriceBO) {
        MarketPrice one = backMarketPriceSlave.findOne(1L);
        if(one==null){
            MarketPrice marketPrice = new MarketPrice();
            marketPrice.setId(1L);
            marketPrice.setBuySpread(marketPriceBO.getBuySpread());
            marketPrice.setSellSpread(marketPriceBO.getSellSpread());
            marketPrice.setCnysgd(marketPriceBO.getCnysgd());
            backMarketPriceMaster.save(marketPrice);
            return;
        }

        MarketPrice marketPrice = new MarketPrice();
        marketPrice.setBuySpread(marketPriceBO.getBuySpread());
        marketPrice.setSellSpread(marketPriceBO.getSellSpread());
        marketPrice.setCnysgd(marketPriceBO.getCnysgd());
        backMarketPriceMaster.updateById(marketPrice,1L,"cnysgd","sellSpread","buySpread");
    }
}
