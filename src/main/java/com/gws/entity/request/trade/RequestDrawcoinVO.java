package com.gws.entity.request.trade;

import lombok.Data;

import javax.persistence.Entity;
import java.math.BigDecimal;

/**
 * @author WangBin
 */
@Data
public class RequestDrawcoinVO {
    /**
     * 用户id
     */
    private Long uid;
    /**
     * 提币类型
     */
    private Integer coinType;
    /**
     * 出去的地址
     */
    private String outerAddress;
    /**
     * 提币数量
     */
    private BigDecimal coinAmount;
    /**
     * 矿工费
     */
    private BigDecimal minerAmount;
}
