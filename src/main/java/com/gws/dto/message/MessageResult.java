package com.gws.dto.message;

import com.gws.dto.message.UserMessage;
import lombok.Data;

import java.util.List;

/**
 * @Author : Kumamon 熊本同学
 * @Description:
 * @Date create in ${time} on ${data}
 * @Modified By:
 */
@Data
public class MessageResult {

    /**
     * 条数
     */
    private Integer total;

    /**
     * 是否有下一页
     */
    private Boolean hasNext;

    /**
     * 集合
     */
    private List<UserMessage> list;

    /**
     * 最新的一条数据的创建时间
     */
    private Integer ctime;

}
