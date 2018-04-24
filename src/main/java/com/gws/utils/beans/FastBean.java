
package com.gws.utils.beans;

import java.beans.PropertyDescriptor;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.cglib.beans.BeanMap;

/**
 * 动态创建一个bean对象
 *
 * @version
 * @author wangdong 2017年4月12日 上午10:57:40
 * 
 */
public class FastBean {
	private Object objBean = null;
	private BeanMap beanPropMap = null;
	
	/**
	 * 
	 *动态bean对象创建方法
	 * 
	 * @author wangdong 2017年4月12日
	 * @param propertyMap
	 * @return
	 */
	public static FastBean builder(Map<String, Class<?>> propertyMap){
		FastBean fb= new FastBean();
		fb.toBean(propertyMap);
		return fb;
	}
	

	/**
	 * 
	 * 动态创建属性
	 * 
	 * @author wangdong 2017年4月12日
	 * @param propertyMap
	 * @return
	 */
	private void toBean(Map<String, Class<?>> propertyMap) {
		BeanGenerator generator = new BeanGenerator();
		propertyMap.forEach((k, v) -> {
			generator.addProperty(k, v);
		});
		this.objBean= generator.create();
		this.beanPropMap = BeanMap.create(objBean);
	}

	/**
	 * 
	 * 属性赋值
	 * 
	 * @author wangdong 2017年4月12日
	 * @param property
	 * @param value
	 */
	public FastBean setValue(String property, Object value) {
		beanPropMap.put(property, value);  
		return this;
	}
	
	
	/**
	 * 
	 * 获取属性值
	 * 
	 * @author wangdong 2017年4月12日
	 * @param property
	 * @return
	 */
	public Object getValue(String property){
		 return beanPropMap.get(property);
	}
	
	/**
	 * 
	 * 获取创建成功的bean对象
	 * 
	 * @author wangdong 2017年4月12日
	 * @return
	 */
	public Object getBeanObj(){
		return objBean;
	}

}
