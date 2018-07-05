package com.gws.controllers.backSM;

import com.alibaba.fastjson.JSON;
import com.gws.common.constants.backstage.ErrorMsg;
import com.gws.entity.backstage.SMResult;
import com.gws.exception.ExceptionUtils;
import com.gws.utils.IPUtil;
import com.gws.utils.http.ConfReadUtil;
import com.gws.utils.http.HttpRequest;
import com.gws.utils.http.LangReadUtil;
import com.gws.utils.webservice.HashUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author ylx
 * Created by fuzamei on 2018/5/14.
 */
@Component
public class SMUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(SMUtil.class);

    private static final String APP_KEY = ConfReadUtil.getProperty("fzmsm.appKey");
    private static final String APP_SECRET = ConfReadUtil.getProperty("fzmsm.appSecret");
    private static final String SM_INTERFACE = ConfReadUtil.getProperty("fzmsm.shortMessageInterface");
    private static final String SM_VALIDATE_INTERFACE = ConfReadUtil.getProperty("fzmsm.shortMessageCode");
    private static final String COUNTRY = ConfReadUtil.getProperty("fzmsm.country");
    //只是针对短信验证码的一个参数
    private static final String PARAM = ConfReadUtil.getProperty("fzmsm.param");
    private static final String CODE_TYPE = ConfReadUtil.getProperty("fzmsm.codetype");
    private static final String TPLID = ConfReadUtil.getProperty("fzmsm.tplid");
    private static final String TPLID2 = ConfReadUtil.getProperty("fzmsm.tplid2");
    private static final String T = ConfReadUtil.getProperty("fzmsm.t");
    private static final String SIGN_ID = ConfReadUtil.getProperty("fzmsm.signid");
    private static final String GUIDE = ConfReadUtil.getProperty("fzmsm.guide");


//    private static final String APP_KEY = "supplychain";
//    private static final String APP_SECRET = "Lm46G7w4DF0HnbOaxQ";
//    private static final String SM_INTERFACE = "https://sms.33.cn/send/sms";
//    private static final String SM_VALIDATE_INTERFACE = "https://sms.33.cn/validate/code";
//    private static final String COUNTRY = "CN";
//    //只是针对短信验证码的一个参数
//    private static final String PARAM = "FzmRandom4";
//    private static final String CODE_TYPE = "1";
//    private static final String TPLID = "14";
//    private static final String TPLID2 = "20";
//    private static final String T = "sms";
//    private static final String SIGN_ID = "0";
//    private static final String GUIDE = "0";

    /**
     * 发送短信验证码方法
     * @param phone
     */
    public static final void sendSMCode(String phone,String ip){
        String time = String.valueOf(System.currentTimeMillis()/1000);
        String params = "codetype="+CODE_TYPE+"&country="+COUNTRY+"&mobile="+phone+"&param="+PARAM+"&signid="+SIGN_ID+"&tplid="+TPLID;
        String sign = HashUtil.getStringMD5(APP_KEY + params + APP_SECRET + time).toUpperCase();
        String json = HttpRequest.sendPostSM(SM_INTERFACE,params,time,ip,sign,APP_KEY);
        SMResult smResult = JSON.parseObject(json, SMResult.class);
        Integer code = smResult.getCode();
        if(!code.equals(200)){
            String message = smResult.getMessage();
            String error = smResult.getError();
            LOGGER.error("短信发送失败: {}",message+","+error);
        }
    }

    /**
     * 发送短信内容的方法
     * @param phone
     * @param param 这是具体的内容，格式是url编码后的（username|ip）
     */
    public static final void sendSMMessage(String phone,String param,String ip){
        String time = String.valueOf(System.currentTimeMillis()/1000);
        String params = "codetype="+CODE_TYPE+"&country="+COUNTRY+"&mobile="+phone+"&param="+param+"&signid="+SIGN_ID+"&tplid="+TPLID2;
        String sign = HashUtil.getStringMD5(APP_KEY + params + APP_SECRET + time).toUpperCase();
        String json = HttpRequest.sendPostSM(SM_INTERFACE,params,time,ip,sign,APP_KEY);
        SMResult smResult = JSON.parseObject(json, SMResult.class);
        Integer code = smResult.getCode();
        if(!code.equals(200)){
            String message = smResult.getMessage();
            String error = smResult.getError();
            LOGGER.error("短信发送失败: {}",message+","+error);
        }
    }


    /**
     * 验证短信验证码是否正确的接口
     * @param code 验证码
     *
     * 返回true表示验证成功，返回false表示验证失败
     */
    public static final boolean valiSMCode(String code, String phone,String ip){
        String time = String.valueOf(System.currentTimeMillis()/1000);
        //参数的排列顺序是固定的，按照Arrays.sort排列
        String params = "code="+code+"&codetype="+CODE_TYPE+"&country="+COUNTRY+"&guide="+GUIDE+"&mobile="+phone+"&t="+T;
        String sign = HashUtil.getStringMD5(APP_KEY + params + APP_SECRET + time).toUpperCase();
        String json = HttpRequest.sendPostSM(SM_VALIDATE_INTERFACE,params,time,ip,sign,APP_KEY);
        SMResult smResult = JSON.parseObject(json, SMResult.class);
        Integer resultCode = smResult.getCode();
        if(!resultCode.equals(200)){
            String message = smResult.getMessage();
            String error = smResult.getError();
            LOGGER.error("短信验证失败: {}",message+","+error);
            throw new RuntimeException(LangReadUtil.getProperty(ErrorMsg.SM_ERROR)+error+","+message);
        }
        return true;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
//        sendSMMessage("18757198903",URLEncoder.encode("杨凌霄|120.120.120","utf-8"),"120.120.120.120");
//        sendSMMessage("13588039975","admin|120.120.120","120.120.120.120");
//        sendSMCode("18757198903","120.120.120.120");
//        boolean b = valiSMCode("7448", "18757198903", "120.120.120.120");
//        if(b){
//            System.out.println("短信验证成功");
//        }else{
//            System.out.println("短信验证失败");
//        }
    }

}
