package com.gws.entity.backstage;

import lombok.Data;

/**
 * @author ylx
 * Created by fuzamei on 2018/5/8.
 */
@Data
public class FeedbackBO {

    /**
     * 问题类型
     */
    private Integer problemType;

    private Integer page;

    private Integer rowNum;

    private Integer startTime;

    private Integer endTime;

    private Integer lang;

}
