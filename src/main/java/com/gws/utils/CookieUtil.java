package com.gws.utils;

import org.aspectj.util.FileUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * cookie 工具类
 * @version 1.0
 * @author wangdong<wangdong@zjyushi.com>
 * @data
 */
public class CookieUtil {


	public static String COOKIE_DOMAIN;

	/**
	 * 添加cookie信息
	 * @param response
	 * @param name
	 * @param value
	 * @param maxAge
	 */
	public static void addCookie(HttpServletResponse response, String name,
                                 String value, int maxAge){
		try {
			Cookie cookie=new Cookie(name, value);
			cookie.setMaxAge(maxAge);
			cookie.setDomain(COOKIE_DOMAIN) ;
			cookie.setPath("/");
			response.addCookie(cookie);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	 }

	
	/**
	 * 获取cookie值
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getValue(HttpServletRequest request, String name){
		String value=null;
		Cookie[] cookies=request.getCookies();
		if(cookies!=null){
			for(Cookie cookie:cookies){
				if(name.equals(cookie.getName())){
					try {
						value= URLDecoder.decode(cookie.getValue(),"utf-8");
					} catch (UnsupportedEncodingException e) {
						throw new RuntimeException(e.getMessage());
					}
					break;
				}
			}
		}
		return value;
	}
	

}
