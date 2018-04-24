package com.gws.webservice;


import com.gws.utils.webservice.impl.CommonResponse;

import java.util.Map;

/**
 * api请求统一响应数据类型
 * Created by wangdong on 7/18/16.
 */
public class Response extends CommonResponse {
    public Map<String, UserInfo> data;

    public Map<String, UserInfo> getData() {
        return data;
    }

    public void setData(Map<String, UserInfo> data) {
        this.data = data;
    }
}

