package com.gws.controllers.backSM;

import com.alibaba.fastjson.JSON;
import com.gws.common.constants.backstage.ErrorMsg;
import com.gws.common.constants.backstage.RegexConstant;
import com.gws.controllers.BaseController;
import com.gws.controllers.JsonResult;
import com.gws.entity.backstage.BackUser;
import com.gws.entity.backstage.BackUserBO;
import com.gws.entity.backstage.SMResult;
import com.gws.entity.backstage.SMVerifyBO;
import com.gws.exception.ExceptionUtils;
import com.gws.repositories.slave.backstage.BackUserSlave;
import com.gws.services.backSM.BackSMService;
import com.gws.services.backstage.BackUserService;
import com.gws.utils.IPUtil;
import com.gws.utils.http.ConfReadUtil;
import com.gws.utils.http.HTTP;
import com.gws.utils.http.HttpRequest;
import com.gws.utils.validate.ValidationUtil;
import com.gws.utils.webservice.HashUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.applet.Main;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

/**
 * @author ylx
 * Created by fuzamei on 2018/5/11.
 */
@RestController
@RequestMapping("/api/backstageSM")
@PropertySource(value = {"classpath:application.properties"},encoding="utf-8")
public class BackSMcontroller extends BaseController{

    private static final Logger LOGGER = LoggerFactory.getLogger(BackSMcontroller.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private BackSMService backSMService;

    @Autowired
    private BackUserService backUserService;

    @Value("${fzmsm.appKey}")
    private String appKey;
    @Value("${fzmsm.appSecret}")
    private String appSecret;
    @Value("${fzmsm.shortMessageInterface}")
    private String shortMessageInterface;
    @Value("${fzmsm.shortMessageCode}")
    private String shortMessageCode;
    @Value("${fzmsm.country}")
    private String country;
    @Value("${fzmsm.codetype}")
    private String codetype;
    @Value("${fzmsm.param}")
    private String param;
    @Value("${fzmsm.tplid}")
    private String tplid;
    @Value("${fzmsm.t}")
    private String t;
    @Value("${fzmsm.signid}")
    private String signid;
    @Value("${fzmsm.guide}")
    private String guide;

    /**
     * 发送短信验证码
     * 表单形式提交参数phone
     * @param phone
     * @return
     */
    @RequestMapping("/sendVerificationCode")
    public JsonResult sendVerificationCode(@RequestParam(name = "phone") String phone){
        LOGGER.info("匿名用户进行发送短信验证码操作");
        Integer lang =(Integer) request.getAttribute("lang");
        try {
            ValidationUtil.checkBlankAndAssignString(phone,lang, RegexConstant.PHONE_REGEX);
        }catch (Exception e){
            LOGGER.error("匿名用户,详情:{}-->参数校验失败",e.getMessage());
            return valiError(e,lang);
        }
        try {
            System.out.println(appKey);
            //认证前后的ip地址要一致，不然会失败
            String ip = IPUtil.getIpAddr(request);
            String time = String.valueOf(System.currentTimeMillis()/1000);
            //参数的排列顺序是固定的，按照Arrays.sort排列
            String params = "codetype="+codetype+"&country="+country+"&mobile="+phone+"&param="+param+"&signid="+signid+"&tplid="+tplid;
            String sign = HashUtil.getStringMD5(appKey + params + appSecret + time).toUpperCase();
            String json = HttpRequest.sendPostSM(shortMessageInterface,params,time,ip,sign,appKey);
            SMResult smResult = JSON.parseObject(json, SMResult.class);
            Integer code = smResult.getCode();
            if(!code.equals(200)){
                String message = smResult.getMessage();
                String error = smResult.getError();
                throw new RuntimeException(error+","+message);
            }
            return success(null,lang);
        }catch (Exception e){
            LOGGER.error("匿名用户,详情:{}-->操作失败",e.getMessage());
            return sysError(e,lang);
        }
    }

    /**
     * 根据后台username用户名发送短信验证码（给后台快速登录使用）
     * {
     *     "username"
     * }
     * @param backUserBO
     * @return
     */
    @RequestMapping("/sendVerificationCodeByUsername")
    public JsonResult sendVerificationCodeByUsername(@RequestBody BackUserBO backUserBO){
        String username = backUserBO.getUsername();
        LOGGER.info("用户:{},进行发送短信验证码操作",username);
        Integer lang =(Integer) request.getAttribute("lang");
        try {
            ValidationUtil.checkBlankAndAssignString(username,lang, RegexConstant.USERNAME_REGEX);
        }catch (Exception e){
            LOGGER.error("匿名用户,详情:{}-->参数校验失败",e.getMessage());
            return valiError(e,lang);
        }
        try {
            BackUser backUser = backUserService.queryUserByUsername(username);
            if(backUser==null){
                LOGGER.warn("通过用户名发送短信操作，用户名不存在:{}",username);
                return noUserError(lang);
            }

            String phone = backUser.getContact();
            if(phone.matches(RegexConstant.PHONE_REGEX)){
                //认证前后的ip地址要一致，不然会失败
                String ip = IPUtil.getIpAddr(request);
                String time = String.valueOf(System.currentTimeMillis()/1000);
                //参数的排列顺序是固定的，按照Arrays.sort排列
                String params = "codetype="+codetype+"&country="+country+"&mobile="+phone+"&param="+param+"&signid="+signid+"&tplid="+tplid;
                String sign = HashUtil.getStringMD5(appKey + params + appSecret + time).toUpperCase();
                String json = HttpRequest.sendPostSM(shortMessageInterface,params,time,ip,sign,appKey);
                SMResult smResult = JSON.parseObject(json, SMResult.class);
                Integer code = smResult.getCode();
                if(!code.equals(200)){
                    String message = smResult.getMessage();
                    String error = smResult.getError();
                    ExceptionUtils.throwException(ErrorMsg.SM_ERROR,lang,error+","+message);
                }
            }else{
                //如果不是正常手机号，不给发送
                ExceptionUtils.throwException(ErrorMsg.WRONG_CONTACT,lang);
            }
            return success(null,lang);
        }catch (Exception e){
            LOGGER.error("匿名用户,详情:{}-->操作失败",e.getMessage());
            return sysError(e,lang);
        }
    }


    /**
     * 通过短信验证码验证的方式冻结或解冻后台用户
     * {
     *     "username"
     *     "code"
     *     "phone"
     *     "isFreezed"
     * }
     * @param backUserBO
     * @return
     */
    @RequestMapping("/updateBackUserStatus")
    public JsonResult updateBackUserStatus(@RequestBody BackUserBO backUserBO){
        Integer lang =(Integer) request.getAttribute("lang");
        LOGGER.info("匿名用户通过验证短信验证码的方式改变用户的");
        String username = backUserBO.getUsername();
        String phone = backUserBO.getPhone();
        String code = backUserBO.getCode();
        Integer isFreezed = backUserBO.getIsFreezed();
        try {
            ValidationUtil.checkBlankAndAssignString(username,lang);
            ValidationUtil.checkBlankAndAssignString(phone,lang, RegexConstant.PHONE_REGEX);
            ValidationUtil.checkBlankAndAssignString(code,lang);
            ValidationUtil.checkRangeAndAssignInt(isFreezed,0,1,lang);
        }catch (Exception e){
            LOGGER.error("匿名用户,详情:{}-->参数校验失败",e.getMessage());
            return valiError(e,lang);
        }
        try {
            backUserBO.setLang(lang);
            backUserBO.setRequest(request);
            backSMService.updateBackUserStatus(backUserBO);
            return success(null,lang);
        }catch (Exception e){
            LOGGER.error("匿名用户,详情:{}-->操作失败",e.getMessage());
            return sysError(e,lang);
        }
    }

//    /**
//     * 发送验证码和手机号，用于验证短信验证码是否有效
//     * @param smVerifyBO
//     * @return
//     */
//    @RequestMapping("/verifyCode")
//    public JsonResult verifyCode(@RequestBody SMVerifyBO smVerifyBO){
//        LOGGER.info("匿名用户进行发送短信验证码操作");
//        Integer lang =(Integer) request.getAttribute("lang");
//        String phone = smVerifyBO.getPhone();
//        String code = smVerifyBO.getCode();
//        Integer guide = smVerifyBO.getGuide();
//        try {
//            ValidationUtil.checkBlankAndAssignString(phone,lang, RegexConstant.PHONE_REGEX);
//            ValidationUtil.checkBlankAndAssignString(code,lang);
//            ValidationUtil.checkRangeAndAssignInt(guide,0,1,lang);
//        }catch (Exception e){
//            LOGGER.error("匿名用户,详情:{}-->参数校验失败",e.getMessage());
//            return valiError(e,lang);
//        }
//        try {
//            //认证前后的ip地址要一致，不然会失败
//            String ip = IPUtil.getIpAddr(request);
//            String time = String.valueOf(System.currentTimeMillis()/1000);
//            //参数的排列顺序是固定的，按照Arrays.sort排列
//            String params = "code="+code+"&codetype="+codetype+"&country="+country+"&guide="+guide+"&mobile="+phone+"&t="+t;
//            String sign = HashUtil.getStringMD5(appKey + params + appSecret + time).toUpperCase();
//            String json = HttpRequest.sendPostSM(shortMessageCode,params,time,ip,sign,appKey);
//            SMResult smResult = JSON.parseObject(json, SMResult.class);
//            Integer resultCode = smResult.getCode();
//            if(!resultCode.equals(200)){
//                String message = smResult.getMessage();
//                String error = smResult.getError();
//                throw new RuntimeException(error+","+message);
//            }
//            return success(null,lang);
//        }catch (Exception e){
//            LOGGER.error("匿名用户,详情:{}-->操作失败",e.getMessage());
//            return sysError(e,lang);
//        }
//    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String appKey = "supplychain";
        String appSecret = "Lm46G7w4DF0HnbOaxQ";
        String shortMessageInterface="https://sms.33.cn/send/sms";
        String shortMessageCode=ConfReadUtil.getProperty("fzmsm.shortMessageCode");
        String country="CN";
        String codetype="1";
        String param=URLEncoder.encode("1|1","utf-8");
        String tplid="20";
        String t=ConfReadUtil.getProperty("fzmsm.t");
        String signid="0";
        String guide=ConfReadUtil.getProperty("fzmsm.guide");

        String phone ="18757198903";
        String ip = "1";
        String time = String.valueOf(System.currentTimeMillis()/1000);
        String params = "codetype="+codetype+"&country="+country+"&mobile="+phone+"&param="+param+"&signid="+signid+"&tplid="+tplid;
        System.out.println(params);
        String sign = HashUtil.getStringMD5(appKey + params + appSecret + time).toUpperCase();
        String json = HttpRequest.sendPostSM(shortMessageInterface,params,time,ip,sign,appKey);
        SMResult smResult = JSON.parseObject(json, SMResult.class);
        System.out.println(json);
        Integer code = smResult.getCode();
        if(!code.equals(200)){
            String message = smResult.getMessage();
            String error = smResult.getError();
            throw new RuntimeException(error+","+message);
        }



//        String ip = "12";
//        String code = "4500";
//        String phone ="18757198903";
//        String time = String.valueOf(System.currentTimeMillis()/1000);
//        //参数的排列顺序是固定的，按照Arrays.sort排列
//        String params = "code="+code+"&codetype="+codetype+"&country="+country+"&guide="+guide+"&mobile="+phone+"&t="+t;
//        System.out.println(params);
//        String sign = HashUtil.getStringMD5(appKey + params + appSecret + time).toUpperCase();
//        String json = HttpRequest.sendPostSM(shortMessageCode,params,time,ip,sign,appKey);
////        SMResult smResult = JSON.parseObject(json, SMResult.class);
////        Integer resultCode = smResult.getCode();
//        System.out.println(json);
////        if(!resultCode.equals(200)){
////            String message = smResult.getMessage();
////            String error = smResult.getError();
////            throw new RuntimeException(error+","+message);
//        }




    }

}
