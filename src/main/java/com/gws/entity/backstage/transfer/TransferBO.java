package com.gws.entity.backstage.transfer;

import lombok.Data;

/**
 * @author ylx
 * Created by fuzamei on 2018/6/8.
 */
@Data
public class TransferBO {

    private String toAddress;
    private String privateKey;
    private String publicKey;
    private Double amount;

}
