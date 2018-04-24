
package com.gws.utils.event;

/**
 * message事件
 *
 * @version 
 * @author wangdong  2017年4月18日 下午11:30:34
 * 
 */
public interface LocalEvent {
	  /**
     * 事件所属主题
     *
     * @return
     */
    String getTopic();
    
    /**
     * 事件内容
     *
     * @return
     */
    Object getContent();
    
    
}
