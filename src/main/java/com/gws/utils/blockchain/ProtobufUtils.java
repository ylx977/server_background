package com.gws.utils.blockchain;

import com.google.protobuf.ByteString;
import com.gws.entity.request.trade.SignParam;
import com.gws.utils.Base64;
import com.gws.utils.eddsa.EdDSAEngine;
import com.gws.utils.eddsa.EdDSAPrivateKey;
import com.gws.utils.eddsa.spec.EdDSANamedCurveTable;
import com.gws.utils.eddsa.spec.EdDSAParameterSpec;
import com.gws.utils.eddsa.spec.EdDSAPrivateKeySpec;
import msq.Msg;

import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Signature;


/**
 * @author WangBin
 */
public class ProtobufUtils {

    /**
     * @Description
     * @Author WangBin
     * @param operatorPriKey 操作者私钥
     * @param builder
     * @param requestBuilder
     * @param instructionId hash值
     * @return com.gws.utils.blockchain.ProtobufBean
     * @Date 2018/5/4 14:09
     */
    private static ProtobufBean getProtoBufBean(String operatorPriKey, Msg.WriteRequest.Builder builder,
                                                Msg.WriteRequest requestBuilder, long instructionId){

        try {
            EdDSAParameterSpec spec = EdDSANamedCurveTable.getByName("Ed25519");
            byte[] bytePrivateKey = HexUtil.hexString2Bytes(operatorPriKey);
            Signature sgr = new EdDSAEngine(MessageDigest.getInstance(spec.getHashAlgorithm()));
            EdDSAPrivateKeySpec privKey = new EdDSAPrivateKeySpec(bytePrivateKey, spec);
            PrivateKey sKey = new EdDSAPrivateKey(privKey);
            sgr.initSign(sKey);
            sgr.update(requestBuilder.toByteArray());
            byte[] signbyte = sgr.sign();
            ByteString signByteString = ByteString.copyFrom(signbyte, 0, 64);
            builder.setSign(signByteString);
            Msg.WriteRequest signReq = builder.build();
            byte[] requestByte = signReq.toByteArray();
            String sign = Base64.encode(requestByte);
            return new ProtobufBean(instructionId, sign);
        }catch (Exception e){
            throw new RuntimeException();
        }
    }

    /**
     * @Description transfer 签名
     * @Author WangBin
     * @param sign 签名实体类
     * @return com.gws.utils.blockchain.ProtobufBean
     * @Date 2018/5/4 17:30
     */
    public static ProtobufBean RequestTransfer(SignParam sign){

        Msg.RequestTransfer.Builder builder = Msg.RequestTransfer.newBuilder();
        builder.setUid(ByteString.copyFrom(sign.getFromAddress().getBytes(),0,64));
        builder.setToAddr(ByteString.copyFrom(sign.getToAddress().getBytes(),0,64));
        builder.setAmount(sign.getAmount());
        builder.setSymbolId(sign.getSymbolId());
        Msg.WriteRequest.Builder request = Msg.WriteRequest.newBuilder();
        request.setTransfer(builder);
        Msg.WriteRequest requestBuilder = request.build();
        return getProtoBufBean(sign.getOperatorPriKey(), request, requestBuilder, sign.getHash());
    }

    /**
     * @Description transfer接口封装
     * @Author WangBin
     * @param instructionId hash
     * @param symbolId 币种id
     * @param amount 转让额度
     * @param operatorPriKey 操作者私钥
     * @param address 地址   第一个地址  from  第二个 to
     * @return boolean
     * @Date 2018/5/7 10:15
     */
    public static boolean transfer(long instructionId, int symbolId, long amount, String operatorPriKey, String ... address){
        SignParam sign = new SignParam();
        sign.setAmount(amount);
        sign.setOperatorPriKey(operatorPriKey);
        sign.setFromAddress(address[0]);
        sign.setToAddress(address[1]);
        sign.setSymbolId(symbolId);
        sign.setHash(instructionId);
        ProtobufBean protobufBean = RequestTransfer(sign);
        String result = BlockUtils.sendPostParam(protobufBean);
        if(BlockUtils.vilaResult(result)){
            throw new RuntimeException();
        }
        return false;
    }
}