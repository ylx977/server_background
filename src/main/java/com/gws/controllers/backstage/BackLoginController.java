package com.gws.controllers.backstage;

import com.alibaba.fastjson.JSON;
import com.gws.common.constants.backstage.RedisConfig;
import com.gws.common.constants.backstage.RegexConstant;
import com.gws.controllers.BaseController;
import com.gws.controllers.JsonResult;
import com.gws.dto.backstage.UserDetailDTO;
import com.gws.entity.backstage.BackUser;
import com.gws.entity.backstage.BackUserBO;
import com.gws.enums.SystemCode;
import com.gws.services.backstage.BackUserService;
import com.gws.utils.redis.RedisUtil;
import com.gws.utils.validate.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/18.
 */
@RestController
@RequestMapping("/api/backstage")
public class BackLoginController extends BaseController{

    @Autowired
    private BackUserService backUserService;

    @Autowired
    private RedisUtil redisUtil;

    private static final Logger LOGGER = LoggerFactory.getLogger(BackLoginController.class);

    /**
     * 用户登录操作
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
            ValidationUtil.checkBlankAndAssignString(username, RegexConstant.USERNAME_REGEX);
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
            }else{
                //如果redis没有，就从数据库获取用户详细信息
                LOGGER.info("用户:{},从MySQL获取了登录详细信息",username);
                userDetailDTO = backUserService.queryUserByNameAndPwd(username,password);
                //用户名密码错误
                if(userDetailDTO==null){
                    LOGGER.error("用户名:{} 输入错误的账户名或密码",username);
                    return loginFail();
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
            userDetailDTO.setTokenId("Bearer"+token+"&"+uid);
            userDetailDTO.setIfFirstLogin(ifFirstLogin);

            //将token信息更新到redis
            redisUtil.set(RedisConfig.USER_TOKEN_PREFIX+uid, token,RedisConfig.USER_TOKEN_TIMEOUT, TimeUnit.MILLISECONDS);

            return loginSuccess(userDetailDTO);
        }catch (Exception e){
            LOGGER.error("用户名:{},登录异常详情:{}",username,e.getMessage());
            return loginError(e);
        }
    }

}
