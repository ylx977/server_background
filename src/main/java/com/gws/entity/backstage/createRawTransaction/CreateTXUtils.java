package com.gws.entity.backstage.createRawTransaction;

import com.alibaba.fastjson.JSON;
import com.gws.utils.HexUtil;
import com.gws.utils.blockchain.RequestBean;
import com.gws.utils.eddsa.EdDSAEngine;
import com.gws.utils.eddsa.EdDSAPrivateKey;
import com.gws.utils.eddsa.spec.EdDSANamedCurveSpec;
import com.gws.utils.eddsa.spec.EdDSANamedCurveTable;
import com.gws.utils.eddsa.spec.EdDSAPrivateKeySpec;
import com.gws.utils.http.ConfReadUtil;
import com.gws.utils.http.HttpRequest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.PrivateKey;

/**
 * @author ylx
 * 与滨江进行交互的工具类
 * Created by fuzamei on 2018/5/14.
 */
@Data
@Component
public class CreateTXUtils {

    private static String outerurl = ConfReadUtil.getProperty("blockchain.outerurl");

    /**
     * 交易的时候向滨江的区块链地址发送生成交易的
     * @param toAddress
     * @param amount
     * @param fee
     * @param note
     * @param isToken 如果是BTY的话，传false即可，后面的tokenSymbol可传可不传，但如果不是BTY就需要填写true，后面的tokenSymbol要写相应币种的大写形式
     * @param tokenSymbol
     * @return
     */
    public static final String createRawTransaction(String toAddress,Long amount,Long fee,String note,Boolean isToken,String tokenSymbol){
        RawTX rawTX = new RawTX();
        rawTX.setTo(toAddress);
        rawTX.setAmount(amount);
        rawTX.setFee(fee);
        rawTX.setNote(note);
        rawTX.setIsToken(isToken);
        rawTX.setTokenSymbol(tokenSymbol);
        RequestBean requestBean = new RequestBean(rawTX,"Chain33.CreateRawTransaction",1);//这里的id是写死的，都是1
        String requestBeanJson = JSON.toJSONString(requestBean);
        String result = HttpRequest.sendPost(outerurl,requestBeanJson);
        return result;
    }


    /**
     * 根据上面这个生成交易的方法返回的参数进行签名等操作后，将相关参数再发送到该方法中去，就可以了
     * @param unsignTx
     * @param sign
     * @param pubkey
     * @param ty
     * @return
     */
    public static final String SendRawTransaction(String unsignTx,String sign,String pubkey,Integer ty){
        SendTX sendTX = new SendTX();
        sendTX.setUnsignTx(unsignTx);
        sendTX.setSign(sign);
        sendTX.setPubkey(pubkey);
        sendTX.setTy(ty);
        RequestBean requestBean = new RequestBean(sendTX,"Chain33.SendRawTransaction",1);//这里的id是写死的，都是1
        String requestBeanJson = JSON.toJSONString(requestBean);
        String result = HttpRequest.sendPost(outerurl, requestBeanJson);
        return result;
    }



    public static final String getSign(String unsigntx,String operatorPriKey){
        try {
            byte[] sourceDataBytes = HexUtil.hexString2Bytes(unsigntx);
            //todo 对元数据进行进行加密
            EdDSANamedCurveSpec spec = EdDSANamedCurveTable.getByName("Ed25519");
            //就是SHA-512
            EdDSAEngine edDSAEngine = new EdDSAEngine(MessageDigest.getInstance(spec.getHashAlgorithm()));
            //私钥转成byte数组
            byte[] bytePrivateKey = HexUtil.hexString2Bytes(operatorPriKey);

            EdDSAPrivateKeySpec privKey = new EdDSAPrivateKeySpec(bytePrivateKey, spec);
            PrivateKey privateKey = new EdDSAPrivateKey(privKey);
            edDSAEngine.initSign(privateKey);
            edDSAEngine.update(sourceDataBytes);

            //元数据经过私钥加密过后的byte数组类型的签名数据
            byte[] sign = edDSAEngine.sign();

            String hexSign = HexUtil.bytes2HexString(sign);
            return hexSign;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
