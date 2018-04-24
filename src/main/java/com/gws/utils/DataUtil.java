package com.gws.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;


/**
 * 【数据工具类】
 *
 * @version 
 * @author wenfei  2017年2月16日 上午10:32:17
 * 
 */
public class DataUtil {

	/**
	 * 
	 * 【map的value非空判断】
	 * 
	 * @author wenfei 2017年4月10日
	 * @param map
	 * @param key
	 * @param clazz
	 * @return
	 */
	public static <T> T mapValueJudge(Map<?, ?> map, String key, Class<T> clazz){
		if(null == map || map.isEmpty() || StringUtils.isEmpty(key) || null == clazz){
			return null;
		}
		
		Object value = map.get(key);
		
		if(null == value){
			return null;
		}
		
		T t = clazz.cast(value);
		return t;
	}
	
	

	
	/**
	 * 
	 * map to object
	 * 
	 * @author wangdong 2017年4月12日
	 * @param map
	 * @param clazz
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static  Object ConvertMapToObject(Map<?, ?> map,Object object) throws IllegalAccessException, InvocationTargetException{
		BeanUtils.populate(object, (Map<String, ? extends Object>) map);
		return  object;
	}
	
	
}
