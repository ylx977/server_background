package com.gws.controllers.backMarketPrice;

import com.alibaba.fastjson.JSON;
import com.gws.common.constants.backstage.RedisConfig;
import com.gws.controllers.BaseController;
import com.gws.controllers.JsonResult;
import com.gws.entity.backstage.MarketPrice;
import com.gws.exception.ExceptionUtils;
import com.gws.services.backstage.BackMarketPriceService;
import com.gws.utils.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ylx
 * Created by fuzamei on 2018/5/30.
 */
@RestController
@RequestMapping("/api/backmarketprice")
public class MarketPriceController extends BaseController{

    private final HttpServletRequest request;

    private final BackMarketPriceService backMarketPriceService;

    private final RedisUtil redisUtil;

    @Autowired
    public MarketPriceController(HttpServletRequest request, BackMarketPriceService backMarketPriceService, RedisUtil redisUtil) {
        this.request = request;
        this.backMarketPriceService = backMarketPriceService;
        this.redisUtil = redisUtil;
    }

    @RequestMapping("marketPrice")
    public MarketPrice marketPrice(){
        try {
            Object marketPriceCache = redisUtil.get(RedisConfig.MARKET_PRICE);
            if(StringUtils.isEmpty(marketPriceCache)){
                MarketPrice marketPrice = backMarketPriceService.queryPrice(null);
                Long id = marketPrice.getId();
                if(id==null){
                    return marketPrice;
                }else{
                    redisUtil.set(RedisConfig.MARKET_PRICE,JSON.toJSONString(marketPrice));
                    return marketPrice;
                }
            }else{
                return JSON.parseObject(marketPriceCache.toString(),MarketPrice.class);
            }
        }catch(Exception e){
            return new MarketPrice();
        }

    }

}
