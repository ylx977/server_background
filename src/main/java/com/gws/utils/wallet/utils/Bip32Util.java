package com.gws.utils.wallet.utils;

import com.gws.utils.HexUtil;
import com.gws.utils.wallet.Base58;
import com.gws.utils.wallet.Utils;
import com.gws.utils.wallet.bip32.Bip32;
import com.gws.utils.wallet.bip32.ExtendedKeyPair;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.params.MainNetParams;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;

import java.math.BigInteger;

/**
 * @author ylx
 * Created by fuzamei on 2018/6/1.
 */
public class Bip32Util {

    public static final String getPrivateKey(byte[] seed,int index){
        ExtendedKeyPair extendedKeyPair1 = Bip32.generateMasterKey(seed).ckdPriv(index);
        String privateKey = extendedKeyPair1.getPrivKey().toString(16);
        return privateKey;
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

    public static void main(String[] args) {
//        String seed = "goat frequent actor vital shoulder chaos across glance artefact bitter curve trust rare village earn";
        String seed = "muffin door color cute demise kangaroo affair subway clay bone squirrel script uncover track nuclear";
        String privateKey = getPrivateKey(seed.getBytes(),1);
        System.out.println(privateKey);
        String publicKey = getPublicKey(privateKey,true);
        byte[] bytePublicKey = getBytePublicKey(privateKey,true);
        System.out.println(publicKey);
        String bitcoinAddress = getBitcoinAddress(bytePublicKey);
        String address = bitcoinAddress;
        System.out.println(address);

    }

}
