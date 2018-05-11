package com.gws.utils.blockchain;

import com.alibaba.fastjson.JSON;
import com.gws.utils.ReadConfUtil;
import com.gws.utils.http.imp.GwsHttpClientImpl;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author WangBin
 */
public class BlockUtils {

    private final static String url = ReadConfUtil.getProperty("blockUrl");

    public static String sendPostParam(ProtobufBean protobufBean) {
        GwsHttpClientImpl httpRequest = new GwsHttpClientImpl();
        String jsonstr = JSON.toJSONString(new RequestBean(protobufBean.getSignature()));
        try {
            return new String(httpRequest.POST(url, jsonstr), "UTF-8");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    public static boolean vilaResult(String jsonData) {
        boolean flag = false;
        Map<String, Object> map = JSON.parseObject(jsonData, Map.class);    //总的map
        //得到第一个code的值
        String result = map.get("result").toString();    //得到result节点
        Map<String, Object> resultMap = JSON.parseObject(result, Map.class);
        String check_tx = resultMap.get("check_tx").toString();
        Map<String, Object> check_txMap = JSON.parseObject(check_tx, Map.class);
        String code1 = check_txMap.get("code").toString();
        //得到第二个code的值
        String deliver_tx = resultMap.get("deliver_tx").toString();
        Map<String, Object> deliver_txMap = JSON.parseObject(deliver_tx, Map.class);
        String code2 = deliver_txMap.get("code").toString();
        //两个都等于0才等于成功
        if (("0".equals(code1)) && ("0".equals(code2))) {
            flag = true;
        }
        return flag;
    }
}
