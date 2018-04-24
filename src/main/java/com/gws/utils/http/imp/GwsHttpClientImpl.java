
package com.gws.utils.http.imp;

import java.io.IOException;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.gws.utils.http.HTTP;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.HttpUrl.Builder;

/**
 * 封装http协议，简化操作
 *
 * @version 
 * @author wangdong  2016年7月16日 下午10:51:51
 * 
 */
public  class GwsHttpClientImpl implements HTTP {
	public  final MediaType MEDIA_JSON = MediaType.parse("application/json; charset=utf-8");
	
	@Autowired
	private  OkHttpClient httpClient;
	
	
	
	
	/**
	 * 
	 * GET极简同步方法
	 * 
	 * @author wangdong 2016年7月17日
	 * @param url
	 * @return
	 * @throws HttpGwsException 
	 * @throws IOException
	 */
	@Override
	public  byte[] GET(String url) throws HttpGwsException,IOException {
		Request request = new Request.Builder()
										.url(url).get()
										.build();
		Response response =ReqExecute(request);
		if (!response.isSuccessful()) {
			throw new HttpGwsException(response,"exception code:" + response.code());
		}
		return response.body().bytes();
	}
	
	/**
	 * 
	 * POST极简同步方法，JSON内容
	 * 
	 * @author wangdong 2016年7月17日
	 * @param url
	 * @param jsonBody
	 * @return
	 * @throws HttpGwsException
	 * @throws IOException
	 */
	@Override
	public  byte[] POST(String url,String jsonBody) throws HttpGwsException,IOException {
		RequestBody body = RequestBody.create(MEDIA_JSON, jsonBody);
		Request request = new Request.Builder()
										.url(url)
										.post(body)
										.build();
		Response response =ReqExecute(request);
		if (!response.isSuccessful()) {
			throw new HttpGwsException(response,"exception code:" + response.code());
		}
		return response.body().bytes();
	}
		
	/**
	 * 
	 * GET 同步方法
	 * 
	 * @author wangdong 2016年7月17日
	 * @param baseUrl
	 * @param queryParams
	 * @return
	 * @throws HttpGwsException
	 * @throws IOException
	 */
	@Override
	public  byte[] GET(String baseUrl,Map<String, String> queryParams) throws HttpGwsException,IOException {
		//拼装param
		Builder urlBuilder = HttpUrl.parse(baseUrl).newBuilder();
		for (Map.Entry<String, String> item : queryParams.entrySet()) {
			urlBuilder.addQueryParameter(item.getKey(), item.getValue());
		}
		HttpUrl httpUrl= urlBuilder.build();
		//发送请求
		Request request = new Request.Builder()
										.url(httpUrl.toString()).get()
										.build();
		Response response = ReqExecute(request);
		if (!response.isSuccessful()) {
			throw new HttpGwsException(response,"exception code:" + response.code());
		}
		return response.body().bytes();
	}
	
	/**
	 * 
	 * POST同步方法
	 * 
	 * @author wangdong 2016年7月17日
	 * @param baseUrl
	 * @param bodyParams
	 * @return
	 * @throws HttpGwsException
	 * @throws IOException
	 */
	@Override
	public  byte[] POST(String baseUrl,Map<String, String> bodyParams) throws HttpGwsException,IOException {
		//encode params
		okhttp3.FormBody.Builder bodyBuilder = new FormBody.Builder();
		for (Map.Entry<String, String> item : bodyParams.entrySet()) {
			bodyBuilder.addEncoded(item.getKey(), item.getValue());
		}
		FormBody formBody = bodyBuilder.build();
		//发送请求
		Request request = new Request.Builder()
										.url(baseUrl).post(formBody)
										.build();
		
		Response response = ReqExecute(request);
		if (!response.isSuccessful()) {
			throw new HttpGwsException(response,"exception code:" + response.code());
		}
		return response.body().bytes();
	}
	
	
	/**
	 * 
	 * GET异步方法
	 * 
	 * @author wangdong 2016年7月17日
	 * @param request
	 * @param responseHandle
	 * @throws IOException
	 */
	@Override
	public  void asyncGET(Request request,Callback responseHandle) throws IOException{
		ReqExecuteCall(request).enqueue(responseHandle);
	}
	
	/**
	 * 
	 * GET方法 异步请求
	 * 
	 * @author wangdong 2016年7月17日
	 * @param baseUrl
	 * @param queryParams
	 * @param responseHandle
	 * @throws HttpGwsException
	 * @throws IOException
	 */
	@Override
	public  void asyncGET(String baseUrl,
						Map<String, String> queryParams,Callback responseHandle) throws HttpGwsException,IOException {
		//拼装param
		Builder urlBuilder = HttpUrl.parse(baseUrl).newBuilder();
		for (Map.Entry<String, String> item : queryParams.entrySet()) {
			urlBuilder.addQueryParameter(item.getKey(), item.getValue());
		}
		HttpUrl httpUrl= urlBuilder.build();
		//发送请求
		Request request = new Request.Builder()
										.url(httpUrl.toString()).get()
										.build();
		ReqExecuteCall(request).enqueue(responseHandle);
	}
	
	
	/**
	 * 
	 * POST异步方法
	 * 
	 * @author wangdong 2016年7月17日
	 * @param baseUrl
	 * @param jsonBody
	 * @param responseHandle
	 */
	@Override
	public  void asyncPOST(String baseUrl,String jsonBody,Callback responseHandle){
		RequestBody body = RequestBody.create(MEDIA_JSON, jsonBody);
		Request request = new Request.Builder()
										.url(baseUrl)
										.post(body)
										.build();
		ReqExecuteCall(request).enqueue(responseHandle);
	}
	
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
	@Override
	public  void asyncPOST(String baseUrl,Map<String, String> bodyParams,Callback responseHandle) throws HttpGwsException,IOException{
		//encode params
		okhttp3.FormBody.Builder bodyBuilder = new FormBody.Builder();
		for (Map.Entry<String, String> item : bodyParams.entrySet()) {
			bodyBuilder.addEncoded(item.getKey(), item.getValue());
		}
		FormBody formBody = bodyBuilder.build();
		//发送请求
		Request request = new Request.Builder()
										.url(baseUrl).post(formBody)
										.build();
		ReqExecuteCall(request).enqueue(responseHandle);	
	}
	
	
	
	/**
	 * 
	 * 请求方法
	 * 
	 * @author wangdong 2016年7月17日
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@Override
	public  Response ReqExecute(Request request) throws IOException{
		 return ReqExecuteCall(request).execute();
	}
	
	/**
	 * 
	 * 构造CALL方法
	 * 
	 * @author wangdong 2016年7月17日
	 * @param request
	 * @return
	 */
	@Override
	public  Call ReqExecuteCall(Request request){
		 return httpClient.newCall(request);
	}

	
	}
