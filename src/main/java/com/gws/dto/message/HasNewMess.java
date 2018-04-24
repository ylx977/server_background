package com.gws.dto.message;

import lombok.Data;

import java.util.List;

/**
 * @Author : Kumamon 熊本同学
 * @Description:
 * @Date create in ${time} on ${data}
 * @Modified By:
 */
@Data
public class HasNewMess {

    /**
     * 最新的一条数据的创建时间
     */
    private Integer ctime;

    /**
     * 是否有新消息
     */
    private Boolean hasNewMess;
}
