package com.gws.entity.backstage.createRawTransaction;

import lombok.Data;

/**
 * @author ylx
 * Created by fuzamei on 2018/5/14.
 */
@Data
public class RawTXResp {

    private Integer id;

    /**
     * 这个是返回的元数据，就是那个unsigntx的值
     */
    private String result;

    /**
     * 返回的错误信息，如果没错，就是null
     */
    private String error;

}
