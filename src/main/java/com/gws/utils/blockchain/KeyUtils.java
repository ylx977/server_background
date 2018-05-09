package com.gws.utils.blockchain;

import com.gws.utils.eddsa.EdDSAPrivateKey;
import com.gws.utils.eddsa.spec.EdDSANamedCurveTable;
import com.gws.utils.eddsa.spec.EdDSAParameterSpec;
import com.gws.utils.eddsa.spec.EdDSAPrivateKeySpec;
import com.gws.utils.theromus.sha.Keccak;
import com.gws.utils.theromus.sha.Parameters;
import com.gws.utils.theromus.utils.HexUtils;

import java.util.UUID;

/**
 * Created by zhengfan on 2017/7/13.
 * Explain
 */
public class KeyUtils {

    /**
     * getprivatekey
     * @param password
     * @param random
     * @return
     */
    public static String getPrivateKey(String password, String random) {
        String tempString = "";
        try{
            String key = password + random;
            Keccak keccak = new Keccak();
            String s = HexUtils.getHex(key.getBytes());
            tempString = keccak.getHash(s, Parameters.KECCAK_256);
        }catch (Exception e){
            e.printStackTrace();
        }
        return tempString;
    }
    public static String getPrivateKey1(String random) {
        String tempString = "";
        try{
            String key = random;
            Keccak keccak = new Keccak();
            String s = HexUtils.getHex(key.getBytes());
            tempString = keccak.getHash(s, Parameters.KECCAK_256);
        }catch (Exception e){
            e.printStackTrace();
        }
        return tempString;
    }

    /**
     * getpublickey
     * @param privateKey
     * @return
     */
    public static String getPublicKey(String privateKey) {
        String publickey = "";
        EdDSAParameterSpec spec = EdDSANamedCurveTable.getByName("Ed25519");
        try {
            EdDSAPrivateKeySpec privKey = new EdDSAPrivateKeySpec(HexUtil.hexString2Bytes(privateKey), spec);
            EdDSAPrivateKey sKey = new EdDSAPrivateKey(privKey);
            publickey = HexUtil.bytes2HexString(sKey.getAbyte()).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return publickey;
    }

    /**
     * random to getprivatekey
     * @return
     */
    public static String getRandom() {
        String s = "";
        try{
            String str = UUID.randomUUID().toString().replace("-", "");
            s = str.substring(str.length() - 16);
        }catch (Exception e){
            e.printStackTrace();
        }
        return s;
    }
}
