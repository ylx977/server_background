package com.gws.entity.backstage;

import lombok.Data;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/21.
 */
@Data
public class MarketPriceBO {

    /**
     * 多少人民币/1新加坡元--->买入价
     */
    private Double buyCnySgd;

    /**
     * 多少人民币/1新加坡元--->卖出价
     */
    private Double sellCnySgd;

    /**
     * 多少新元/1美元--->买入价
     */
    private Double buySgdUsd;

    /**
     * 多少新元/1美元--->卖出价
     */
    private Double sellSgdUsd;

    private Double buyBtyUsdg;
    private Double sellBtyUsdg;
    private Double tradeFee;

    /**
     * 买入点差
     */
    private Double buySpread;

    /**
     * 卖出点差
     */
    private Double sellSpread;

    private Integer lang;

    private Integer page;

    private Integer rowNum;

}
