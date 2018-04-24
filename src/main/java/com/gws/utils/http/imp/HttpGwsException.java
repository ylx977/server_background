
package com.gws.utils.http.imp;

import java.io.IOException;

import okhttp3.Response;

/**
 * http自定义异常类
 *
 * @version 
 * @author wangdong  2016年7月17日 下午2:28:00
 * 
 */
public class HttpGwsException extends IOException {
	private Response response;
	private static final long serialVersionUID = -4802242016364002941L;
	
    public HttpGwsException(IOException e) {
        super(e);
    }
    
    public HttpGwsException(Response response,String message) {
        super(message);
        this.setResponse(response);
    }

	/**
	 * @return the response
	 */
	public Response getResponse() {
		return response;
	}

	/**
	 * @param response the response to set
	 */
	public void setResponse(Response response) {
		this.response = response;
	}
}
