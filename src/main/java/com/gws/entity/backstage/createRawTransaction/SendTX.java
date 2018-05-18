package com.gws.entity.backstage.createRawTransaction;

import lombok.Data;

/**
 * @author ylx
 * Created by fuzamei on 2018/5/14.
 */
@Data
public class SendTX {

    /**
     * 发送4.1.1 构造交易CreateRawTransaction返回的结果中的result中的结果
     */
    private String unsignTx;

    /**
     * 对unsigntx进行签名后的结果
     */
    private String sign;

    /**
     * 用户的公钥
     */
    private String pubkey;

    /**
     *
     */
    private Integer ty;

}
