package com.gws.controllers.backSM;

import com.gws.utils.IPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author ylx
 * Created by fuzamei on 2018/5/14.
 */
public class SendSMThread implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendSMThread.class);

    public SendSMThread(String phone,String param,String ip){
        this.phone = phone;
        this.param = param;
        this.ip = ip;
    }

    private String phone;

    private String param;

    private String ip;

    @Override
    public void run() {
        try {
            param = URLEncoder.encode(param,"utf-8");
            SMUtil.sendSMMessage(phone,param,ip);
        } catch (Exception e) {
            LOGGER.warn("发送短信操作失败: {}",e.getMessage());
            return;
        }
    }
}
