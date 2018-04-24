package com.gws.utils;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HttpUtil {

	public static void httpResp(Object retResult, HttpServletResponse response){
		try {
			response.getOutputStream().write(JSONObject.toJSONString(retResult).getBytes("utf8"));
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
