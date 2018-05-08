package com.gws.controllers.backstage;

import com.gws.controllers.BaseController;
import com.gws.controllers.JsonResult;
import com.gws.dto.backstage.PageDTO;
import com.gws.dto.backstage.UserDetailDTO;
import com.gws.entity.backstage.NoticeBO;
import com.gws.entity.backstage.NoticeContent;
import com.gws.services.backstage.BackNoticeService;
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
@RequestMapping("/api/backstage/notice")
public class BackNoticeController extends BaseController{

    private final HttpServletRequest request;

    private final BackNoticeService backNoticeService;

    private static final Logger LOGGER = LoggerFactory.getLogger(BackNoticeController.class);

    @Autowired
    public BackNoticeController(HttpServletRequest request, BackNoticeService backNoticeService) {
        this.request = request;
        this.backNoticeService = backNoticeService;
    }


    /**
     * 查询公告信息
     * {
     *     "page"
     *     "rowNum"
     *     "id"
     *     "title"
     *     "author"
     * }
     * @param noticeBO
     * @return
     */
    @RequestMapping("/queryNotices")
    public JsonResult queryNotices(@RequestBody NoticeBO noticeBO){
        Long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},查看后台设置的公告信息",uid);
        try {
            ValidationUtil.checkMinAndAssignInt(noticeBO.getRowNum(),1);
            ValidationUtil.checkMinAndAssignInt(noticeBO.getPage(),1);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e);
        }
        try {
            PageDTO pageDTO = backNoticeService.queryNotices(noticeBO);
            return success(pageDTO);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e);
        }
    }

    /**
     * 查询公告内容
     * {
     *     "contentId"
     * }
     * @param noticeBO
     * @return
     */
    @RequestMapping("/queryNoticeContent")
    public JsonResult queryNoticeContent(@RequestBody NoticeBO noticeBO){
        Long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},查看公告内容",uid);
        try {
            ValidationUtil.checkAndAssignLong(noticeBO.getContentId());
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e);
        }
        try {
            NoticeContent noticeContent = backNoticeService.queryNoticeContent(noticeBO);
            return success(noticeContent);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e);
        }
    }

    /**
     * 修改公告内容
     * {
     *     "contentId"
     *     "noticeContent"
     *     "title"
     * }
     * @param noticeBO
     * @return
     */
    @RequestMapping("/updateNotice")
    public JsonResult updateNotice(@RequestBody NoticeBO noticeBO){
        Long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},修改公告内容",uid);
        try {
            ValidationUtil.checkAndAssignLong(noticeBO.getContentId());
            ValidationUtil.checkBlankString(noticeBO.getNoticeContent());
            ValidationUtil.checkBlankString(noticeBO.getTitle());
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e);
        }
        try {
            backNoticeService.updateNotice(noticeBO);
            return success(null);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e);
        }
    }

    /**
     * 删除公告
     * {
     *     "id"
     * }
     * @param noticeBO
     * @return
     */
    @RequestMapping("/deleteNotice")
    public JsonResult deleteNotice(@RequestBody NoticeBO noticeBO){
        Long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},删除公告",uid);
        try {
            ValidationUtil.checkAndAssignLong(noticeBO.getId());
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e);
        }
        try {
            backNoticeService.deleteNotice(noticeBO);
            return success(null);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e);
        }
    }

    /**
     * 新建公告
     * {
     *     "title"
     *     "noticeContent"
     * }
     * @param noticeBO
     * @return
     */
    @RequestMapping("/publishNotice")
    public JsonResult publishNotice(@RequestBody NoticeBO noticeBO){
        Long uid = (Long) request.getAttribute("uid");
        UserDetailDTO userDetailDTO = (UserDetailDTO) request.getAttribute("userDetailDTO");
        LOGGER.info("用户:{},新建公告",uid);
        try {
            ValidationUtil.checkBlankString(noticeBO.getTitle());
            ValidationUtil.checkBlankString(noticeBO.getNoticeContent());
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e);
        }
        try {
            noticeBO.setUserDetailDTO(userDetailDTO);
            backNoticeService.publishNotice(noticeBO);
            return success(null);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e);
        }
    }

    /**
     * 修改公告状态
     * {
     *     "id"
     *     "type"
     *     "status"
     * }
     * @param noticeBO
     * @return
     */
    @RequestMapping("/updateNoticeStatus")
    public JsonResult updateNoticeStatus(@RequestBody NoticeBO noticeBO){
        Long uid = (Long) request.getAttribute("uid");
        LOGGER.info("用户:{},修改公告状态",uid);
        try {
            ValidationUtil.checkAndAssignLong(noticeBO.getId());
            ValidationUtil.checkRangeAndAssignInt(noticeBO.getType(),1,2);
            ValidationUtil.checkRangeAndAssignInt(noticeBO.getStatus(),0,1);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e);
        }
        try {
            backNoticeService.updateNoticeStatus(noticeBO);
            return success(null);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e);
        }
    }







}
