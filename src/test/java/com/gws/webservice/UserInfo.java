
package com.gws.webservice;

import java.io.Serializable;

/**
 * 用户访问日志
 *
 * @version 
 * @author wangdong  2016年4月19日 下午5:14:38
 * 
 */
public class UserInfo implements Serializable {

    private String name;
    private Integer age;

    public UserInfo() {}
    public UserInfo(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
