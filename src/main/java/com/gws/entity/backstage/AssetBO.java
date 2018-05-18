package com.gws.entity.backstage;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/25.
 */
@Data
public class AssetBO {

    private Double gold;

    private Long id;

    private String address;

    private String tag;

    private String isDelete;

    private Integer lang;

    private String toAddress;

    private Double amount;

    private Double fee;

    private String code;

    /**
     * 前台用户提供的备注信息
     */
    private String note;

    private HttpServletRequest request;

}
