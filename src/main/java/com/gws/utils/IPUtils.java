package com.gws.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class IPUtils {

	//获得客户端真实IP地址的方法一：
	/*public static String getRemortIP(HttpServletRequest request) {  
	    if (request.getHeader("x-forwarded-for") == null) {  
	        return request.getRemoteAddr();  
	    }  
	    return request.getHeader("x-forwarded-for");  
	}  */
	
	public static String getRemortIP(HttpServletRequest request) {
		if (null == request) {
			return null;
		}
		String proxs[] = { "X-Forwarded-For", "Proxy-Client-IP",
				"WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR" };
		String ip = null;
		for (String prox : proxs) {
			ip = request.getHeader(prox);
			if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
				continue;
			} else {
				break;
			}
		}
		
		//对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if(ip!=null && ip.length()>15){ //"***.***.***.***".length() = 15
			if(ip.indexOf(",")>0){
				ip = ip.substring(0,ip.indexOf(","));
			}
		}
		
		if (StringUtils.isEmpty(ip)) {
			return request.getRemoteAddr();
		}
		return ip;
	}


//	　　获得客户端真实IP地址的方法二：
	public static String getIpAddr(HttpServletRequest request) {
	    String ip = request.getHeader("x-forwarded-for");  
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getHeader("Proxy-Client-IP");  
	    }  
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getHeader("WL-Proxy-Client-IP");  
	    }  
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getRemoteAddr();  
	    }  
	    return ip;  
	}  
		
	//获得客户端真实IP地址的方法三：
	public static String getIp3(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if(index != -1){
                return ip.substring(0,index);
            }else{
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            return ip;
        }
        return request.getRemoteAddr();
    }
}
