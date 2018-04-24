package com.gws.utils.crypto;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Encrypt utils
 */
public class Encrypt {

    /**
     * Define a hash type enumeration for strong-typing
     */
    public enum HashType {
        MD5("MD5"),
        SHA1("SHA-1"),
        SHA256("SHA-256"),
        SHA512("SHA-512");
        private String algorithm;
        HashType(String algorithm) { this.algorithm = algorithm; }
        @Override
        public String toString() { return this.algorithm; }
    }

    /**
     * Set-up MD5 as the default hashing algorithm
     */
    private static final HashType DEFAULT_HASH_TYPE = HashType.MD5;

    static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    public static String sign(Map<String, Object> messages, String key) {
        if (null == messages || 0 == messages.size() ||
                null == key || 0 == key.length()) {
            return null;
        }

        return sign(mergeDataToSign(messages), key);
    }

    /**
     * Sign a message with a key
     * @param message The message to sign
     * @param key The key to use
     * @return The signed message
     * @throws Exception
     */
    public static String sign(String message, String key) {

        if (null == key || 0 == key.length() ||
                null == message || 0 == message.length()) {
            return message;
        }
        try {
            return Codec.hexMD5(message + key).toLowerCase();
        } catch (CryptoException e) {
            throw new RuntimeException();
        }
    }

    /**
        * Create a password hash using the default hashing algorithm
        * @param input The password
        * @return The password hash
        */
    public static String passwordHash(String input)
    {
        return passwordHash(input, DEFAULT_HASH_TYPE);
    }

    /**
        * Create a password hash using specific hashing algorithm
        * @param input The password
        * @param hashType The hashing algorithm
        * @return The password hash
        */
    public static String passwordHash(String input, HashType hashType)
    {
        try {
            MessageDigest m = MessageDigest.getInstance(hashType.toString());
            byte[] out = m.digest(input.getBytes());
            return new String(Base64.encodeBase64(out));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Encrypt a String with the AES encryption standard. Private key must have a length of 16 bytes
     * @param value The String to encrypt
     * @param privateKey The key used to encrypt
     * @return An hexadecimal encrypted string
     */
    public static String encryptAES(String value, String privateKey) throws CryptoException {
        try {

            byte[] key = Base64.decodeBase64(privateKey.getBytes("utf8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            return Codec.byteToHexString(cipher.doFinal(value.getBytes()));
        } catch (Exception ex) {
            throw new CryptoException(ex);
        }
    }


    /**
     * Decrypt a String with the AES encryption standard. Private key must have a length of 16 bytes
     * @param value An hexadecimal encrypted string
     * @param privateKey The key used to encrypt
     * @return The decrypted String
     */
    public static String decryptAES(String value, String privateKey) throws CryptoException {
        try {
            byte[] key = Base64.decodeBase64(privateKey.getBytes("utf8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            return new String(cipher.doFinal(Codec.hexStringToByte(value)));
        } catch (Exception ex) {
            throw new CryptoException(ex);
        }
    }

    public static String encrypt(String value, String privateKey) throws CryptoException {
        try {
            byte[] raw = privateKey.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            return Codec.byteToHexString(cipher.doFinal(value.getBytes()));
        } catch (Exception ex) {
            throw new CryptoException(ex);
        }
    }

    /** 密钥填充，补齐16位 */
    public static String keyFill(String privateKey) {
    	if (privateKey.length() < 16) {
    		int len = 16 - privateKey.length();
    		for (int i=0; i<len; i ++) {
    			privateKey += "0";
    		}
    	}
    	return privateKey;
    }

    /**
     * 合并待签名的数据,按照key做排序
     * @param data
     * @return
     */
    private static String mergeDataToSign(Map<String, Object> data) {
        StringBuffer content = new StringBuffer();

        List<String> keys = new ArrayList(data.keySet());
        Collections.sort(keys);

        for (String key : keys) {
            Object value = data.get(key);
            if (null != value) {
                content.append(value.toString());
            }
        }

        return content.toString();
    }

    /**
    public static void main(String[] args) throws Exception {
        Map<String, String> data = new HashedMap();
        data.put("b", "54321");
        data.put("a", "12345");
        data.put("c", "abc");

        System.out.println(Encrypt.sign(data, "abc"));
    }
     **/
}
