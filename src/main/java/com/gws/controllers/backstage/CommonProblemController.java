package com.gws.controllers.backstage;

import com.gws.configuration.backstage.UidConfig;
import com.gws.controllers.BaseController;
import com.gws.controllers.JsonResult;
import com.gws.dto.backstage.PageDTO;
import com.gws.entity.backstage.NoticeContent;
import com.gws.entity.backstage.ProblemBO;
import com.gws.entity.backstage.ProblemContent;
import com.gws.services.backstage.BackProblemService;
import com.gws.utils.validate.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ylx
 * 常见问题模块
 * Created by fuzamei on 2018/6/21.
 */
@RestController
@RequestMapping(("/api/backstage/problem"))
public class CommonProblemController extends BaseController{

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonProblemController.class);

    private final BackProblemService backProblemService;

    @Autowired
    public CommonProblemController(BackProblemService backProblemService) {
        this.backProblemService = backProblemService;
    }

    /**
     * 查询常见问题
     {
        "page"
        "rowNum"
        "id"
        "title"
        "author"
     }
     * @param problemBO
     * @return
     */
    @RequestMapping("/queryProblems")
    public JsonResult queryProblems(@RequestBody ProblemBO problemBO){
        Long uid = UidConfig.getUid();
        LOGGER.info("用户:{},查询常见问题",uid);
        try {
            ValidationUtil.checkMinAndAssignInt(problemBO.getRowNum(),1);
            ValidationUtil.checkMinAndAssignInt(problemBO.getPage(),1);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e);
        }
        try {
            PageDTO pageDTO = backProblemService.queryProblem(problemBO);
            return success(pageDTO);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e);
        }
    }

    /**
     * 查询常见问题内容
      {
         "contentId"
      }
     * @param problemBO
     * @return
     */
    @RequestMapping("/queryProblemContent")
    public JsonResult queryProblemContent(@RequestBody ProblemBO problemBO){
        Long uid = UidConfig.getUid();
        LOGGER.info("用户:{},查询常见问题内容",uid);
        try {
            ValidationUtil.checkAndAssignLong(problemBO.getContentId());
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e);
        }
        try {
            ProblemContent problemContent = backProblemService.queryProblemContent(problemBO);
            return success(problemContent);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e);
        }
    }

    /**
     * 修改常见问题内容
     {
         "contentId"
         "problemContent"
         "title"
     }
     * @param problemBO
     * @return
     */
    @RequestMapping("/updateProblem")
    public JsonResult updateProblem(@RequestBody ProblemBO problemBO){
        Long uid = UidConfig.getUid();
        LOGGER.info("用户:{},修改常见问题内容",uid);
        try {
            ValidationUtil.checkAndAssignLong(problemBO.getContentId());
            ValidationUtil.checkBlankString(problemBO.getProblemContent());
            ValidationUtil.checkBlankString(problemBO.getTitle());
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e);
        }
        try {
            backProblemService.updateProblem(problemBO);
            return success(null);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e);
        }
    }

    /**
     * 删除常见问题
     {
         "id"
     }
     * @param problemBO
     * @return
     */
    @RequestMapping("/deleteProblem")
    public JsonResult deleteProblem(@RequestBody ProblemBO problemBO){
        Long uid = UidConfig.getUid();
        LOGGER.info("用户:{},删除常见问题",uid);
        try {
            ValidationUtil.checkAndAssignLong(problemBO.getId());
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e);
        }
        try {
            backProblemService.deleteProblem(problemBO);
            return success(null);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e);
        }
    }

    /**
     * 新建常见问题
     {
         "title"
         "problemContent"
     }
     * @param problemBO
     * @return
     */
    @RequestMapping("/publishProblem")
    public JsonResult publishProblem(@RequestBody ProblemBO problemBO){
        Long uid = UidConfig.getUid();
        LOGGER.info("用户:{},新建常见问题",uid);
        try {
            ValidationUtil.checkBlankString(problemBO.getTitle());
            ValidationUtil.checkBlankString(problemBO.getProblemContent());
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e);
        }
        try {
            backProblemService.publishNotice(problemBO);
            return success(null);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e);
        }
    }



}
