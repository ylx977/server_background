package com.gws.services.backSM.impl;

import com.alibaba.fastjson.JSON;
import com.gws.common.constants.backstage.BackUserStatus;
import com.gws.common.constants.backstage.ErrorMsg;
import com.gws.common.constants.backstage.RedisConfig;
import com.gws.entity.backstage.BackUser;
import com.gws.entity.backstage.BackUserBO;
import com.gws.entity.backstage.SMResult;
import com.gws.exception.ExceptionUtils;
import com.gws.repositories.master.backstage.BackUserMaster;
import com.gws.repositories.query.backstage.BackUsersQuery;
import com.gws.repositories.slave.backstage.BackUserSlave;
import com.gws.services.backSM.BackSMService;
import com.gws.utils.IPUtil;
import com.gws.utils.http.HttpRequest;
import com.gws.utils.http.LangReadUtil;
import com.gws.utils.redis.RedisUtil;
import com.gws.utils.webservice.HashUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ylx
 * Created by fuzamei on 2018/5/15.
 */
@Service
@PropertySource(value = {"classpath:application.properties"},encoding="utf-8")
public class BackSMServiceImpl implements BackSMService{

    private static final Logger LOGGER = LoggerFactory.getLogger(BackSMServiceImpl.class);

    @Autowired
    private BackUserMaster backUserMaster;

    @Autowired
    private BackUserSlave backUserSlave;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private HttpServletRequest request;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBackUserStatus(BackUserBO backUserBO) {
        String username = backUserBO.getUsername();
        String phone = backUserBO.getPhone();
        String code = backUserBO.getCode();
        Integer isFreezed = backUserBO.getIsFreezed();

        //先根据username和phone查看用户是否存在
        BackUsersQuery backUsersQuery = new BackUsersQuery();
        backUsersQuery.setUsername(username);
        backUsersQuery.setContact(phone);
        BackUser one = backUserSlave.findOne(backUsersQuery);
        if(one == null){
            LOGGER.error("用户的用户名和联系方式(username & contact)不能匹配数据库用户信息");
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.USER_AND_CONTACT_NOT_FOUNT));
        }

        //**************************短信验证模块***************************
        //认证前后的ip地址要一致，不然会失败
        String ip = IPUtil.getIpAddr(request);
        String time = String.valueOf(System.currentTimeMillis()/1000);
        //参数的排列顺序是固定的，按照Arrays.sort排列
        String params = "code="+code+"&codetype="+codetype+"&country="+country+"&guide="+guide+"&mobile="+phone+"&t="+t;
        String sign = HashUtil.getStringMD5(appKey + params + appSecret + time).toUpperCase();
        String json = HttpRequest.sendPostSM(shortMessageCode,params,time,ip,sign,appKey);
        SMResult smResult = JSON.parseObject(json, SMResult.class);
        Integer resultCode = smResult.getCode();
        if(!resultCode.equals(200)){
            String message = smResult.getMessage();
            String error = smResult.getError();
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.SM_ERROR)+error+","+message);
        }
        //**************************短信验证模块***************************


        //该用户的uid
        long uid = one.getUid();
        BackUser backUser = new BackUser();
        backUser.setIsFreezed(isFreezed);
        int success = backUserMaster.updateById(backUser, uid, "isFreezed");
        if(success == 0){
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.UPDATE_USER_INFO_FAIL));

        }

        //**************************删除该用户的redis缓存信息***************************
        //删用户的token信息
        redisUtil.delete(RedisConfig.USER_TOKEN_PREFIX + uid);
        //因为修改了密码，必须要将这个缓存删除
        redisUtil.delete(RedisConfig.LOGIN_USERDETAIL_PREFIX + username);
        redisUtil.delete(RedisConfig.USER_AUTH_PREFIX + uid);
    }


}
