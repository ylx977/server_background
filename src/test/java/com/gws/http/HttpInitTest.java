
package com.gws.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.gws.base.GwsBaseTestCase;
import com.gws.base.TestInject;
import com.gws.utils.http.HTTP;
import com.gws.utils.http.imp.HttpGwsException;

/**
 * HttpInitTest
 *
 * @version 
 * @author wangdong  2016年7月17日 下午6:20:25
 * 
 */
public class HttpInitTest extends GwsBaseTestCase {

	@TestInject
    private HTTP httpClient;
	
	public void testGet(){
		try {
			Map<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("ip", "117.89.35.58");
			for(int i=0;i<1;i++){
				byte[] resP =httpClient.GET("http://apis.baidu.com/apistore/iplookupservice/iplookup",queryParams);
				System.out.println(new String(resP));
			}
		} catch (HttpGwsException e) {
			// TODO 这是系统自动生成描述，请在此补完后续代码
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 这是系统自动生成描述，请在此补完后续代码
			e.printStackTrace();
		}
	}
}
