
package com.gws.configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gws.utils.http.HTTP;
import com.gws.utils.http.imp.GwsHttpClientImpl;
import com.gws.utils.http.imp.HttpClientMetricsInterceptor;
import com.gws.utils.http.imp.HttpLoggerInterceptor;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

/**
 * http client配置
 *
 * @version 
 * @author wangdong  2016年7月17日 下午5:58:37
 * 
 * 
 */
@Configuration
@EnableAutoConfiguration
public class HttpClientConfig {
	//默认时间为秒
	@Value("${http.connectTimeout}")
	private Long connectTimeout=10000L;
	@Value("${http.readTimeout}")
	private Long readTimeout=10000L;
	@Value("${http.writeTimeout}")
	private Long writeTimeout=10000L;
	@Value("${http.maxIdleConnections}")
	private int maxIdleConnections=5;
	@Value("${http.keepAliveDurationNs}")
	private Long keepAliveDurationNs=5L;
	//连接失败重试，默认打开开关 避免Unreachable IP addresses，和连接池满了拒绝请求情况
	private static boolean retryOnConnectionFailure = true;
	
	@Autowired
	private HttpClientMetricsInterceptor httpClientMetricsInterceptor;
	
	@Autowired
	private HttpLoggerInterceptor httpLoggerInterceptor;
	
	@Bean
	public HttpClientMetricsInterceptor getHttpClientMetricsInterceptor(){
		return new HttpClientMetricsInterceptor();
	}
	
	@Bean
	public HttpLoggerInterceptor getHttpLoggerInterceptor(){
		return new HttpLoggerInterceptor();
	}
	
    @Bean
    public OkHttpClient okHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        ConnectionPool connectionPool = new ConnectionPool(maxIdleConnections, keepAliveDurationNs, TimeUnit.MINUTES);
        builder.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
        builder.readTimeout(readTimeout, TimeUnit.MILLISECONDS);
        builder.writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);
        builder.retryOnConnectionFailure(retryOnConnectionFailure);
        builder.connectionPool(connectionPool);
        //增加监控
        builder.addInterceptor(httpClientMetricsInterceptor);
        builder.addInterceptor(httpLoggerInterceptor);
        //由于是服务队服务的 所以不用设置访问所有的HTTPS站点配置
        return builder.build();
    }
    
   
    
    @Bean
    public HTTP httpFactory(){
    	HTTP httpClient = new GwsHttpClientImpl();
    	return httpClient;
    }

}
