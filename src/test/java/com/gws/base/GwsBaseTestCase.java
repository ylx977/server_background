
package com.gws.base;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.Before;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import com.gws.GwsWebApplication;
import com.gws.utils.GwsLogger;

import junit.framework.TestCase;

/**
 * GWS框架单元测试基础类
 *
 * @version 
 * @author wangdong  2016年6月24日 上午9:54:13
 * 
 */
public class GwsBaseTestCase extends TestCase implements ITest {
	private static ApplicationContext ctx;
	
	
	@Before
	@Override
	protected void setUp() throws Exception {
		// TODO 这是系统自动生成描述，请在此补完后续代码
		super.setUp();
		String[] args = {};
        ctx = SpringApplication.run(GwsWebApplication.class, args);
        autoInjectObj();
        
	}


    
    /**
     * 
     * 自动注入对象
     * 
     * @author wangdong 2016年6月24日
     */
	private void autoInjectObj(){
    	Field[] fields = getClass().getDeclaredFields();
		for(Field field:fields){
			Annotation anno = field.getAnnotation(TestInject.class);
			if(anno!=null){
				try {
					field.setAccessible(true);
					field.set(this, getDestBean(field.getType()));
				} catch (Exception e) {
					GwsLogger.error(e.getMessage());
				}
			}
		}
    }



	/**
	 * 【请在此输入描述文字】
	 * 
	 * (non-Javadoc)
	 * @return 
	 * @see ITest#getDestBean(java.lang.Class)
	 */
	@Override
	public <T> T getDestBean(Class<T> className) {
		T obj =null;
		if(ctx!=null){
			obj = ctx.getBean(className);
		}
		return obj;
	}
    
    

}
