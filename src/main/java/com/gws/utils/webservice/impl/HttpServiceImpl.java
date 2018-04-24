package com.gws.utils.webservice.impl;

import com.alibaba.fastjson.JSON;
import com.gws.utils.GwsLogger;
import com.gws.utils.http.HTTP;
import com.gws.utils.webservice.HttpService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangdong on 7/18/16.
 */
public class HttpServiceImpl implements HttpService {

    @Autowired
    private HTTP httpClient;

    @Override
    public <T extends CommonResponse> T call(String service, String methodName, Map<String, Object> params, Class<T> clazz) {
        if (StringUtils.isEmpty(service) || StringUtils.isEmpty(methodName) || null == clazz) {
            throw new IllegalArgumentException("远程调用请求错误：service , methodName, clazz不能为空");
        }

        T resp  = null;
        try {
            Map<String, String> data = new HashMap<>();
            if (null != params) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    data.put(entry.getKey(), entry.getValue().toString());
                }
            }
            data.put("requestId", String.valueOf(System.currentTimeMillis()));

            byte[] bytes =  httpClient.POST(service + methodName, data);
            resp = JSON.parseObject(new String(bytes), clazz);

        } catch (Exception e) {
            try {
            	 GwsLogger.error(e, "HttpServiceImpl call  POST请求异常");
                resp = clazz.newInstance();
            } catch (Exception ex) {
                GwsLogger.error(ex, "HttpServiceImpl 解析http响应错误");
            }
        }
        return resp;
    }

    @Override
    public String call(String url, Map<String, String> params) {
        byte[] bytes = new byte[0];
        try {
            bytes =  httpClient.GET(url, params);
        } catch (IOException e) {

        }

        return new String(bytes);
    }
    
	public String postCall(String url, Map<String, String> data) {
		byte[] bytes = new byte[0];
		try {
			GwsLogger.info("请求接口url={},参数={}", url,data);
			bytes = httpClient.POST(url, data);
		} catch (Exception e) {
			GwsLogger.error(e, "请求接口异常");
		}
		return new String(bytes);
	}
	
}
