
package com.gws.utils.http.imp;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.gws.utils.GwsLogger;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * HTTP客户端HTTP请求日志
 *
 * @version 
 * @author wangdong  2016年7月17日 下午7:15:30
 * 
 */
public class HttpLoggerInterceptor implements Interceptor {
	private Long startTime=null;
	private int reqId=0;
	/**
	 * 拦截生成日志
	 * 
	 * (non-Javadoc)
	 * @see okhttp3.Interceptor#intercept(okhttp3.Interceptor.Chain)
	 */
	@Override
	public Response intercept(Chain chain) throws IOException {
		 Request request = chain.request();
		 Response response = null;
	     try {
	    	 //请求前报文
	    	 beforeRequest(request);
	         response = chain.proceed(request);
	        } finally {
	           //返回后报文
	        	if(response!=null){
	        		return afterRequest(response);
	        	}
	       }
		return response;
	}
	
	private void beforeRequest(Request request){
		 startTime = System.currentTimeMillis();
		 String url = request.url().toString();
		 String content="blank";
		 reqId =request.hashCode();
		  try
	        {
			  if(request.body()!=null){
		            final Request cloneReq = request.newBuilder().build();
		            final Buffer buffer = new Buffer();
		            cloneReq.body().writeTo(buffer);
		            content= buffer.readUtf8();
			  }
				
	          GwsLogger.info(String.format("[hc request log,`reqId:%s`url:%s,`content:`%s]",reqId,url,content));
	        } catch (final IOException e)
	        {
	           GwsLogger.error("[HttpLoggerInterceptor error:]"+e);
	        }
	}
	
	private Response  afterRequest(Response response){
		 String url = response.request().url().toString();
		 Response.Builder builder = response.newBuilder();
         Response cloneRespone = builder.build();
         ResponseBody body = cloneRespone.body();
		 //返回时间
		 Long responseTime = System.currentTimeMillis()-startTime;
         if(body!=null){
        	 MediaType mediaType = body.contentType();
             if (mediaType != null)
             {
                 if (isText(mediaType))
                 {
                    String resp = null;
					try {
						resp = body.string();
					} catch (IOException e) {
				           GwsLogger.error("[HttpLoggerInterceptor error:]"+e);
					}
                    body = ResponseBody.create(mediaType, resp);

           		    Long durationTime = System.currentTimeMillis()-startTime;

           		    Long reqTime =TimeUnit.MILLISECONDS.toMillis(responseTime);
    				Long useTime =TimeUnit.MILLISECONDS.toMillis(durationTime);
    				
     	            GwsLogger.info(String.format("[hc response log, "
    	            		+ "reqId:%s,`url:%s,`http code:%s,`content:%s,`reqTime:%sms,`useTime:%sms]", 
    	            		reqId,url,cloneRespone.code(),resp,reqTime,useTime));
     	           response = response.newBuilder().body(body).build();
                 }
             }
        	 }
         return response;
	}
	

	/**
	 * 
	 * 只监控json,xml,html类型
	 * 
	 * @author wangdong 2016年7月18日
	 * @param mediaType
	 * @return
	 */
	 private boolean isText(MediaType mediaType)
	    {
	        if (mediaType.type() != null && mediaType.type().equals("text"))
	        {
	            return true;
	        }
	        if (mediaType.subtype() != null)
	        {
	            if (mediaType.subtype().equals("json") ||
	                    mediaType.subtype().equals("xml") ||
	                    mediaType.subtype().equals("html") ||
	                    mediaType.subtype().equals("webviewhtml")
	                    )
	                return true;
	        }
	        return false;
	    }


}
