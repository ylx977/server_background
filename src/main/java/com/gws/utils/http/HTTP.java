
package com.gws.utils.http;

import java.io.IOException;
import java.util.Map;

import com.gws.utils.http.imp.HttpGwsException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * HTTP客户端常用接口封装，简化操作，提升性能，后续支持注解
 * 参考RestTemplate
 * @version 
 * @author wangdong  2016年7月17日 上午9:29:16
 * 
 */
public interface HTTP {
	
	/**
	 * 
	 * GET同步方法
	 * 
	 * @author wangdong 2016年7月17日
	 * @param url
	 * @return
	 * @throws HttpGwsException
	 * @throws IOException
	 */
	public byte[] GET(String url) throws HttpGwsException,IOException;
	/**
	 * 
	 * GET同步方法
	 * 
	 * @author wangdong 2016年7月17日
	 * @param baseUrl
	 * @param queryParams
	 * @return
	 * @throws HttpGwsException
	 * @throws IOException
	 */
	public byte[] GET(String baseUrl,Map<String, String> queryParams) throws HttpGwsException,IOException;
	/**
	 * 
	 * GET异步方法
	 * 
	 * @author wangdong 2016年7月17日
	 * @param request
	 * @param responseHandle
	 * @throws IOException
	 */
	public  void asyncGET(Request request,Callback responseHandle) throws IOException;
	/**
	 * 
	 * GET异步方法
	 * 
	 * @author wangdong 2016年7月17日
	 * @param baseUrl
	 * @param queryParams
	 * @param responseHandle
	 * @throws HttpGwsException
	 * @throws IOException
	 */
	public  void asyncGET(String baseUrl,
			Map<String, String> queryParams,Callback responseHandle) throws HttpGwsException,IOException;
	
	/**
	 * 
	 * POST同步方法
	 * 
	 * @author wangdong 2016年7月17日
	 * @param url
	 * @param jsonBody
	 * @return
	 * @throws HttpGwsException
	 * @throws IOException
	 */
	public  byte[] POST(String url,String jsonBody) throws HttpGwsException,IOException;
	/**
	 * 
	 *  POST同步方法
	 * 
	 * @author wangdong 2016年7月17日
	 * @param baseUrl
	 * @param bodyParams
	 * @return
	 * @throws HttpGwsException
	 * @throws IOException
	 */
	public  byte[] POST(String baseUrl,Map<String, String> bodyParams) throws HttpGwsException,IOException;
	/**
	 * 
	 *  POST异步方法
	 * 
	 * @author wangdong 2016年7月17日
	 * @param baseUrl
	 * @param jsonBody
	 * @param responseHandle
	 */
	public  void asyncPOST(String baseUrl,String jsonBody,Callback responseHandle);
	/**
	 * 
	 * POST异步方法
	 * 
	 * @author wangdong 2016年7月17日
	 * @param baseUrl
	 * @param bodyParams
	 * @param responseHandle
	 * @throws HttpGwsException
	 * @throws IOException
	 */
	public  void asyncPOST(String baseUrl,Map<String, String> bodyParams,Callback responseHandle) throws HttpGwsException,IOException;
	
	public  Response ReqExecute(Request request) throws IOException;
	public  Call ReqExecuteCall(Request request);

}
