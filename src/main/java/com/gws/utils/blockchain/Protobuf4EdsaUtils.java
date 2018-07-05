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
        transfer.setUid(ByteString.copyFrom(HexUtil.hexString2Bytes(fromAdd)));
        transfer.setToAddr(ByteString.copyFrom(HexUtil.hexString2Bytes(toAdd)));
        transfer.setAmount(amount);
        transfer.setSymbolId(symbolId);
        transfer.setActionId(Msg.MessageType.MsgTransfer);

        // Msg.WriteRequest.Builder的builder对象
        writeRequest.setTransfer(transfer);
        writeRequest.setId(0L);
//        writeRequest.setCreateTime();
        writeRequest.setSign(getSign(transfer,privateKey));

        //最终的请求数据
        Msg.WriteRequest finalRequest = writeRequest.build();
        byte[] finalBytes = finalRequest.toByteArray();
        String sign = Base64.getEncoder().encodeToString(finalBytes);
        return new ProtobufBean(instructionId,sign);
    }


    private static final ByteString getSign(Msg.RequestTransfer.Builder transfer, String operatorKey) {
        try {
            Msg.RequestTransfer requestTransfer = transfer.build();
            byte[] sourceDataBytes = requestTransfer.toByteArray();
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

        String privateKey = KeyUtils.getPrivateKey1(KeyUtils.getRandom());
        String publicKey = KeyUtils.getPublicKey(privateKey);
        System.out.println(privateKey);
        System.out.println(publicKey);
        String userprikey = "a1de1af521a6701260112f0b0487f7034a5abe23719825a628549cca11c6c0ef";
        System.out.println(userprikey);
        String userpubkey = "2f05b971096c569ff361c5ea277fe79b44c2e86a5d6f8a13d32e90b5a58bc786";
        System.out.println(userpubkey);
        String bankpubkey = "9c885ce7664ed3c3675df8e49590c141665d94add5bf72a816a8d8a78bd8fbe5";
        String bankprikey = "017615faee7ade72ecc9838f06e954ab4581802c7be22668c90508c8a9bee1e5";
//        ProtobufBean protobufBean = requestTransfer(userprikey, SymbolId.USDG, userpubkey, bankpubkey, 0L);
        ProtobufBean protobufBean = requestTransfer(bankprikey, SymbolId.USDG, bankpubkey, userpubkey, 100000000L);
        String jsonResult = BlockUtils.sendPostParam(protobufBean);
//        System.out.println(jsonResult);
//        boolean flag = BlockUtils.vilaResult(jsonResult);
//        if(!flag){
//            System.out.println(BlockUtils.getErrorMessage(jsonResult));
//        }
    }

}
