package com.gws.entity.backstage.createRawTransaction;

import lombok.Data;

/**
 * @author ylx
 * Created by fuzamei on 2018/5/14.
 */
@Data
public class SendTXResp {

    private Integer id;

    /**
     */
    private String result;

    /**
     * 返回的错误信息
     */
    private String error;
}
