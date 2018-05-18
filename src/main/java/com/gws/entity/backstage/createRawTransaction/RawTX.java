package com.gws.entity.backstage.createRawTransaction;

import lombok.Data;

/**
 * @author ylx
 * Created by fuzamei on 2018/5/14.
 */
@Data
public class RawTX {

    /**
     * 要打币的地址
     */
    private String to;

    /**
     * 转币的数量，比如转1个BTY，就要乘以10的8次方
     */
    private Long amount;

    /**
     * 旷工费，和amount一样，都要乘以10的8次方
     */
    private Long fee;

    /**
     * 备注信息，可以不写
     */
    private String note;

    /**
     *
     */
    private Boolean isToken;

    /**
     *
     */
    private String tokenSymbol;
}
