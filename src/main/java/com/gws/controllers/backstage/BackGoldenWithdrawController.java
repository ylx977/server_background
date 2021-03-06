package com.gws.controllers.backstage;

import com.gws.common.constants.backstage.ErrorMsg;
import com.gws.configuration.backstage.UidConfig;
import com.gws.controllers.BaseApiController;
import com.gws.controllers.BaseController;
import com.gws.controllers.JsonResult;
import com.gws.dto.backstage.PageDTO;
import com.gws.entity.backstage.BackGoldenCode;
import com.gws.entity.backstage.GoldenWithdrawBO;
import com.gws.enums.SystemCode;
import com.gws.exception.ExceptionUtils;
import com.gws.services.backstage.BackGoldenWithdrawService;
import com.gws.utils.http.LangReadUtil;
import com.gws.utils.validate.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/17.
 */
@RestController
@RequestMapping("/back/api/backstage/goldenWithdraw")
public class BackGoldenWithdrawController extends BaseController{

    private static final Logger LOGGER = LoggerFactory.getLogger(BackGoldenWithdrawController.class);

    private final BackGoldenWithdrawService backGoldenWithdrawService;

    private final HttpServletRequest request;

    @Autowired
    public BackGoldenWithdrawController(BackGoldenWithdrawService backGoldenWithdrawService, HttpServletRequest request) {
        this.backGoldenWithdrawService = backGoldenWithdrawService;
        this.request = request;
    }

    /**
     * 查询黄金提取记录信息
     * {
     *     "page"
     *     "rowNum"
     *     "uid"
     *     "personName"
     *     "status"
     *     "startTime"
     *     "endTime"
     * }
     * @return
     */
    @RequestMapping("/queryGoldenWithdraw")
    public JsonResult queryGoldenWithdraw(@RequestBody GoldenWithdrawBO goldenWithdrawBO){
        /*
         * 1：前台传入page,rowNum，状态，提交日期，uid，手机号，邮箱地址，姓名等参数
         * 2：基本参数校验
         * 3：将满足条件的数据信息，分页展示在页面
         */
        Long uid = UidConfig.getUid();
        LOGGER.info("用户:{},后台用户查看黄金提取记录",uid);
        try {
            Integer endTime = ValidationUtil.checkAndAssignDefaultInt(goldenWithdrawBO.getEndTime(),Integer.MAX_VALUE);
            Integer startTime = ValidationUtil.checkAndAssignDefaultInt(goldenWithdrawBO.getStartTime(),0);
            if(startTime > endTime){
                goldenWithdrawBO.setEndTime(Integer.MAX_VALUE);
            }else{
                goldenWithdrawBO.setEndTime(endTime);
            }
            ValidationUtil.checkMinAndAssignInt(goldenWithdrawBO.getRowNum(),1);
            ValidationUtil.checkMinAndAssignInt(goldenWithdrawBO.getPage(),1);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e);
        }
        try {
            PageDTO pageDTO = backGoldenWithdrawService.queryGoldenWithdrawInfo(goldenWithdrawBO);
            return success(pageDTO);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e);
        }
    }

    /**
     * 录入黄金编号
     * {
     *     "id"
     *     "goldenCodes":[1,2,3,4]
     * }
     * @return
     */
    @RequestMapping("/insertGoldenCode")
    public JsonResult insertGoldenCode(@RequestBody GoldenWithdrawBO goldenWithdrawBO){
        /*
         * 1：前台传入uid,该条提款记录的id，录入的黄金编号 等参数
         * 2：基本参数校验
         * 3：检查uid下是否有这个提款记录的id
         * 4：检查黄金编号是否有重复的
         * 5：检查该提款记录的状态是否符合录入黄金编号的条件
         * 6：将编号插入到该用户的提款记录中，然后将状态改成【待提取中】，还要将提款时间也插入到数据库中，确保到时候取款时间是否已经超时
         */
        Long uid = UidConfig.getUid();
        LOGGER.info("用户:{},录入黄金编号",uid);
        try {
            ValidationUtil.checkAndAssignLong(goldenWithdrawBO.getId());
            List<String> goldenCodes = goldenWithdrawBO.getGoldenCodes();
            if(goldenCodes.size() == 0){
                throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.INPUT_GOLD_CODE));
            }
            for (String goldenCode : goldenCodes) {
                ValidationUtil.checkBlankAndAssignString(goldenCode);
            }
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e);
        }
        try {
            backGoldenWithdrawService.insertGoldenCode(goldenWithdrawBO);
            return success(null);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e);
        }
    }


    /**
     * 根据黄金提取单id查询该单据下的所有黄金编号
     * {
     *     "id"------->黄金提取单的id号
     * }
     * @param goldenWithdrawBO
     * @return
     */
    @RequestMapping("/queryGoldenCode")
    public JsonResult queryGoldenCode(@RequestBody GoldenWithdrawBO goldenWithdrawBO){
        Long uid = UidConfig.getUid();
        LOGGER.info("用户:{},查询黄金编号",uid);
        try {
            ValidationUtil.checkAndAssignLong(goldenWithdrawBO.getId());
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->参数校验失败",uid,e.getMessage());
            return valiError(e);
        }
        try {
            List<BackGoldenCode> backGoldenCodeList = backGoldenWithdrawService.queryGoldenCode(goldenWithdrawBO);
            return success(backGoldenCodeList);
        }catch (Exception e){
            LOGGER.error("用户:{},详情:{}-->操作失败",uid,e.getMessage());
            return sysError(e);
        }
    }

}
