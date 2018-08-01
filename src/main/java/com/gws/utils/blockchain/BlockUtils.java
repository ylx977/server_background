package com.gws.utils.blockchain;

import com.alibaba.fastjson.JSON;
import com.gws.common.constants.backstage.SymbolId;
import com.gws.utils.HexStringUtil;
import com.gws.utils.ReadConfUtil;
import com.gws.utils.http.ConfReadUtil;
import com.gws.utils.http.HttpRequest;
import com.gws.utils.http.imp.GwsHttpClientImpl;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author WangBin
 */
public class BlockUtils {

    /**
     * usdg交易所内部的区块链接口
     */
    private final static String url = ConfReadUtil.getProperty("blockchain.usdgurl");

    public static String sendPostParam(ProtobufBean protobufBean) {
        String jsonstr = JSON.toJSONString(new RequestBean(protobufBean.getSignature(),"broadcast_tx_commit",null));
        System.out.println(jsonstr);
        String jsonResult = HttpRequest.sendPost(url, jsonstr);
        return jsonResult;
    }

    public static void main(String[] args) {
        ProtobufBean protobufBean = Protobuf4EdsaUtils.requestTransfer("017615faee7ade72ecc9838f06e954ab4581802c7be22668c90508c8a9bee1e5",
                SymbolId.BTY, "9c885ce7664ed3c3675df8e49590c141665d94add5bf72a816a8d8a78bd8fbe5",
                "297b9985d533c14a801b016d2065dbe21c70b1c5e396e52ec886b41e94b14fa5", 1000000000000L);
        String s = sendPostParam(protobufBean);
        System.out.println(s);
        if(!vilaResult(s)){
            throw new RuntimeException(getErrorMessage(s));
        }
    }

    /**
     * 验证区块链返回的结果是否正确
     * @param jsonData
     * @return
     */
    public static boolean vilaResult(String jsonData) {
        Map<String,Object> map = JSON.parseObject(jsonData, Map.class);	//总的map

        //先看error里面有没有数据，如果有直接返回false
        String error = map.get("error").toString();
        if(!StringUtils.isEmpty(error)){
            return false;
        }

        //得到第一个code的值
        String result = map.get("result").toString();	//得到result节点
        Map<String,Object> resultMap = JSON.parseObject(result, Map.class);
        String check_tx = resultMap.get("check_tx").toString();
        Map<String,Object> check_txMap = JSON.parseObject(check_tx, Map.class);
        String code1 = check_txMap.get("code").toString();
        //得到第二个code的值
        String deliver_tx = resultMap.get("deliver_tx").toString();
        Map<String,Object> deliver_txMap = JSON.parseObject(deliver_tx, Map.class);
        String code2 = deliver_txMap.get("code").toString();
        if(("0".equals(code1))&&("0".equals(code2))){	//两个都等于0才等于成功
            return true;
        }
        return false;
    }


    /**
     * 如果验证错误，返回区块链错误的信息
     * @param jsonData
     * @return
     */
    public static String getErrorMessage(String jsonData){
        Map<String,Object> map = JSON.parseObject(jsonData,Map.class);	//总的map
        String error = map.get("error").toString();
        if(!StringUtils.isEmpty(error)){
            return error+"(blockchain request error)";
        }
        //得到第一个code的值(如果第一个有值就取第一个，如果第一个没有值取第二个)
        String result = map.get("result").toString();	//得到result节点
        Map<String,Object> resultMap = JSON.parseObject(result,Map.class);
        String check_tx = resultMap.get("check_tx").toString();
        Map<String,Object> check_txMap = JSON.parseObject(check_tx,Map.class);
        String data = check_txMap.get("data").toString();
        if("".equals(data.trim())){
            String deliver_tx = resultMap.get("deliver_tx").toString();
            Map<String,Object> deliver_txMap = JSON.parseObject(deliver_tx,Map.class);
            String data2 = deliver_txMap.get("data").toString();
            return HexStringUtil.hexStringToString(data2)+"(blockchain error)";
        }
        return HexStringUtil.hexStringToString(data)+"(blockchain error)";
    }

}
