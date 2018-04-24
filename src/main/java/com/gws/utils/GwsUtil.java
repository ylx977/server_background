
package com.gws.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 【GwsUtil】
 *
 * @version 
 * @author wenfei  2017年4月6日 上午11:57:29
 * 
 */
public class GwsUtil {

	/**
	 * 
	 * 【ID获取】
	 * 
	 * @author wenfei 2017年4月6日
	 * @return
	 */
	public static Long getSeq(){
		Long maxNum = System.currentTimeMillis();
		Long randNum = Math.round(Math.random() * 1000);
		return maxNum * 1000 + randNum;
	}
	
	/**
	 * 
	 * 【是否Email】
	 * 
	 * @author wenfei 2017年4月19日
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email){
		String regex = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
	    Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(email);
	    return matcher.matches();
	}
	
	/**
	 * 
	 * 【手机号判断】
	 * 
	 * @author wenfei 2017年4月19日
	 * @param phone
	 * @return
	 */
	public static boolean isMobilePhone(String phone){
		String regex = "^(13[0-9]|14[5|7]|15[0-9]|17[0-9]|18[0-9])\\d{8}$";
	    Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(phone);
	    return matcher.matches();
	}
	
	/**
	 * 
	 * 【只有字母】
	 * 
	 * @author wenfei 2017年4月19日
	 * @param letter
	 * @return
	 */
	public static boolean isLetter(String letter){
		String regex = "^[A-Za-z]+$";
	    Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(letter);
	    return matcher.matches();
	}

	/**
	 * 【只有数字】
	 * 
	 * @author wenfei 2017年4月19日
	 * @param value
	 * @return
	 */
	public static boolean isNumber(String value) {
		String regex = "^[0-9]*$";
	    Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(value);
	    return matcher.matches();
	}
	
	/**
	 * 
	 * 【纯数字单价转换为汉字单价
	 *  如：1000000 ==》 100万
	 * 】
	 * 
	 * @author wenfei 2017年4月20日
	 * @param singlePrice
	 * @return
	 */
	public static String unitConvert(String singlePrice) {
		
		BigDecimal number = new BigDecimal(singlePrice);
		DecimalFormat decimalFormat = new DecimalFormat("###################.##");  
		decimalFormat.setRoundingMode(RoundingMode.DOWN);  
		
		if(number.compareTo(BigDecimal.ZERO) <= 0){
			return "0";
		}
		
		if(number.compareTo(new BigDecimal("10000")) < 0){
			BigDecimal bigDecimal = new BigDecimal(singlePrice);
			return decimalFormat.format(bigDecimal);
		}
		
		if(number.compareTo(new BigDecimal("10000000")) < 0){
			BigDecimal bigDecimal = BigDecimalUtils.divide(number, new BigDecimal("10000"), 2, 1);
			return decimalFormat.format(bigDecimal) + "万";
		}
		
		if(number.compareTo(new BigDecimal("100000000")) < 0){
			BigDecimal bigDecimal = BigDecimalUtils.divide(number, new BigDecimal("10000000"), 2, 1);
			return decimalFormat.format(bigDecimal) + "千万";
		}
		
		BigDecimal bigDecimal = BigDecimalUtils.divide(number, new BigDecimal("100000000"), 2, 1);
		return decimalFormat.format(bigDecimal) + "亿";
	}

	/**
	 * 
	 * 获取用户SID
	 * 
	 * @author wangdong 2016年4月19日
	 * @return
	 */
	public static String getSid() {
		// return UUID.randomUUID().toString() + "_" +
		// System.currentTimeMillis();
		return ObjectId.get().toString();
	}

	/**
	 * 手机号码,中间部分自动用*显示
	 *
	 * @author leiyongping 2017年4月28日 下午2:15:58
	 * @param mobile
	 * @return
	 */
	public static String getSpecialMobile(String mobile) {
		if (StringUtils.isBlank(mobile)) {
			return null;
		}

		StringBuilder str = new StringBuilder();
		for (int i = 0; i < mobile.length(); i++) {
			if (i == mobile.length() - 11) {
				str.append(mobile.charAt(i));
			} else if (i == mobile.length() - 10) {
				str.append(mobile.charAt(i));
			} else if (i == mobile.length() - 9) {
				str.append(mobile.charAt(i));
			} else if (i == mobile.length() - 4) {
				str.append(mobile.charAt(i));
			} else if (i == mobile.length() - 3) {
				str.append(mobile.charAt(i));
			} else if (i == mobile.length() - 2) {
				str.append(mobile.charAt(i));
			} else if (i == mobile.length() - 1) {
				str.append(mobile.charAt(i));
			} else {
				str.append("*");
			}
		}
		return str.toString();
	}

	/**
	 * 字符串的后n位用*号代替
	 *
	 * @author leiyongping 2017年4月28日 下午2:18:53
	 * @param content
	 *            要代替的字符串
	 * @param n
	 *            代替的位数
	 * @return
	 */
	public static String replaceString(String content, int n) {
		char[] cs = content.toCharArray();
		for (int i = 0; i < n; i++) {
			cs[cs.length - i - 1] = '*';
		}
		return String.valueOf(cs);
	}
	
	/**
	 * 对字符加星号处理：除前面几位和后面几位外，其他的字符以星号代替
	 *
	 * @author leiyongping 2017年4月28日 下午2:20:22
	 * @param content
	 *            需要处理的字符串
	 * @param frontNum
	 *            保留前面字符的位数
	 * @param endNum
	 *            保留后面字符的位数
	 * @return 带星号的字符串
	 */
	public static String getStarString(String content, int frontNum, int endNum){
		if (StringUtils.isBlank(content)) {
			return null;
		}

		if (frontNum >= content.length() || frontNum < 0) {
			return content;
		}
		if (endNum >= content.length() || endNum < 0) {
			return content;
		}
		if (frontNum + endNum >= content.length()) {
			return content;
		}
		String starStr = "";
		for (int i = 0; i < (content.length() - frontNum - endNum); i++) {
			starStr = starStr + "*";
		}

		return content.substring(0, frontNum).concat(starStr)
				.concat(content.substring(content.length() - endNum, content.length()));
	}

	/**
	 * 查找数组中与目标数最接近的数
	 *
	 * @author leiyongping 2017年6月5日 下午3:15:59
	 * @param array
	 * @param targetNum
	 * @return
	 */
	public static int binarysearchKey(int[] array, int targetNum) {
		Arrays.sort(array);
		int left = 0, right = 0;
		for (right = array.length - 1; left != right;) {
			int midIndex = (right + left) / 2;
			int mid = (right - left);
			int midValue = array[midIndex];
			if (targetNum == midValue) {
				return targetNum;
			}

			if (targetNum > midValue) {
				left = midIndex;
			} else {
				right = midIndex;
			}

			if (mid <= 2) {
				break;
			}
		}
		int rightnum = array[right];
		int leftnum = array[left];
		int ret = Math.abs((rightnum - leftnum) / 2) > Math.abs(rightnum - targetNum) ? rightnum : leftnum;
		if (targetNum > ret) {
			ret = rightnum;
		}
		return ret;
	}
}
