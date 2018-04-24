package com.gws.utils;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GetFirstZM {

    /** 
     * 提取每个汉字的首字母 
     *  
     * @param str 
     * @return String 
     */  
    public static String getPinYinHeadChar(String str) {
    	if (str == null || "".equals(str)) {
			return "#";
		}
    	for (int i = 0; i < str.length(); i++) {
    		String ch =  str.substring(i, i+1);
    		if (ch == null || "".equals(ch) || " ".equals(ch)) {
    			continue;
    		}else {
    			str = ch;
    			break;
			}
    	}
    	if (str.length() != 1) {
    		return "#";
		}
        String convert = "";  
        for (int j = 0; j < str.length(); j++) {  
            char word = str.charAt(j); 
            String regEx = "[`_ ~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|[1-9]";
		    Pattern pattern = Pattern.compile(regEx);
		    Matcher matcher = pattern.matcher(String.valueOf(word));
		    // 字符串是否与正则表达式相匹配
		    boolean rs = matcher.matches();
		    if(rs){
		    	convert += "#";
		    }else{
		    	// 提取汉字的首字母  
		    	String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
		    	if (pinyinArray != null) {  
		    		convert += Character.toUpperCase(pinyinArray[0].charAt(0));  
		    	} else {  
		    		convert += Character.toUpperCase(word);  
		    	}
		    }
        }
        return checkPinYinHeadChar(convert.charAt(0));  
    }  
    
    /** 
     * 提取每个汉字的首字母 
     *  
     * @param word
     * @return String 
     */  
    public static String checkPinYinHeadChar(char word) {
    	if ("".equals(word)) {
			return "#";
		}
        String regEx = "[#ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz]";
	    Pattern pattern = Pattern.compile(regEx);
	    Matcher matcher = pattern.matcher(String.valueOf(word));
	    // 字符串是否与正则表达式相匹配
	    boolean rs = matcher.matches();
	    if(rs){
	    	return word+"";
	    }else{
	    	return "#";
	    }
    }  
  

    public static void main(String[] args) {  
        String cnStr = " 12喵";
        System.out.print(getPinYinHeadChar(cnStr));
    }
  
}  