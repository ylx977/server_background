package com.gws.utils.wallet.utils;

import com.gws.utils.HexUtil;
import com.gws.utils.blockchain.KeyUtils;
import com.gws.utils.wallet.Base58;
import com.gws.utils.wallet.Utils;
import com.gws.utils.wallet.bip32.Bip32;
import com.gws.utils.wallet.bip32.ExtendedKeyPair;
import com.gws.utils.wallet.bip39.SeedCalculator;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;

/**
 * bip39协议
 * 生成公私钥的算法都是ECDSA 不是 EdDSA算法，这两个算法是不一样的，完全不一样
 * Created by fuzamei on 2018/6/4.
 */
public class Bip39Util {

    /**
     * 通过种子+密码 生成私钥(bip方式生成种子的)
     * @param seed
     * @param index
     * @return
     */
    public static String getPrivateKey(String seed, int index) {
        byte[] byteSeed = new SeedCalculator().calculateSeed(seed, "");//fuzamie33
        // byte[] byteSeed = seed.getBytes();
        ExtendedKeyPair masterKey = Bip32.generateMasterKey(byteSeed);
        ExtendedKeyPair childKey = masterKey.generate("m/44H/13107H/0H/0");//13107 13108
         ExtendedKeyPair childKey2 = childKey.ckdPriv(index);
        String prikey = childKey2.getPrivKey().toString(16);
        return prikey;
    }

    public static final String getPublicKey(String privateKey,boolean compress){
        BigInteger bigInteger = new BigInteger(privateKey, 16);
        byte[] bytePubkey = ECKey.publicKeyFromPrivate(bigInteger, compress);
        String publicKey = Utils.bytesToHexString(bytePubkey).toLowerCase();
        return publicKey;
    }

    public static final byte[] getBytePublicKey(String privateKey,boolean compress){
        BigInteger bigInteger = new BigInteger(privateKey, 16);
        byte[] bytePubkey = ECKey.publicKeyFromPrivate(bigInteger, compress);
        return bytePubkey;
    }

    public static final String sign(String unsign,String prikey){
        byte[] byteRawSign = HexUtil.hexString2Bytes(unsign);
        byte[] bytePrikey = HexUtil.hexString2Bytes(prikey);
        byte[] sha256BytesRawSign = Sha256Hash.hash(byteRawSign);
        Sign sign = new Sign();
        String signMessage = sign.getBtySignDataFromLocal(sha256BytesRawSign, bytePrikey);
        return signMessage;
    }

    public static String getBitcoinAddress(byte[] bytePubkey){
        byte[] sha256Bytes = Utils.sha256(bytePubkey);
//        System.out.println("sha256加密=" + Utils.bytesToHexString(sha256Bytes));

        RIPEMD160Digest digest = new RIPEMD160Digest();
        digest.update(sha256Bytes, 0, sha256Bytes.length);
        byte[] ripemd160Bytes = new byte[digest.getDigestSize()];
        digest.doFinal(ripemd160Bytes, 0);

//        System.out.println("ripemd160加密=" + Utils.bytesToHexString(ripemd160Bytes));

        byte[] networkID = new BigInteger("00", 16).toByteArray();
        byte[] extendedRipemd160Bytes = Utils.add(networkID, ripemd160Bytes);

//        System.out.println("添加NetworkID=" + Utils.bytesToHexString(extendedRipemd160Bytes));

        byte[] twiceSha256Bytes = Utils.sha256(Utils.sha256(extendedRipemd160Bytes));

//        System.out.println("两次sha256加密=" + Utils.bytesToHexString(twiceSha256Bytes));

        byte[] checksum = new byte[4];
        System.arraycopy(twiceSha256Bytes, 0, checksum, 0, 4);

//        System.out.println("checksum=" + Utils.bytesToHexString(checksum));

        byte[] binaryBitcoinAddressBytes = Utils.add(extendedRipemd160Bytes, checksum);

//        System.out.println("添加checksum之后=" + Utils.bytesToHexString(binaryBitcoinAddressBytes));

        String bitcoinAddress = Base58.encode(binaryBitcoinAddressBytes);
//        System.out.println("bitcoinAddress=" + bitcoinAddress);
        return bitcoinAddress;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
//        String seed = "剪 走 键 戈 授 泽 梁 缆 严 尔 甚 忙 冰 商 顿";
//        String seed = "improve topic stool jazz doll summer metal achieve fossil scissors guide inmate";
//        String seed = "muffin door color cute demise kangaroo affair subway clay bone squirrel script uncover track nuclear";
        String seed = "sentence fruit price gather job idle canal focus immune display enough sad virus online rare";
//        String seed = "bid shop lottery weird use deliver property thought spice popular outer syrup shoulder cram food";
//        String seed = "丘 病 秋 嘛 喝 诉 新 愿 训 舰 京 良 守 获 湖";
        String privateKey = getPrivateKey(seed, 0);
        System.out.println("privatekey："+privateKey);
        String publicKey = getPublicKey(privateKey, true);
        System.out.println("publickey："+publicKey);
        byte[] bytePublicKey = getBytePublicKey(privateKey, true);
        String bitcoinAddress = getBitcoinAddress(bytePublicKey);
        System.out.println("address:"+bitcoinAddress);
    }


}
