package com.gws.utils.wallet.utils;

import org.bitcoinj.core.*;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.script.ScriptBuilder;
import org.spongycastle.asn1.DERInteger;
import org.spongycastle.asn1.DERSequenceGenerator;
import org.spongycastle.util.encoders.Hex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;

/**
 * Created by fuzamei on 2018/6/7.
 */
public class Sign {

    private byte[] r;
    private byte[] s;
    private String strR;
    private String strS;

    public boolean parserData(String strData) {
        try {
            if (strData == null) {
                return false;
            }
            int lenght = strData.length();
            if (lenght < 140) {
                return false;
            }
            if (lenght == 140) {
                String sub_r_string = strData.substring(8, 72);
                r = Hex.decode(sub_r_string);
                String sub_s_string = strData.substring(76, 140);
                s = Hex.decode(sub_s_string);
                return true;
            }
            if (lenght == 142) {
                String rLength = strData.substring(6, 8);
                if ("20".equals(rLength)) {
                    String sub_r_string = strData.substring(8, 72);
                    r = Hex.decode(sub_r_string);
                } else {
                    String sub_r_string = strData.substring(10, 74);
                    r = Hex.decode(sub_r_string);
                }
                String sub_s_string = strData.substring(78, 142);
                s = Hex.decode(sub_s_string);
                return true;
            }

            if (lenght == 144) {
                String sub_r_string = strData.substring(10, 74);
                r = Hex.decode(sub_r_string);
                String sub_s_string = strData.substring(80, 144);
                s = Hex.decode(sub_s_string);
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public String getBtySignDataFromLocal(byte[] signHash, byte[] prikey)  throws ScriptException {
        ECKey eckey = ECKey.fromPrivate(prikey);
        Sha256Hash hash256 = new Sha256Hash(signHash);
        ECKey.ECDSASignature ecdsas = eckey.sign(hash256);
        byte[] byteSign = ecdsas.encodeToDER();
        String strSigned = Hex.toHexString(byteSign);
        //LogUtil.showLogD("testutil","strSigned："+strSigned);
        if (parserData(strSigned) == false) {
            return null;
        }
        ECKey.ECDSASignature tt = new ECKey.ECDSASignature(new BigInteger(1, r), new BigInteger(1, s));
        ECKey.ECDSASignature tt1 = tt.toCanonicalised();
        byte[] byteTransaction = getTransactionCode(tt1.r, tt1.s);
        String signString = Hex.toHexString(byteTransaction);
        return signString;
    }

    public byte[] getTransactionCode(BigInteger r, BigInteger s) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DERSequenceGenerator seq = new DERSequenceGenerator(bos);
            seq.addObject(new DERInteger(r));
            seq.addObject(new DERInteger(s));
            seq.close();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);  // Cannot happen.
        }
    }

    public byte[] toScript(byte[] pub) {
        byte[] hash160 = Utils.sha256hash160(pub);
        Address addr = new Address(MainNetParams.get(), hash160);
        return ScriptBuilder.createOutputScript(addr).getProgram();
    }

}
