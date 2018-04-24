
package com.gws.base;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;


/**
 * GWS测试框架bean注入注解
 *
 * @version 
 * @author wangdong  2016年06月16日 下午6:18:03
 * 
 */
@Target({java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TestInject {
	boolean required() default true;
}
