package com.gws.controllers.backstage;

import com.gws.configuration.backstage.UidConfig;
import com.gws.controllers.BaseController;
import com.gws.controllers.JsonResult;
import com.gws.dto.backstage.PageDTO;
import com.gws.entity.backstage.FeedbackBO;
import com.gws.enums.SystemCode;
import com.gws.services.backstage.BackFeedbackService;
import com.gws.utils.validate.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ylx
 * Created by fuzamei on 2018/5/8.
 */
@RestController
@RequestMapping("/api/backstage/feedback")
public class BackFeedbackController extends BaseController{

    private final HttpServletRequest request;

    private final BackFeedbackService backFeedbackService;

    private static final Logger LOGGER = LoggerFactory.getLogger(BackFeedbackController.class);

    @Autowired
    public BackFeedbackController(HttpServletRequest request, BackFeedbackService backFeedbackService) {
        this.request = request;
        this.backFeedbackService = backFeedbackService;
    }

    /**
     * 查询前台用户反馈的信息
     * {
     *     "page"
     *     "rowNum"
     *     "problemType"
     *     "startTime"
     *     "endTime"
     * }
     * @param feedbackBO
     * @return
     */
    @RequestMapping(value = "/queryFeedbacks")
    public JsonResult queryFeedbacks(@RequestBody FeedbackBO feedbackBO){
        Long uid = UidConfig.getUid();
        LOGGER.info("用户:{},修改后台banner基本配置信息",uid);
        try {
            validate(feedbackBO);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e);
        }
        try {
            PageDTO pageDTO = backFeedbackService.queryFeedbacks(feedbackBO);
            return success(pageDTO);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e);
        }
    }


    /**
     * 校验的公共方法
     * @param feedbackBO
     */
    private static final void validate(FeedbackBO feedbackBO){
        ValidationUtil.checkMinAndAssignInt(feedbackBO.getRowNum(),1);
        ValidationUtil.checkMinAndAssignInt(feedbackBO.getPage(),1);
//        ValidationUtil.checkRangeAndAssignInt(feedbackBO.getProblemType(),1,8,lang);
        Integer endTime = ValidationUtil.checkAndAssignDefaultInt(feedbackBO.getEndTime(),Integer.MAX_VALUE);
        Integer startTime = ValidationUtil.checkAndAssignDefaultInt(feedbackBO.getStartTime(),0);
        if(startTime > endTime){
            feedbackBO.setEndTime(Integer.MAX_VALUE);
        }else{
            feedbackBO.setEndTime(endTime);
        }
    }

}
