
package com.gws.base;

/**
 * 单元测试框架
 *
 * @version 
 * @author wangdong  2016年6月24日 下午1:37:56
 * 
 */
public interface ITest {
	<T> T getDestBean(Class<T> className);
}
