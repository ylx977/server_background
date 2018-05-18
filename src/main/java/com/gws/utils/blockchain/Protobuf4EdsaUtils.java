package com.gws.utils.blockchain;

import com.google.protobuf.ByteString;
import com.gws.common.constants.backstage.SymbolId;
import com.gws.utils.ReadConfUtil;
import com.gws.utils.eddsa.EdDSAEngine;
import com.gws.utils.eddsa.EdDSAPrivateKey;
import com.gws.utils.eddsa.spec.EdDSANamedCurveSpec;
import com.gws.utils.eddsa.spec.EdDSANamedCurveTable;
import com.gws.utils.eddsa.spec.EdDSAParameterSpec;
import com.gws.utils.eddsa.spec.EdDSAPrivateKeySpec;
import com.gws.utils.http.ConfReadUtil;
import msq.Msg;

import java.security.*;
import java.util.Base64;

/**
 * @author ylx
 * Created by fuzamei on 2018/5/10.
 */
public class Protobuf4EdsaUtils {

    private Protobuf4EdsaUtils(){
        throw new AssertionError("instantiation is not permitted");
    }


    /**
     *
     * @param privateKey
     * @param symbolId
     * @param fromAdd---------->合约上的uid是打币者的公钥(不是地址)
     * @param toAdd------------>合约上的toAddr是被打币者的公钥(也不是地址)
     * @param amount
     * @return
     */
    public static final ProtobufBean requestTransfer(String privateKey, int symbolId, String fromAdd,String toAdd,long amount){
        //随机数，生成一个操作hash
        SecureRandom secureRandom = new SecureRandom();
        //传说中的操作hash值
        long instructionId = secureRandom.nextLong();

        Msg.WriteRequest.Builder writeRequest = Msg.WriteRequest.newBuilder();
        Msg.RequestTransfer.Builder transfer = Msg.RequestTransfer.newBuilder();
        transfer.setInstructionId(instructionId);
        transfer.setUid(ByteString.copyFrom(fromAdd.getBytes()));
        transfer.setToAddr(ByteString.copyFrom(toAdd.getBytes()));
        transfer.setAmount(amount);
        transfer.setSymbolId(symbolId);
        transfer.setActionId(Msg.MessageType.MsgTransfer);

        // Msg.WriteRequest.Builder的builder对象
        writeRequest.setTransfer(transfer);
        writeRequest.setId(0L);
//        writeRequest.setCreateTime();
        writeRequest.setSign(getSign(writeRequest,privateKey));

        //最终的请求数据
        Msg.WriteRequest finalRequest = writeRequest.build();
        byte[] finalBytes = finalRequest.toByteArray();
        String sign = Base64.getEncoder().encodeToString(finalBytes);
        return new ProtobufBean(instructionId,sign);
    }


    private static final ByteString getSign(Msg.WriteRequest.Builder writeRequest, String operatorKey) {
        try {
            Msg.WriteRequest writeRequestBuild = writeRequest.build();
            byte[] sourceDataBytes = writeRequestBuild.toByteArray();
            //todo 对元数据进行进行加密
            EdDSANamedCurveSpec spec = EdDSANamedCurveTable.getByName("Ed25519");
            //就是SHA-512
            System.out.println(spec.getHashAlgorithm());
            EdDSAEngine edDSAEngine = new EdDSAEngine(MessageDigest.getInstance(spec.getHashAlgorithm()));
            //私钥转成byte数组
            byte[] bytePrivateKey = HexUtil.hexString2Bytes(operatorKey);

            EdDSAPrivateKeySpec privKey = new EdDSAPrivateKeySpec(bytePrivateKey, spec);
            PrivateKey privateKey = new EdDSAPrivateKey(privKey);
            edDSAEngine.initSign(privateKey);
            edDSAEngine.update(sourceDataBytes);

            //元数据经过私钥加密过后的byte数组类型的签名数据
            byte[] sign = edDSAEngine.sign();

            ByteString encryptedBytes = ByteString.copyFrom(sign);
            return encryptedBytes;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void main(String[] args) {
        String userprikey = "50fcf39115c859ac86ce698010193c1bb7333450fbc0b235a0aca47e5ea21ce9";
        String userpubkey = "141d13555786a1c2cf1b2fe7e6779f2376fc6282e0bdce86a05e950100c640edd246e4a2a5e4bf821925391d247143f67b3e306db153fd2921188c6aa2c5666f";
        String bankpubkey = ConfReadUtil.getProperty("blockchain.bankPubkey");
        ProtobufBean protobufBean = requestTransfer(userprikey, SymbolId.BTY, userpubkey, bankpubkey, 100000000L);
        String jsonResult = BlockUtils.sendPostParam(protobufBean);
        boolean flag = BlockUtils.vilaResult(jsonResult);
        if(!flag){
            System.out.println(BlockUtils.getErrorMessage(jsonResult));
        }
    }

}
