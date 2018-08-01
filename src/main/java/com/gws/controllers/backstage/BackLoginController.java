package com.gws.controllers.backstage;

import com.alibaba.fastjson.JSON;
import com.gws.common.constants.backstage.BackUserStatus;
import com.gws.common.constants.backstage.ErrorMsg;
import com.gws.common.constants.backstage.RedisConfig;
import com.gws.common.constants.backstage.RegexConstant;
import com.gws.controllers.BaseController;
import com.gws.controllers.JsonResult;
import com.gws.controllers.backSM.SMUtil;
import com.gws.controllers.backSM.SendSMThread;
import com.gws.dto.backstage.UserDetailDTO;
import com.gws.entity.backstage.BackUser;
import com.gws.entity.backstage.BackUserBO;
import com.gws.entity.backstage.SMResult;
import com.gws.enums.SystemCode;
import com.gws.exception.ExceptionUtils;
import com.gws.services.backstage.BackUserService;
import com.gws.utils.IPUtil;
import com.gws.utils.http.ConfReadUtil;
import com.gws.utils.http.HttpRequest;
import com.gws.utils.http.LangReadUtil;
import com.gws.utils.redis.RedisUtil;
import com.gws.utils.token.TokenUtil;
import com.gws.utils.validate.ValidationUtil;
import com.gws.utils.webservice.HashUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/18.
 */
@RestController
@RequestMapping("/back/api/backstage")
public class BackLoginController extends BaseController{

    @Autowired
    private BackUserService backUserService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ExecutorService executorService;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(BackLoginController.class);

//    private static final String STEVEN_PHONE = ConfReadUtil.getProperty("backuser.steven.phonenumber");

    @Value("${backuser.steven.phonenumber}")
    private String STEVEN_PHONE;

    /**
     * 用户登录操作
     * {
     *     "username"
     *     "password"
     * }
     * @param backUserBO 登录用户的用户名和密码
     * @return
     */
    @RequestMapping("/login")
    public JsonResult login(@RequestBody BackUserBO backUserBO){
        String username = backUserBO.getUsername();
        String password = backUserBO.getPassword();
        LOGGER.info("用户名:{} 进行了登录",username);
        try {
            //先进行用户名和密码的校验
            ValidationUtil.checkBlankAndAssignString(username,RegexConstant.USERNAME_REGEX);
            ValidationUtil.checkBlankAndAssignString(password,RegexConstant.PWD_REGEX);
        } catch (Exception e) {
            LOGGER.error("用户名:{},登录校验异常:{}",username,e.getMessage());
            return valiError(e);
        }

        //redis中key的值
        String userDetailDTOString = (String) redisUtil.get(RedisConfig.LOGIN_USERDETAIL_PREFIX + username);

        //每次登录生成全新token
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        try {
            BackUser admin = backUserService.queryAdmin();
            if(admin==null){
                LOGGER.warn("平台初始化，创建超级管理员:{}",username);
                //创建超级管理员,和管理员,插入token,上区块链
                UserDetailDTO userDetailDTO = backUserService.createAdmin(username,password,token);
                return loginSuccess(userDetailDTO);
            }

            UserDetailDTO userDetailDTO;
            if(userDetailDTOString != null){
                //如果redis里面有，就从redis里面获取
                LOGGER.info("用户:{},从redis获取了登录详细信息",username);
                userDetailDTO = JSON.parseObject(userDetailDTOString, UserDetailDTO.class);
                //缓存中的密码需要比对
                if(!userDetailDTO.getPassword().equals(HashUtil.hashPwd(password))){
                    LOGGER.error("用户名:{} 输入错误的账户名或密码",username);
                    return loginFail();
                }
            }else{
                //如果redis没有，就从数据库获取用户详细信息
                LOGGER.info("用户:{},从MySQL获取了登录详细信息",username);
                userDetailDTO = backUserService.queryUserByNameAndPwd(username,password);
                //用户名密码错误
                if(userDetailDTO==null){
                    LOGGER.error("用户名:{} 输入错误的账户名或密码",username);
                    return loginFail();
                }
                if(userDetailDTO.getIsFreezed().equals(BackUserStatus.FREEZED)){
                    LOGGER.error("用户名:{} 该用户已被冻结",username);
                    return freezeFail();
                }
                //只是将用户名用户密码等详细信息先存入redis
                redisUtil.set(RedisConfig.LOGIN_USERDETAIL_PREFIX + username, JSON.toJSONString(userDetailDTO),RedisConfig.LOGIN_USERDETAIL_TIMEOUT, TimeUnit.MILLISECONDS);
            }

            //用来判定是否是初次登录的标记
            boolean ifFirstLogin = false;
            Long uid = userDetailDTO.getUid();
            if(userDetailDTO.getToken()==null||"".equals(userDetailDTO.getToken().trim())){
                //初次登录
                LOGGER.warn("用户名:{} 初次登录，进行token信息的插入",username);
                backUserService.addToken(uid, token);
                ifFirstLogin = true;
            } else {
                //再次登录
                LOGGER.info("用户名:{} 登录，对token信息进行更新",username);
                backUserService.updateToken(uid,token);
            }
            userDetailDTO.setTokenId(TokenUtil.packTokne(token,uid));
            userDetailDTO.setIfFirstLogin(ifFirstLogin);

            //将token信息更新到redis,并设置超时时间USER_TOKEN_TIMEOUT
            redisUtil.set(RedisConfig.USER_TOKEN_PREFIX+uid, token,RedisConfig.USER_TOKEN_TIMEOUT, TimeUnit.MILLISECONDS);

            //*******************每次用户登陆需要向用户发送短信通知********************
            //暂时认为contact为手机号码
            String contact = userDetailDTO.getContact();
            if(contact.matches(RegexConstant.PHONE_REGEX)){
                //手机类型匹配，再发送短信
                String ip = IPUtil.getIpAddr(request);
                String param = username+"|"+ip;
                //发送短信通知当前用户的手机号
                executorService.execute(new SendSMThread(contact,param,ip));
            }

            return loginSuccess(userDetailDTO);
        }catch (Exception e){
            LOGGER.error("用户名:{},登录异常详情:{}",username,e.getMessage());
            return loginError(e);
        }
    }



