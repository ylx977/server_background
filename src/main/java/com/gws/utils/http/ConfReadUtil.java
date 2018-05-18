package com.gws.utils.http;

import java.io.*;
import java.util.Properties;

/**
 * @author ylx
 */
public class ConfReadUtil {
	private ConfReadUtil() {
		throw new AssertionError("can not be instaniated");
	}

	/**
	 *
	 */
	public static Properties APPLICATION_PROPERTIES = new Properties();

	static {
		try {
//			ConfReadUtil.class.getClassLoader().getResource("application.properties").getPath();
			//必须通过流的形式去读取，如果是通过路径的形式去获取文件会读取不到文件,如上所示,以下两个都ok
//			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResource("application.properties").openStream();
			APPLICATION_PROPERTIES.load(inputStream);
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
//		return getProperty(key,"application.properties");
		return APPLICATION_PROPERTIES.getProperty(key);
	}
}
