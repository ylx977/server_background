package com.gws.entity.backstage;

import lombok.Data;

import java.util.Map;

/**
 * @author ylx
 * Created by fuzamei on 2018/5/12.
 */
@Data
public class SMResult {

    private Integer code;

    private String message;

    private String error;

    private Object data;

}
