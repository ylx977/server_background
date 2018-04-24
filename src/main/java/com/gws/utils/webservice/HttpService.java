package com.gws.utils.webservice;

import com.gws.utils.webservice.impl.CommonResponse;

import java.util.Map;

/**
 * @author wangdong
 */
public interface HttpService {

    /**
     *
     * http服务调用接口
     * @param service 服务名
     * @param methodName 方法名
     * @param params 请求参数
     * @param <T>
     * @return
     */
    <T extends CommonResponse> T call(String service, String methodName, Map<String, Object> params, Class<T> clazz);

    String call(String url, Map<String, String> params);

}
