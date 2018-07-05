package com.gws.utils.http;

import com.gws.common.constants.backstage.ErrorMsg;
import com.gws.common.constants.backstage.LangMark;
import com.gws.configuration.backstage.LangConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @author ylx
 */
@Component
public class LangReadUtil {

	/**
	 *
	 */
	public static Properties CN_PROPERTIES = new Properties();
	public static Properties EN_PROPERTIES = new Properties();

	static {
		try {
			InputStream inputStream1 = Thread.currentThread().getContextClassLoader().getResource("cn.properties").openStream();
			InputStream inputStream2 = Thread.currentThread().getContextClassLoader().getResource("en.properties").openStream();
			CN_PROPERTIES.load(new InputStreamReader(inputStream1,"utf-8"));
			EN_PROPERTIES.load(new InputStreamReader(inputStream2,"utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("配置文件读取异常");
		}
	}

	/**
	 * @author ylx
	 * @param key
	 * @param
	 * @descri 读取conf.properties配置文件中的某个key值对应的值
	 * @return
	 */
	public static final String getProperty(String key){
			int lang = LangConfig.getLang();
			if(lang == LangMark.CN){
				return CN_PROPERTIES.getProperty(key);
			}else if(lang == LangMark.EN){
				return EN_PROPERTIES.getProperty(key);
			}else{
				return CN_PROPERTIES.getProperty(key);
			}
	}

	public static final String getProperty(ErrorMsg msg){
			int lang = LangConfig.getLang();
			if(lang == LangMark.CN){
				return msg.getMessageCN();
			}else if(lang == LangMark.EN){
				return msg.getMessageEN();
			}else{
				return msg.getMessageCN();
			}
	}


}
