package com.gws.entity.backstage.price;

import com.alibaba.fastjson.JSON;
import com.gws.utils.http.HttpRequest;
import lombok.Data;

/**
 * @author ylx
 * Created by fuzamei on 2018/6/5.
 */
@Data
public class Price {

    /**
     * 对应柯凡的接口http://server.codingfine.com:9090
     * 需要的参数json形式如下所示
     {
         "cnysgdbuy":4.7,
         "cnysgdsell":4.8,
         "sgdusdbuy":1.34,
         "sgdusdsell":1.35,
         "askSpread":0.6
         "bidSpread":0.6
     }
     */
    public Price(){}
    public Price(Double cnysgdbuy,Double cnysgdsell,Double sgdusdbuy,Double sgdusdsell,Double askSpread,Double bidSpread){
        this.cnysgdbuy = cnysgdbuy;
        this.cnysgdsell = cnysgdsell;
        this.sgdusdbuy = sgdusdbuy;
        this.sgdusdsell = sgdusdsell;
        this.askSpread = askSpread;
        this.bidSpread = bidSpread;
    }

    public static final Price getInstance(Double cnysgdbuy,Double cnysgdsell,Double sgdusdbuy,Double sgdusdsell,Double askSpread,Double bidSpread){
        return new Price(cnysgdbuy,cnysgdsell,sgdusdbuy,sgdusdsell,askSpread,bidSpread);
    }

    private Double cnysgdbuy;
    private Double cnysgdsell;
    private Double sgdusdbuy;
    private Double sgdusdsell;
    private Double askSpread;
    private Double bidSpread;

}