    /**
     * 用户快速登录操作(不支持初始化平台时候的登陆)
     * {
     *     "username"
     *     "code"
     * }
     * @param backUserBO
     * @return
     */
    @RequestMapping("/fastLogin")
    public JsonResult fastLogin(@RequestBody BackUserBO backUserBO){
        String username = backUserBO.getUsername();
        String code = backUserBO.getCode();
        LOGGER.info("用户名:{} 进行了短信验证快速登录",username);
        try {
            //先进行用户名和验证码进行校验
            ValidationUtil.checkBlankAndAssignString(username,RegexConstant.USERNAME_REGEX);
            ValidationUtil.checkBlankAndAssignString(code,RegexConstant.CODE_REGEX);
        } catch (Exception e) {
            LOGGER.error("用户名:{},登录校验异常:{}",username,e.getMessage());
            return valiError(e);
        }

        //每次登录生成全新token
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        try {
            BackUser user = backUserService.queryUserByUsername(username);
            if(user==null){
                LOGGER.warn("快捷登陆，用户名不存在:{}",username);
                return noUserError();
            }
            //通过用户名获取contact中的手机号
            String phone = user.getContact();
            if(phone.matches(RegexConstant.PHONE_REGEX)){
                String ip = IPUtil.getIpAddr(request);
                SMUtil.valiSMCode(code, phone, ip);
//                //**************************短信验证模块***************************
//                //认证前后的ip地址要一致，不然会失败
//                String ip = IPUtil.getIpAddr(request);
//                String time = String.valueOf(System.currentTimeMillis()/1000);
//                //参数的排列顺序是固定的，按照Arrays.sort排列
//                String params = "code="+code+"&codetype="+codetype+"&country="+country+"&guide="+guide+"&mobile="+phone+"&t="+t;
//                String sign = HashUtil.getStringMD5(appKey + params + appSecret + time).toUpperCase();
//                String json = HttpRequest.sendPostSM(shortMessageCode,params,time,ip,sign,appKey);
//                SMResult smResult = JSON.parseObject(json, SMResult.class);
//                Integer resultCode = smResult.getCode();
//                if(!resultCode.equals(200)){
//                    String message = smResult.getMessage();
//                    String error = smResult.getError();
//                    ExceptionUtils.throwException(ErrorMsg.SM_ERROR,lang,error+","+message);
//                }
//                //**************************短信验证模块***************************
            }else{
                throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.WRONG_CONTACT));
            }

            LOGGER.info("用户:{},从MySQL获取了登录详细信息",username);
            String password = user.getPassword();
            UserDetailDTO userDetailDTO = backUserService.queryUserByNameAndPwd(username,password);

            //用来判定是否是初次登录的标记
            boolean ifFirstLogin = false;
            Long uid = userDetailDTO.getUid();
            if(userDetailDTO.getToken()==null || "".equals(userDetailDTO.getToken().trim())){
                //判断是否是初次登录
                LOGGER.warn("用户名:{} 初次快速登录，进行token信息的插入",username);
                backUserService.addToken(uid, token);
                ifFirstLogin = true;
            } else {
                //再次登录
                LOGGER.info("用户名:{} 登录，对token信息进行更新",username);
                backUserService.updateToken(uid,token);
            }
            userDetailDTO.setTokenId(TokenUtil.packTokne(token,uid));
            userDetailDTO.setIfFirstLogin(ifFirstLogin);

            //将token信息更新到redis,并设置超时时间USER_TOKEN_TIMEOUT
            redisUtil.set(RedisConfig.USER_TOKEN_PREFIX + uid, token,RedisConfig.USER_TOKEN_TIMEOUT, TimeUnit.MILLISECONDS);

            return loginSuccess(userDetailDTO);
        }catch (Exception e){
            LOGGER.error("用户名:{},登录异常详情:{}",username,e.getMessage());
            return loginError(e);
        }
    }

}
