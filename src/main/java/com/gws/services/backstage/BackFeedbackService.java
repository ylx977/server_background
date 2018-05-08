package com.gws.services.backstage;

import com.gws.dto.backstage.PageDTO;
import com.gws.entity.backstage.FeedbackBO;

/**
 * @author ylx
 * Created by fuzamei on 2018/5/8.
 */
public interface BackFeedbackService {

    /**
     * 查询前台用户返回的意见信息
     * @param feedbackBO
     * @return
     */
    PageDTO queryFeedbacks(FeedbackBO feedbackBO);

}
