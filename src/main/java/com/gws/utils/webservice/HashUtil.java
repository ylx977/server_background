package com.gws.utils.webservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * 
 * @author ylx
 * @Descri 文件hash计算工具类
 */
public class HashUtil {
	private HashUtil(){
		throw new AssertionError("instantiation is not permitted");
	}
	/** 
     * 根据文件计算出文件的MD5 
     * @param file 
     * @return 
     */  
    public static final String getFileMD5(File file) {  
    	if(!file.exists()){
    		throw new RuntimeException("该文件不存在");
    	}
        if (!file.isFile()) {  
        	throw new RuntimeException("无法对文件夹进行hash计算");
        }  
        MessageDigest digest = null;  
        FileInputStream in = null;  
        byte[] buffer = new byte[1024];  
        int len;  
        try {  
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);  
            while ((len = in.read(buffer)) != -1) {  
                digest.update(buffer, 0, len);  
            }  
        } catch (Exception e) {  
        	throw new RuntimeException("计算文件hash出错");
        }finally {
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					in = null;
				}
			}
		}
        BigInteger bigInt = new BigInteger(1, digest.digest());  
        return bigInt.toString(16);//转换成16进制
    }
    
    public static final String getFileMD5(String filePath){
    	return getFileMD5(new File(filePath));
    }
	
    /** 
     * 计算字符串的hash值
     * @param str
     * @return 
     */
    public static final String getStringMD5(String str) {
    	MessageDigest digest = null;  
        try {  
            digest = MessageDigest.getInstance("MD5");
            digest.update(str.getBytes());
        } catch (Exception e) {  
        	throw new RuntimeException("计算文件hash出错");
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());  
        return bigInt.toString(16);//转换成16进制
    }
//    public static final String getStringMD52(String str) {
//    	MessageDigest digest = null;  
//    	try {  
//    		digest = MessageDigest.getInstance("MD5");
//    	} catch (Exception e) {  
//    		throw new RuntimeException("计算文件hash出错");
//    	}
//    	BigInteger bigInt = new BigInteger(1, digest.digest(str.getBytes()));//这样也能加密(digest这个对象就digest一次，多次digest后的结果是一样的)
//    	return bigInt.toString(16);//转换成16进制
//    }

    /**
     * 对密码进行加密处理的方法
     * @param password
     * @return
     */
    public static final String hashPwd(String password){
        String prefixSalt = "prefix";
        String postSlat = "post";
        //加盐处理
        String newPassword = prefixSalt+password+postSlat;
        //两次hash处理
        return getStringMD5((getStringMD5(newPassword)));
    }
	
	public static void main(String[] args) throws Exception {
        System.out.println("123456------>fca21e77ff82f3a7ae005c54b56bfcc");
        System.out.println("111111------>657657f000a1fa6ebc88e0529a1023d");
        System.out.println("222222------>bc1930e9493ab3773a20874a6575bfd");
        System.out.println("23212------>46f9ec7cba670a754d8c54ab56ac8ef0");
        System.out.println(hashPwd("23212"));
    }

}
