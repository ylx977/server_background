package com.gws.entity.backstage;

import lombok.Data;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/21.
 */
@Data
public class MarketPriceBO {

    /**
     * 多少人民币/1新加坡元
     */
    private Double cnysgd;

    /**
     * 买入点差
     */
    private Double buySpread;

    /**
     * 买入点差
     */
    private Double sellSpread;

}
