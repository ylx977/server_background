package com.gws.utils.validate;


import com.gws.common.constants.backstage.ErrorMsg;
import com.gws.common.constants.backstage.LangMark;
import com.gws.exception.ExceptionUtils;

/**
 * 
 * @author ylx
 * @describe: 针对hibernate-validator的实体类进行数据校验的工具类
 * @describe: 针对该项目中其它各类数据校验的工具类
 * @Date: 2017-12-26
 */
public class ValidationUtil {

	private ValidationUtil() {
		throw new AssertionError("instaniation is not permitted");
	}

//*****************************************************以下方法都加了int lang参数，支持多国语言*******************************************************************************************8
	/**
	 * @Title: checkRangeOfInt
	 * @Description: 针对传入的参数进行校验--->Integer类型
	 * @Detail: 参数类型虽然是Object,但是要求数据类型是String或者是Integer类型的数据,而且String类型还必须是能被解析成整数的,否则会抛出异常.
	 * 			而且还要限定数值的范围,超出范围的也会报异常.
	 * 			如果一切都解析正常,返回一个true的布尔类型
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验
	 * @return boolean
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final boolean checkRangeOfInt(final Object obj, int min, int max, int lang, final String... patterns) {
		String number = null;
		try {
			if (obj == null) {
				throw new NullPointerException();
			}
			number = String.class.cast(obj);
			try {
				Integer num = Integer.parseInt(number);
				if (num < min) {
					ExceptionUtils.throwException(ErrorMsg.GREATER_THAN,lang,min);
				}
				if(num > max){
					ExceptionUtils.throwException(ErrorMsg.LESS_THAN,lang,max);
				}
			} catch (NumberFormatException e) {
				throw new NumberFormatException();
			}
		} catch (Exception e) {
			if (e instanceof NullPointerException) {
				ExceptionUtils.throwException(ErrorMsg.NULL_POINT,lang);
			}
			if (e instanceof NumberFormatException) {
				ExceptionUtils.throwException(ErrorMsg.NUMBER_PARSE_EXCEPTION,lang);
			}
			if (e instanceof OutOfRangeException) {
				throw new OutOfRangeException(e.getMessage());
			}
			Integer num = null;
			try {
				num = Integer.class.cast(obj);
			} catch (Exception e1) {
				ExceptionUtils.throwException(ErrorMsg.NUMBER_PARSE_EXCEPTION,lang);
			}
			if (num < min) {
				ExceptionUtils.throwException(ErrorMsg.GREATER_THAN,lang,min);
			}
			if(num > max){
				ExceptionUtils.throwException(ErrorMsg.LESS_THAN,lang,max);
			}
		}
		if(patterns.length!=0){//格式校验
			for (String pattern : patterns) {
				if(String.valueOf(obj).matches(pattern)){
					return true;
				}
			}
			ExceptionUtils.throwException(ErrorMsg.WRONG_FORMAT,lang);
		}
		return true;
	}

	/**
	 * @Title: checkRangeOfInt
	 * @Description: 针对传入的参数进行校验--->Integer类型
	 * @Detail: 参数类型虽然是Object,但是要求数据类型是String或者是Integer类型的数据,而且String类型还必须是能被解析成整数的,否则会抛出异常.
	 * 			而且还要限定数值的范围,超出范围的也会报异常.
	 * 			如果一切都解析正常,返回一个true的布尔类型
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验
	 * @return boolean
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final boolean checkRangeOfInt(final Object obj, int min, int max, final String... patterns) {
		return checkRangeOfInt(obj,min,max, LangMark.CN,patterns);
	}

	/**
	 * @Title: checkRangeOfInt
	 * @Description: 针对传入的参数进行校验--->Integer类型
	 * @Detail: 要限定数值的范围,超出范围的会报异常，如果一切都解析正常,返回一个true的布尔类型
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验
	 * @return boolean
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final boolean checkRangeOfInt(final Integer intValue,int min,int max,int lang, final String...patterns){
		if (intValue < min) {
			ExceptionUtils.throwException(ErrorMsg.GREATER_THAN,lang,min);
		}
		if(intValue > max){
			ExceptionUtils.throwException(ErrorMsg.LESS_THAN,lang,max);
		}
		if(patterns.length!=0){//格式校验
			for (String pattern : patterns) {
				if(String.valueOf(intValue).matches(pattern)){
					return true;
				}
			}
			ExceptionUtils.throwException(ErrorMsg.WRONG_FORMAT,lang);
		}
		return false;
	}

	public static final boolean checkRangeOfInt(final Integer intValue,int min,int max, final String...patterns){
		return checkRangeOfInt(intValue,min,max,LangMark.CN,patterns);
	}

	/**
	 * @Title: checkMinOfInt
	 * @Description: 针对传入的参数进行校验--->Integer类型
	 * @Detail: 参数类型虽然是Object,但是要求数据类型是String或者是Integer类型的数据,而且String类型还必须是能被解析成整数的,否则会抛出异常.
	 * 			而且还要限定数值的最小值,小于最小值的也会报异常.
	 * 			如果一切都解析正常,返回一个true的布尔类型
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验
	 * @return boolean
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final boolean checkMinOfInt(final Object obj, int min,final String...patterns) {
		return checkRangeOfInt(obj, min, Integer.MAX_VALUE,patterns);
	}
	public static final boolean checkMinOfInt(final Object obj, int min,int lang,final String...patterns) {
		return checkRangeOfInt(obj, min, Integer.MAX_VALUE,lang,patterns);
	}

	/**
	 * @Title: checkMinOfInt
	 * @Description: 针对传入的参数进行校验--->Integer类型
	 * @Detail: 限定数值的最小值,小于最小值会报异常，	如果一切都解析正常,返回一个true的布尔类型
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验
	 * @return boolean
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final boolean checkMinOfInt(final Integer intValue, int min,final String...patterns){
		return checkRangeOfInt(intValue, min, Integer.MAX_VALUE, patterns);
	}
	public static final boolean checkMinOfInt(final Integer intValue, int min,int lang,final String...patterns){
		return checkRangeOfInt(intValue, min, Integer.MAX_VALUE,lang, patterns);
	}

	/**
	 * @Title: checkMaxOfInt
	 * @Description: 针对传入的参数进行校验--->Integer类型
	 * @Detail: 参数类型虽然是Object,但是要求数据类型是String或者是Integer类型的数据,而且String类型还必须是能被解析成整数的,否则会抛出异常.
	 * 			而且还要限定数值的最大值,大于最大值的也会报异常.
	 * 			如果一切都解析正常,返回一个true的布尔类型
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验
	 * @return boolean
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final boolean checkMaxOfInt(final Object obj, int max,final String...patterns) {
		return checkRangeOfInt(obj, Integer.MIN_VALUE, max,patterns);
	}
	public static final boolean checkMaxOfInt(final Object obj, int max,int lang,final String...patterns) {
		return checkRangeOfInt(obj, Integer.MIN_VALUE, max,lang,patterns);
	}

	/**
	 * @Title: checkMaxOfInt
	 * @Description: 针对传入的参数进行校验--->Integer类型
	 * @Detail: 限定数值的最大值,大于最大值的也会报异常，如果一切都解析正常,返回一个true的布尔类型
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验
	 * @return boolean
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final boolean checkMaxOfInt(final Integer intValue, int max,final String...patterns) {
		return checkRangeOfInt(intValue, Integer.MIN_VALUE, max,patterns);
	}
	public static final boolean checkMaxOfInt(final Integer intValue, int max,int lang,final String...patterns) {
		return checkRangeOfInt(intValue, Integer.MIN_VALUE, max,lang,patterns);
	}

	/**
	 * @Title: checkOfInt
	 * @Description: 针对传入的参数进行校验--->Integer类型
	 * @Detail: 参数类型虽然是Object,但是要求数据类型是String或者是Integer类型的数据,而且String类型还必须是能被解析成整数的,否则会抛出异常.
	 * 			针对整个Integer范围内的数据校验(也就是能被解析成Integer的String还是Integer就能校验成功返回true)
	 * 			如果一切都解析正常,返回一个true的布尔类型
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验
	 * @return boolean
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final boolean checkOfInt(final Object obj,final String...patterns) {
		return checkRangeOfInt(obj, Integer.MIN_VALUE, Integer.MAX_VALUE,patterns);
	}
	public static final boolean checkOfInt(final Object obj,int lang,final String...patterns) {
		return checkRangeOfInt(obj, Integer.MIN_VALUE, Integer.MAX_VALUE,lang,patterns);
	}

	/**
	 * @Title: checkOfInt
	 * @Description: 针对传入的参数进行校验--->Integer类型
	 * @Detail: 针对整个Integer范围内的数据校验(也就是能被解析成Integer的String还是Integer就能校验成功返回true)
	 * 			如果一切都解析正常,返回一个true的布尔类型
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验
	 * @return boolean
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final boolean checkOfInt(final Integer intValue,final String...patterns) {
		return checkRangeOfInt(intValue, Integer.MIN_VALUE, Integer.MAX_VALUE,patterns);
	}
	public static final boolean checkOfInt(final Integer intValue,int lang,final String...patterns) {
		return checkRangeOfInt(intValue, Integer.MIN_VALUE, Integer.MAX_VALUE,lang,patterns);
	}

	/**
	 * @Title: checkRangeOfLong
	 * @Description: 针对传入的参数进行校验--->Long类型
	 * @Detail: 参数类型虽然是Object,但是要求数据类型是String或者是Long类型的数据,而且String类型还必须是能被解析成整数的,否则会抛出异常.
	 * 			而且还要限定数值的范围,超出范围的也会报异常.
	 * 			如果一切都解析正常,返回一个true的布尔类型
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验
	 * @return boolean
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final boolean checkRangeOfLong(final Object obj, long min, long max,int lang, final String...patterns) {
		String number = null;
		try {
			if (obj == null) {
				throw new NullPointerException();
			}
			number = String.class.cast(obj);
			try {
				Long num = Long.parseLong(number);
				if (num < min) {
					ExceptionUtils.throwException(ErrorMsg.GREATER_THAN_OR_EQUAL,lang,min);
				}
				if(num > max){
					ExceptionUtils.throwException(ErrorMsg.LESS_THAN_OR_EQUAL,lang,max);
				}
			} catch (NumberFormatException e) {
				throw new NumberFormatException();
			}
		} catch (Exception e) {
			if (e instanceof NullPointerException) {
				ExceptionUtils.throwException(ErrorMsg.NULL_POINT,lang);
			}
			if (e instanceof NumberFormatException) {
				ExceptionUtils.throwException(ErrorMsg.NUMBER_PARSE_EXCEPTION,lang);
			}
			if (e instanceof OutOfRangeException) {
				throw new OutOfRangeException(e.getMessage());
			}
			Long num = null;
			try {
				num=Long.parseLong(String.valueOf(obj));
			} catch (Exception e1) {
				ExceptionUtils.throwException(ErrorMsg.NUMBER_PARSE_EXCEPTION,lang);
			}
			if (num < min) {
				ExceptionUtils.throwException(ErrorMsg.GREATER_THAN_OR_EQUAL,lang,min);
			}
			if(num > max){
				ExceptionUtils.throwException(ErrorMsg.LESS_THAN_OR_EQUAL,lang,max);
			}
		}
		if(patterns.length!=0){//格式校验
			for (String pattern : patterns) {
				if(String.valueOf(obj).matches(pattern)){
					return true;
				}
			}
			ExceptionUtils.throwException(ErrorMsg.WRONG_FORMAT,lang);
		}
		return true;
	}

	public static final boolean checkRangeOfLong(final Object obj, long min, long max, final String...patterns) {
		return checkRangeOfLong(obj,min,max,LangMark.CN,patterns);
	}
	
	/**
	 * @Title: checkRangeOfLong
	 * @Description: 针对传入的参数进行校验--->Long类型
	 * @Detail: 限定数值的范围,超出范围的也会报异常，如果一切都解析正常,返回一个true的布尔类型
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验
	 * @return boolean
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final boolean checkRangeOfLong(final Long longValue, long min, long max, int lang, final String...patterns) {
		if (longValue < min) {
			ExceptionUtils.throwException(ErrorMsg.GREATER_THAN_OR_EQUAL,lang,min);
		}
		if(longValue > max){
			ExceptionUtils.throwException(ErrorMsg.LESS_THAN_OR_EQUAL,lang,max);
		}
		if(patterns.length!=0){//格式校验
			for (String pattern : patterns) {
				if(String.valueOf(longValue).matches(pattern)){
					return true;
				}
			}
			ExceptionUtils.throwException(ErrorMsg.WRONG_FORMAT,lang);
		}
		return true;
	}

	public static final boolean checkRangeOfLong(final Long longValue, long min, long max, final String...patterns) {
		return checkRangeOfLong(longValue, min, max, LangMark.CN,patterns);
	}
	
	/**
	 * @Title: checkMinOfLong
	 * @Description: 针对传入的参数进行校验--->Long类型
	 * @Detail: 参数类型虽然是Object,但是要求数据类型是String或者是Long类型的数据,而且String类型还必须是能被解析成整数的,否则会抛出异常.
	 * 			而且还要限定数值的最小值,小于最小值的也会报异常.
	 * 			如果一切都解析正常,返回一个true的布尔类型
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验
	 * @return boolean
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final boolean checkMinOfLong(final Object obj, long min,final String...patterns) {
		return checkRangeOfLong(obj, min, Long.MAX_VALUE,patterns);
	}
	public static final boolean checkMinOfLong(final Object obj, long min,int lang,final String...patterns) {
		return checkRangeOfLong(obj, min, Long.MAX_VALUE,lang,patterns);
	}

	/**
	 * @Title: checkMinOfLong
	 * @Description: 针对传入的参数进行校验--->Long类型
	 * @Detail: 限定数值的最小值,小于最小值的也会报异常,如果一切都解析正常,返回一个true的布尔类型
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验
	 * @return boolean
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final boolean checkMinOfLong(final Long longValue, long min,final String...patterns) {
		return checkRangeOfLong(longValue, min, Long.MAX_VALUE,patterns);
	}
	public static final boolean checkMinOfLong(final Long longValue, long min,int lang,final String...patterns) {
		return checkRangeOfLong(longValue, min, Long.MAX_VALUE,lang,patterns);
	}

	/**
	 * @Title: checkMaxOfLong
	 * @Description: 针对传入的参数进行校验--->Long类型
	 * @Detail: 参数类型虽然是Object,但是要求数据类型是String或者是Long类型的数据,而且String类型还必须是能被解析成整数的,否则会抛出异常.
	 * 			而且还要限定数值的最大值,大于最大值的也会报异常.
	 * 			如果一切都解析正常,返回一个true的布尔类型
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验
	 * @return boolean
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final boolean checkMaxOfLong(final Object obj, long max, final String...patterns) {
		return checkRangeOfLong(obj, Long.MIN_VALUE, max,patterns);
	}
	public static final boolean checkMaxOfLong(final Object obj, long max,int lang, final String...patterns) {
		return checkRangeOfLong(obj, Long.MIN_VALUE, max,lang,patterns);
	}

	/**
	 * @Title: checkMaxOfLong
	 * @Description: 针对传入的参数进行校验--->Long类型
	 * @Detail: 限定数值的最大值,大于最大值的也会报异常,如果一切都解析正常,返回一个true的布尔类型
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验
	 * @return boolean
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final boolean checkMaxOfLong(final Long longValue, long max, final String...patterns) {
		return checkRangeOfLong(longValue, Long.MIN_VALUE, max,patterns);
	}
	public static final boolean checkMaxOfLong(final Long longValue, long max,int lang, final String...patterns) {
		return checkRangeOfLong(longValue, Long.MIN_VALUE, max,lang,patterns);
	}

	/**
	 * @Title: checkOfLong
	 * @Description: 针对传入的参数进行校验--->Long类型
	 * @Detail: 参数类型虽然是Object,但是要求数据类型是String或者是Long类型的数据,而且String类型还必须是能被解析成整数的,否则会抛出异常.
	 * 			这个针对Long范围内类型数据的校验
	 * 			如果一切都解析正常,返回一个true的布尔类型
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验
	 * @return boolean
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final boolean checkOfLong(final Object obj,final String...patterns) {
		return checkRangeOfLong(obj, Long.MIN_VALUE, Long.MAX_VALUE,patterns);
	}
	public static final boolean checkOfLong(final Object obj,int lang,final String...patterns) {
		return checkRangeOfLong(obj, Long.MIN_VALUE, Long.MAX_VALUE,lang,patterns);
	}

	/**
	 * @Title: checkOfLong
	 * @Description: 针对传入的参数进行校验--->Long类型
	 * @Detail: 针对Long范围内类型数据的校验,如果一切都解析正常,返回一个true的布尔类型
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验
	 * @return boolean
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final boolean checkOfLong(final Long longValue,final String...patterns) {
		return checkRangeOfLong(longValue, Long.MIN_VALUE, Long.MAX_VALUE,patterns);
	}
	public static final boolean checkOfLong(final Long longValue,int lang,final String...patterns) {
		return checkRangeOfLong(longValue, Long.MIN_VALUE, Long.MAX_VALUE,lang,patterns);
	}

	/**
	 * @Title: checkRangeAndAssignInt
	 * @Description: 针对传入的参数进行校验--->Integer类型
	 * @Detail: 参数类型虽然是Object,但是要求数据类型是String或者是Integer类型的数据,而且String类型还必须是能被解析成整数的,否则会抛出异常.
	 * 			而且还要限定数值的范围,超出范围的也会报异常.
	 * 			如果一切都解析正常,说明解析没有问题,结果将解析后的数值以int类型返回
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验和赋值同时进行
	 * @return int
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final int checkRangeAndAssignInt(final Object obj, int min, int max,int lang, final String...patterns) {
		String number = null;
		try {
			if (obj == null) {
				throw new NullPointerException();
			}
			number = String.class.cast(obj);
			try {
				Integer num = Integer.parseInt(number);
				if (num < min) {
					ExceptionUtils.throwException(ErrorMsg.GREATER_THAN_OR_EQUAL,lang,min);
				}
				if(num > max){
					ExceptionUtils.throwException(ErrorMsg.LESS_THAN_OR_EQUAL,lang,max);
				}
				if(patterns.length!=0){//格式校验
					for (String pattern : patterns) {
						if(number.matches(pattern)){
							return num;
						}
					}
					ExceptionUtils.throwException(ErrorMsg.NUMBER_PARSE_EXCEPTION,lang);
				}
				return num;
			} catch (NumberFormatException e) {
				throw new NumberFormatException();
			}
		} catch (Exception e) {
			if (e instanceof NullPointerException) {
				ExceptionUtils.throwException(ErrorMsg.NULL_POINT,lang);
			}
			if (e instanceof NumberFormatException) {
				ExceptionUtils.throwException(ErrorMsg.NUMBER_PARSE_EXCEPTION,lang);
			}
			if (e instanceof OutOfRangeException) {
				throw new OutOfRangeException(e.getMessage());
			}
			Integer num = null;
			try {
				num = Integer.class.cast(obj);
			} catch (Exception e1) {
				ExceptionUtils.throwException(ErrorMsg.NUMBER_PARSE_EXCEPTION,lang);
			}
			if (num < min) {
				ExceptionUtils.throwException(ErrorMsg.GREATER_THAN_OR_EQUAL,lang,min);
			}
			if(num > max){
				ExceptionUtils.throwException(ErrorMsg.LESS_THAN_OR_EQUAL,lang,max);
			}
			if(patterns.length!=0){//格式校验
				for (String pattern : patterns) {
					if(String.valueOf(obj).matches(pattern)){
						return num;
					}
				}
				ExceptionUtils.throwException(ErrorMsg.WRONG_FORMAT,lang);
			}
			return num;
		}
	}

	public static final int checkRangeAndAssignInt(final Object obj, int min, int max, final String...patterns) {
		return checkRangeAndAssignInt(obj,min,max,LangMark.CN,patterns);
	}
	
	/**
	 * @Title: checkRangeAndAssignInt
	 * @Description: 针对传入的参数进行校验--->Integer类型
	 * @Detail: 限定数值的范围,超出范围的会报异常,如果一切都解析正常,说明解析没有问题,结果将解析后的数值以int类型返回
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验和赋值同时进行
	 * @return int
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final int checkRangeAndAssignInt(final Integer intValue, int min, int max, int lang, final String...patterns) {
		if (intValue < min) {
			ExceptionUtils.throwException(ErrorMsg.GREATER_THAN_OR_EQUAL,lang,min);
		}
		if(intValue > max){
			ExceptionUtils.throwException(ErrorMsg.LESS_THAN_OR_EQUAL,lang,max);
		}
		if(patterns.length!=0){//格式校验
			for (String pattern : patterns) {
				if(String.valueOf(intValue).matches(pattern)){
					return intValue;
				}
			}
			ExceptionUtils.throwException(ErrorMsg.WRONG_FORMAT,lang);
		}
		return intValue;
	}

	public static final int checkRangeAndAssignInt(final Integer intValue, int min, int max, final String...patterns) {
		return checkRangeAndAssignInt(intValue,min,max,LangMark.CN,patterns);
	}

	/**
	 * @Title: checkMinAndAssignInt
	 * @Description: 针对传入的参数进行校验--->Integer类型
	 * @Detail: 参数类型虽然是Object,但是要求数据类型是String或者是Integer类型的数据,而且String类型还必须是能被解析成整数的,否则会抛出异常.
	 * 			而且还要限定数值的最小值,小于最小值的也会报异常.
	 * 			如果一切都解析正常,说明解析没有问题,结果将解析后的数值以int类型返回
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验和赋值同时进行
	 * @return int
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final int checkMinAndAssignInt(final Object obj, int min,final String...patterns) {
		return checkRangeAndAssignInt(obj,min,Integer.MAX_VALUE,patterns);
	}
	public static final int checkMinAndAssignInt(final Object obj, int min,int lang ,final String...patterns) {
		return checkRangeAndAssignInt(obj,min,Integer.MAX_VALUE,lang,patterns);
	}

	/**
	 * @Title: checkMinAndAssignInt
	 * @Description: 针对传入的参数进行校验--->Integer类型
	 * @Detail: 限定数值的最小值,小于最小值的会报异常,如果一切都解析正常,说明解析没有问题,结果将解析后的数值以int类型返回
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验和赋值同时进行
	 * @return int
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final int checkMinAndAssignInt(final Integer intValue, int min,final String...patterns) {
		return checkRangeAndAssignInt(intValue,min,Integer.MAX_VALUE,patterns);
	}
	public static final int checkMinAndAssignInt(final Integer intValue, int min,int lang,final String...patterns) {
		return checkRangeAndAssignInt(intValue,min,Integer.MAX_VALUE,lang,patterns);
	}

	/**
	 * @Title: checkMaxAndAssignInt
	 * @Description: 针对传入的参数进行校验--->Integer类型
	 * @Detail: 参数类型虽然是Object,但是要求数据类型是String或者是Integer类型的数据,而且String类型还必须是能被解析成整数的,否则会抛出异常.
	 * 			而且还要限定数值的最大值,大于最大值的也会报异常.
	 * 			如果一切都解析正常,说明解析没有问题,结果将解析后的数值以int类型返回
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验和赋值同时进行
	 * @return int
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final int checkMaxAndAssignInt(final Object obj, int max, final String...patterns) {
		return checkRangeAndAssignInt(obj,Integer.MIN_VALUE,max,patterns);
	}
	public static final int checkMaxAndAssignInt(final Object obj, int max,int lang ,final String...patterns) {
		return checkRangeAndAssignInt(obj,Integer.MIN_VALUE,max, lang ,patterns);
	}

	/**
	 * @Title: checkMaxAndAssignInt
	 * @Description: 针对传入的参数进行校验--->Integer类型
	 * @Detail: 限定数值的最大值,大于最大值的也会报异常,如果一切都解析正常,说明解析没有问题,结果将解析后的数值以int类型返回
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验和赋值同时进行
	 * @return int
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final int checkMaxAndAssignInt(final Integer intValue, int max, final String...patterns) {
		return checkRangeAndAssignInt(intValue,Integer.MIN_VALUE,max,patterns);
	}
	public static final int checkMaxAndAssignInt(final Integer intValue, int max, int lang ,final String...patterns) {
		return checkRangeAndAssignInt(intValue,Integer.MIN_VALUE,max,lang,patterns);
	}

	/**
	 * @Title: checkAndAssignInt
	 * @Description: 针对传入的参数进行校验--->Integer类型
	 * @Detail: 参数类型虽然是Object,但是要求数据类型是String或者是Integer类型的数据,而且String类型还必须是能被解析成整数的,否则会抛出异常.
	 * 			这个针对Integer范围内类型数据的校验和赋值
	 * 			如果一切都解析正常,说明解析没有问题,结果将解析后的数值以int类型返回
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验和赋值同时进行
	 * @return int
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final int checkAndAssignInt(final Object obj, final String...patterns) {
		return checkRangeAndAssignInt(obj,Integer.MIN_VALUE,Integer.MAX_VALUE,patterns);
	}
	public static final int checkAndAssignInt(final Object obj, int lang ,final String...patterns) {
		return checkRangeAndAssignInt(obj,Integer.MIN_VALUE,Integer.MAX_VALUE,lang,patterns);
	}

	/**
	 * @Title: checkAndAssignInt
	 * @Description: 针对传入的参数进行校验--->Integer类型
	 * @Detail: 针对Integer范围内类型数据的校验和赋值,如果一切都解析正常,说明解析没有问题,结果将解析后的数值以int类型返回
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验和赋值同时进行
	 * @return int
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final int checkAndAssignInt(final Integer intValue, final String...patterns) {
		return checkRangeAndAssignInt(intValue,Integer.MIN_VALUE,Integer.MAX_VALUE,patterns);
	}
	public static final int checkAndAssignInt(final Integer intValue, int lang ,final String...patterns) {
		return checkRangeAndAssignInt(intValue,Integer.MIN_VALUE,Integer.MAX_VALUE,lang,patterns);
	}

	/**
	 * @Title: checkBlankIntegerAndAssignNullIfIsBlank
	 * @Description: 针对传入的参数进行校验--->Integer类型
	 * @Detail: 参数类型虽然是Object,但是要求数据类型是String或者是Integer类型的数据,而且String类型还必须是能被解析成整数的,否则会抛出异常.
	 * 			这个针对Integer范围内类型数据的校验和赋值
	 * 			如果传入的参数为null，空串""或空长串" "均返回null
	 * 			如果一切都解析正常,说明解析没有问题,结果将解析后的数值以Integer类型返回
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验和赋值同时进行
	 * @return int
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final Integer checkBlankIntegerAndAssignNullIfIsBlank(final Object obj,int lang ,final String...patterns) {
		String number = null;
		try {
			if (obj == null) {
				throw new NullPointerException();
			}
			number = String.class.cast(obj);
			if("".equals(number.trim())) {
				throw new NullPointerException();
			}
			try {
				Integer num = Integer.parseInt(number);
				if (num < Integer.MIN_VALUE) {
					ExceptionUtils.throwException(ErrorMsg.GREATER_THAN_OR_EQUAL,lang,Integer.MIN_VALUE);
				}
				if(num > Integer.MAX_VALUE){
					ExceptionUtils.throwException(ErrorMsg.LESS_THAN_OR_EQUAL,lang,Integer.MAX_VALUE);
				}
				if(patterns.length!=0){//格式校验
					for (String pattern : patterns) {
						if(number.matches(pattern)){
							return num;
						}
					}
					ExceptionUtils.throwException(ErrorMsg.WRONG_FORMAT,lang);
				}
				return num;
			} catch (NumberFormatException e) {
				throw new NumberFormatException();
			}
		} catch (Exception e) {
			if (e instanceof NullPointerException) {
				return null;
			}
			if (e instanceof NumberFormatException) {
				ExceptionUtils.throwException(ErrorMsg.WRONG_FORMAT,lang);
			}
			if (e instanceof OutOfRangeException) {
				throw new OutOfRangeException(e.getMessage());
			}
			Integer num = null;
			try {
				num = Integer.class.cast(obj);
			} catch (Exception e1) {
				ExceptionUtils.throwException(ErrorMsg.NUMBER_PARSE_EXCEPTION,lang);
			}
			if (num < Integer.MIN_VALUE) {
				ExceptionUtils.throwException(ErrorMsg.GREATER_THAN_OR_EQUAL,lang,Integer.MIN_VALUE);
			}
			if(num > Integer.MAX_VALUE){
				ExceptionUtils.throwException(ErrorMsg.LESS_THAN_OR_EQUAL,lang,Integer.MAX_VALUE);
			}
			if(patterns.length!=0){//格式校验
				for (String pattern : patterns) {
					if(String.valueOf(obj).matches(pattern)){
						return num;
					}
				}
				ExceptionUtils.throwException(ErrorMsg.WRONG_FORMAT,lang);
			}
			return num;
		}
	}

	public static final Integer checkBlankIntegerAndAssignNullIfIsBlank(final Object obj,final String...patterns) {
		return checkBlankIntegerAndAssignNullIfIsBlank(obj,LangMark.CN,patterns);
	}
	
	
	/**
	 * @Title: checkRangeAndAssignDouble
	 * @Description: 针对传入的参数进行校验--->Double类型
	 * @Detail: 参数类型虽然是Object,但是要求数据类型是String或者是Double类型的数据,而且String类型还必须是能被解析成double的,否则会抛出异常.
	 * 			而且还要限定数值的范围,超出范围的也会报异常.
	 * 			如果一切都解析正常,说明解析没有问题,结果将解析后的数值以double类型返回
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验和赋值同时进行
	 * @return double
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final double checkRangeAndAssignDouble(final Object obj, double min, double max, int lang,final String...patterns) {
		String number = null;
		try {
			if (obj == null) {
				throw new NullPointerException();
			}
			number = String.class.cast(obj);
			try {
				Double num = Double.parseDouble(number);
				if (num < min) {
					ExceptionUtils.throwException(ErrorMsg.GREATER_THAN_OR_EQUAL,lang,min);
				}
				if(num > max){
					ExceptionUtils.throwException(ErrorMsg.LESS_THAN_OR_EQUAL,lang,max);
				}
				if(patterns.length!=0){//格式校验
					for (String pattern : patterns) {
						if(number.matches(pattern)){
							return num;
						}
					}
					ExceptionUtils.throwException(ErrorMsg.WRONG_FORMAT,lang);
				}
				return num;
			} catch (NumberFormatException e) {
				throw new NumberFormatException();
			}
		} catch (Exception e) {
			if (e instanceof NullPointerException) {
				ExceptionUtils.throwException(ErrorMsg.NULL_POINT,lang);
			}
			if (e instanceof NumberFormatException) {
				ExceptionUtils.throwException(ErrorMsg.NUMBER_PARSE_EXCEPTION,lang);
			}
			if (e instanceof OutOfRangeException) {
				throw new OutOfRangeException(e.getMessage());
			}
			Double num = null;
			try {
				num = Double.parseDouble(String.valueOf(obj));
			} catch (Exception e1) {
				ExceptionUtils.throwException(ErrorMsg.NUMBER_PARSE_EXCEPTION,lang);
			}
			if (num < min) {
				ExceptionUtils.throwException(ErrorMsg.GREATER_THAN_OR_EQUAL,lang,min);
			}
			if(num > max){
				ExceptionUtils.throwException(ErrorMsg.LESS_THAN_OR_EQUAL,lang,max);
			}
			if(patterns.length!=0){//格式校验
				for (String pattern : patterns) {
					if(String.valueOf(obj).matches(pattern)){
						return num;
					}
				}
				ExceptionUtils.throwException(ErrorMsg.WRONG_FORMAT,lang);
			}
			return num;
		}
	}


	public static final double checkRangeAndAssignDouble(final Object obj, double min, double max,final String...patterns) {
		return checkRangeAndAssignDouble(obj,min,max,LangMark.CN,patterns);
	}
	
	/**
	 * @Title: checkRangeAndAssignDouble
	 * @Description: 针对传入的参数进行校验--->Double类型
	 * @Detail: 限定数值的范围,超出范围的也会报异常,如果一切都解析正常,说明解析没有问题,结果将解析后的数值以double类型返回
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验和赋值同时进行
	 * @return double
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final double checkRangeAndAssignDouble(final Double doubleValue, double min, double max,int lang,final String...patterns) {
		if(doubleValue==null){
			ExceptionUtils.throwException(ErrorMsg.NULL_POINT,lang);
	}
		if (doubleValue < min) {
			ExceptionUtils.throwException(ErrorMsg.GREATER_THAN_OR_EQUAL,lang,min);
		}
		if(doubleValue > max){
			ExceptionUtils.throwException(ErrorMsg.LESS_THAN_OR_EQUAL,lang,max);
		}
		if(patterns.length!=0){//格式校验
			for (String pattern : patterns) {
				if(String.valueOf(doubleValue).matches(pattern)){
					return doubleValue;
				}
			}
			ExceptionUtils.throwException(ErrorMsg.WRONG_FORMAT,lang);
		}
		return doubleValue;
	}

	public static final double checkRangeAndAssignDouble(final Double doubleValue, double min, double max,final String...patterns) {
		return checkRangeAndAssignDouble(doubleValue,min,max,LangMark.CN,patterns);
	}
	
	/**
	 * @Title: checkRangeAndAssignDouble
	 * @Description: 针对传入的参数进行校验--->Double类型
	 * @Detail: 参数类型虽然是Object,但是要求数据类型是String或者是Double类型的数据,而且String类型还必须是能被解析成double的,否则会抛出异常.
	 * 			而且还要限定数值的最大值,超出最大值的也会报异常.
	 * 			如果一切都解析正常,说明解析没有问题,结果将解析后的数值以double类型返回
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验和赋值同时进行
	 * @return double
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final double checkMinAndAssignDouble(final Object obj, double min, final String...patterns) {
		return checkRangeAndAssignDouble(obj,min,Double.MAX_VALUE,patterns);
	}
	public static final double checkMinAndAssignDouble(final Object obj, double min, int lang ,final String...patterns) {
		return checkRangeAndAssignDouble(obj,min,Double.MAX_VALUE,lang,patterns);
	}

	/**
	 * @Title: checkRangeAndAssignDouble
	 * @Description: 针对传入的参数进行校验--->Double类型
	 * @Detail: 限定数值的最大值,超出最大值的会报异常,如果一切都解析正常,说明解析没有问题,结果将解析后的数值以double类型返回
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验和赋值同时进行
	 * @return double
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final double checkMinAndAssignDouble(final Double doubleValue, double min,final String...patterns) {
		return checkRangeAndAssignDouble(doubleValue,min,Double.MAX_VALUE,patterns);
	}
	public static final double checkMinAndAssignDouble(final Double doubleValue, double min,int lang,final String...patterns) {
		return checkRangeAndAssignDouble(doubleValue,min,Double.MAX_VALUE,lang,patterns);
	}

	/**
	 * @Title: checkRangeAndAssignDouble
	 * @Description: 针对传入的参数进行校验--->Double类型
	 * @Detail: 参数类型虽然是Object,但是要求数据类型是String或者是Double类型的数据,而且String类型还必须是能被解析成double的,否则会抛出异常.
	 * 			而且还要限定数值的最大值,超出最大值的也会报异常.
	 * 			如果一切都解析正常,说明解析没有问题,结果将解析后的数值以double类型返回
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验和赋值同时进行
	 * @return double
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final double checkMaxAndAssignDouble(final Object obj,double max,final String...patterns) {
		return checkRangeAndAssignDouble(obj,Double.MIN_VALUE,max,patterns);
	}
	public static final double checkMaxAndAssignDouble(final Object obj,double max,int lang ,final String...patterns) {
		return checkRangeAndAssignDouble(obj,Double.MIN_VALUE,max,lang,patterns);
	}

	/**
	 * @Title: checkRangeAndAssignDouble
	 * @Description: 针对传入的参数进行校验--->Double类型
	 * @Detail: 限定数值的最大值,超出最大值的会报异常,如果一切都解析正常,说明解析没有问题,结果将解析后的数值以double类型返回
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验和赋值同时进行
	 * @return double
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final double checkMaxAndAssignDouble(final Double doubleValue,double max,final String...patterns) {
		return checkRangeAndAssignDouble(doubleValue,Double.MIN_VALUE,max,patterns);
	}
	public static final double checkMaxAndAssignDouble(final Double doubleValue,double max,int lang ,final String...patterns) {
		return checkRangeAndAssignDouble(doubleValue,Double.MIN_VALUE,max,lang ,patterns);
	}

	/**
	 * @Title: checkRangeAndAssignDouble
	 * @Description: 针对传入的参数进行校验--->Double类型
	 * @Detail: 参数类型虽然是Object,但是要求数据类型是String或者是Double类型的数据,而且String类型还必须是能被解析成double的,否则会抛出异常.
	 * 			而且还要限定数值的最大值,超出最大值的也会报异常.
	 * 			如果一切都解析正常,说明解析没有问题,结果将解析后的数值以double类型返回
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验和赋值同时进行
	 * @return double
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final double checkAndAssignDouble(final Object obj, final String...patterns) {
		return checkRangeAndAssignDouble(obj,Double.MIN_VALUE,Double.MAX_VALUE,patterns);
	}
	public static final double checkAndAssignDouble(final Object obj, int lang, final String...patterns) {
		return checkRangeAndAssignDouble(obj,Double.MIN_VALUE,Double.MAX_VALUE,lang,patterns);
	}

	/**
	 * @Title: checkRangeAndAssignDouble
	 * @Description: 针对传入的参数进行校验--->Double类型
	 * @Detail: 限定数值的最大值,超出最大值的会报异常,如果一切都解析正常,说明解析没有问题,结果将解析后的数值以double类型返回
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验和赋值同时进行
	 * @return double
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final double checkAndAssignDouble(final Double doubleValue, final String...patterns) {
		return checkRangeAndAssignDouble(doubleValue,Double.MIN_VALUE,Double.MAX_VALUE,patterns);
	}
	public static final double checkAndAssignDouble(final Double doubleValue, int lang, final String...patterns) {
		return checkRangeAndAssignDouble(doubleValue,Double.MIN_VALUE,Double.MAX_VALUE,lang,patterns);
	}

	/**
	 * @Title: range
	 * @Description: 针对传入的参数进行校验--->Long类型
	 * @Detail: 参数类型虽然是Object,但是要求数据类型是String或者是Long类型的数据,而且String类型还必须是能被解析成整数的,否则会抛出异常.
	 * 			而且还要限定数值的范围,超出范围的也会报异常.
	 * 			如果一切都解析正常,说明解析没有问题,结果将解析后的数值以long类型返回
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验和赋值同时进行
	 * @return long
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final long checkRangeAndAssignLong(final Object obj, long min, long max, int lang, final String...patterns) {
		String number = null;
		try {
			if (obj == null) {
				throw new NullPointerException();
			}
			number = String.class.cast(obj);
			try {
				Long num = Long.parseLong(number);
				if (num < min) {
					ExceptionUtils.throwException(ErrorMsg.GREATER_THAN_OR_EQUAL,lang,min);
				}
				if(num > max){
					ExceptionUtils.throwException(ErrorMsg.LESS_THAN_OR_EQUAL,lang,max);
				}
				if(patterns.length!=0){//格式校验
					for (String pattern : patterns) {
						if(number.matches(pattern)){
							return num;
						}
					}
					ExceptionUtils.throwException(ErrorMsg.WRONG_FORMAT,lang);
				}
				return num;
			} catch (NumberFormatException e) {
				throw new NumberFormatException();
			}
		} catch (Exception e) {
			if (e instanceof NullPointerException) {
				ExceptionUtils.throwException(ErrorMsg.NULL_POINT,lang);
			}
			if (e instanceof NumberFormatException) {
				ExceptionUtils.throwException(ErrorMsg.NUMBER_PARSE_EXCEPTION,lang);
			}
			if (e instanceof OutOfRangeException) {
				throw new OutOfRangeException(e.getMessage());
			}
			Long num = null;
			try {
				num=Long.parseLong(String.valueOf(obj));
			} catch (Exception e1) {
				ExceptionUtils.throwException(ErrorMsg.NUMBER_PARSE_EXCEPTION,lang);
			}
			if (num < min) {
				ExceptionUtils.throwException(ErrorMsg.GREATER_THAN_OR_EQUAL,lang,min);
			}
			if(num > max){
				ExceptionUtils.throwException(ErrorMsg.LESS_THAN_OR_EQUAL,lang,max);
			}
			if(patterns.length!=0){//格式校验
				for (String pattern : patterns) {
					if(String.valueOf(obj).matches(pattern)){
						return num;
					}
				}
				ExceptionUtils.throwException(ErrorMsg.WRONG_FORMAT,lang);
			}
			return num;
		}
	}

	public static final long checkRangeAndAssignLong(final Object obj, long min, long max, final String...patterns) {
		return checkRangeAndAssignLong(obj,min,max,LangMark.CN,patterns);
	}
	
	/**
	 * @Title: range
	 * @Description: 针对传入的参数进行校验--->Long类型
	 * @Detail: 限定数值的范围,超出范围的会报异常,如果一切都解析正常,说明解析没有问题,结果将解析后的数值以long类型返回
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验和赋值同时进行
	 * @return long
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final long checkRangeAndAssignLong(final Long longValue, long min, long max, int lang,final String...patterns) {
		if (longValue < min) {
			ExceptionUtils.throwException(ErrorMsg.GREATER_THAN_OR_EQUAL,lang,min);
		}
		if(longValue > max){
			ExceptionUtils.throwException(ErrorMsg.LESS_THAN_OR_EQUAL,lang,max);
		}
		if(patterns.length!=0){//格式校验
			for (String pattern : patterns) {
				if(String.valueOf(longValue).matches(pattern)){
					return longValue;
				}
			}
			ExceptionUtils.throwException(ErrorMsg.WRONG_FORMAT,lang);
		}
		return longValue;
	}

	public static final long checkRangeAndAssignLong(final Long longValue, long min, long max, final String...patterns) {
		return checkRangeAndAssignLong(longValue,min,max,LangMark.CN,patterns);
	}
	
	/**
	 * @Title: range
	 * @Description: 针对传入的参数进行校验--->Long类型
	 * @Detail: 参数类型虽然是Object,但是要求数据类型是String或者是Long类型的数据,而且String类型还必须是能被解析成整数的,否则会抛出异常.
	 * 			而且还要限定数值的最小值,小于最小值的也会报异常.
	 * 			如果一切都解析正常,说明解析没有问题,结果将解析后的数值以long类型返回
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验和赋值同时进行
	 * @return long
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final long checkMinAndAssignLong(final Object obj, long min, final String...patterns) {
		return checkRangeAndAssignLong(obj,min,Long.MAX_VALUE,patterns);
	}
	public static final long checkMinAndAssignLong(final Object obj, long min, int lang ,final String...patterns) {
		return checkRangeAndAssignLong(obj,min,Long.MAX_VALUE,lang,patterns);
	}

	/**
	 * @Title: range
	 * @Description: 针对传入的参数进行校验--->Long类型
	 * @Detail: 限定数值的最小值,小于最小值的会报异常,如果一切都解析正常,说明解析没有问题,结果将解析后的数值以long类型返回
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验和赋值同时进行
	 * @return long
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final long checkMinAndAssignLong(final Long longValue, long min, final String...patterns) {
		return checkRangeAndAssignLong(longValue,min,Long.MAX_VALUE,patterns);
	}
	public static final long checkMinAndAssignLong(final Long longValue, long min,int lang, final String...patterns) {
		return checkRangeAndAssignLong(longValue,min,Long.MAX_VALUE,lang,patterns);
	}

	/**
	 * @Title: range
	 * @Description: 针对传入的参数进行校验--->Long类型
	 * @Detail: 参数类型虽然是Object,但是要求数据类型是String或者是Long类型的数据,而且String类型还必须是能被解析成整数的,否则会抛出异常.
	 * 			而且还要限定数值的最大值,大于最大值的也会报异常.
	 * 			如果一切都解析正常,说明解析没有问题,结果将解析后的数值以long类型返回
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验和赋值同时进行
	 * @return long
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final long checkMaxAndAssignLong(final Object obj, long max, final String...patterns) {
		return checkRangeAndAssignLong(obj,Long.MIN_VALUE,max,patterns);
	}
	public static final long checkMaxAndAssignLong(final Object obj, long max, int lang,final String...patterns) {
		return checkRangeAndAssignLong(obj,Long.MIN_VALUE,max,lang,patterns);
	}

	/**
	 * @Title: range
	 * @Description: 针对传入的参数进行校验--->Long类型
	 * @Detail: 限定数值的最大值,大于最大值的会报异常,如果一切都解析正常,说明解析没有问题,结果将解析后的数值以long类型返回
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验和赋值同时进行
	 * @return long
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final long checkMaxAndAssignLong(final Long longValue, long max, final String...patterns) {
		return checkRangeAndAssignLong(longValue,Long.MIN_VALUE,max,patterns);
	}
	public static final long checkMaxAndAssignLong(final Long longValue, long max,int lang, final String...patterns) {
		return checkRangeAndAssignLong(longValue,Long.MIN_VALUE,max,lang,patterns);
	}

	/**
	 * @Title: range
	 * @Description: 针对传入的参数进行校验--->Long类型
	 * @Detail: 参数类型虽然是Object,但是要求数据类型是String或者是Long类型的数据,而且String类型还必须是能被解析成整数的,否则会抛出异常.
	 * 			这个针对Long范围内类型数据的校验和赋值
	 * 			如果一切都解析正常,说明解析没有问题,结果将解析后的数值以long类型返回
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验和赋值同时进行
	 * @return long
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static long checkAndAssignLong(final Object obj,String...patterns) {
		return checkRangeAndAssignLong(obj,Long.MIN_VALUE,Long.MAX_VALUE,patterns);
	}
	public static long checkAndAssignLong(final Object obj,int lang,String...patterns) {
		return checkRangeAndAssignLong(obj,Long.MIN_VALUE,Long.MAX_VALUE,lang,patterns);
	}

	/**
	 * @Title: range
	 * @Description: 针对传入的参数进行校验--->Long类型
	 * @Detail: 针对Long范围内类型数据的校验和赋值,如果一切都解析正常,说明解析没有问题,结果将解析后的数值以long类型返回
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @Usage: 适用于数据判断的校验和赋值同时进行
	 * @return long
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final long checkAndAssignLong(final Long longValue, final String...patterns) {
		return checkRangeAndAssignLong(longValue,Long.MIN_VALUE,Long.MAX_VALUE,patterns);
	}
	public static final long checkAndAssignLong(final Long longValue,int lang ,  final String...patterns) {
		return checkRangeAndAssignLong(longValue,Long.MIN_VALUE,Long.MAX_VALUE,lang,patterns);
	}

	/**
	 * @Title: range
	 * @Target: 针对空(null)
	 * @Description: TODO(针对传入的参数进行非空(null)校验并返回string数据类型--->只针对String类型，校验和赋值同时进行，同时返回真实的String值)
	 * @Attention: 保证传入的数据是字符串，且不能为空(null)，否则报异常，正常就返回一个正常的String类型的参数
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @return String
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final String checkNullAndAssignString(final Object obj, int lang,final String...patterns){
		try {
			if (obj == null) {
				throw new NullPointerException();
			}
			String str=String.class.cast(obj);
			if(patterns.length!=0){//格式校验
				for (String pattern : patterns) {
					if(str.matches(pattern)){
						return str;
					}
				}
				ExceptionUtils.throwException(ErrorMsg.WRONG_FORMAT,lang);
			}
			return str;
		} catch (Exception e) {
			if(e instanceof NullPointerException){
				ExceptionUtils.throwException(ErrorMsg.NULL_POINT,lang);
			}
			if(e instanceof ClassCastException){
				ExceptionUtils.throwException(ErrorMsg.CAST_STRING_EXCEPTION,lang);
			}
			throw new RuntimeException();
		}
	}

	public static final String checkNullAndAssignString(final Object obj, final String...patterns){
		return checkNullAndAssignString(obj,LangMark.CN,patterns);
	}
	
	/**
	 * @Title: checkEmptyAndAssignString
	 * @Target: 针对空(null)和空字符串("")
	 * @Description: TODO(针对传入的参数进行非空(null)和空字符串("")校验并返回string数据类型--->只针对String类型，校验和赋值同时进行，同时返回真实的String值)
	 * @Attention: 保证传入的数据是字符串，且不能为空(null)和空字符串("")，否则报异常，正常就返回一个正常的String类型的参数
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @return String
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final String checkEmptyAndAssignString(final Object obj, int lang ,final String...patterns){
		try {
			if (obj == null) {
				throw new NullPointerException();
			}
			String str=String.class.cast(obj);
			if("".equals(str)){
				ExceptionUtils.throwException(ErrorMsg.EMPTY_STRING,lang);
			}
			if(patterns.length!=0){//格式校验
				for (String pattern : patterns) {
					if(str.matches(pattern)){
						return str;
					}
				}
				ExceptionUtils.throwException(ErrorMsg.WRONG_FORMAT,lang);
			}
			return str;
		} catch (Exception e) {
			if(e instanceof NullPointerException){
				ExceptionUtils.throwException(ErrorMsg.NULL_POINT,lang);
			}
			if(e instanceof ClassCastException){
				ExceptionUtils.throwException(ErrorMsg.CAST_STRING_EXCEPTION,lang);
			}
			throw new RuntimeException(e.getMessage());
		}
	}

	public static final String checkEmptyAndAssignString(final Object obj ,final String...patterns){
		return checkEmptyAndAssignString(obj,LangMark.CN,patterns);
	}
	
	/**
	 * @Title: checkBlankAndAssignString
	 * @target: 针对空(null)和空字符串("")和空内容的字符串("   ")
	 * @Description: TODO(针对传入的参数进行非空(null)和空字符串("")和空内容的字符串("   ")校验并返回string数据类型--->只针对String类型，校验和赋值同时进行，同时返回真实的String值)
	 * @Attention: 保证传入的数据是字符串，且不能为空(null)和空字符串("")和空内容的字符串("   ")，否则报异常，正常就返回一个正常的String类型的参数
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @return String
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final String checkBlankAndAssignString(final Object obj,int lang,final String...patterns){
		try {
			if (obj == null) {
				ExceptionUtils.throwException(ErrorMsg.NULL_POINT,lang);
			}
			String str=String.class.cast(obj);
			if("".equals(str.trim())){
				ExceptionUtils.throwException(ErrorMsg.EMPTY_STRING,lang);
			}
			if(patterns.length != 0){//格式校验
				for (String pattern : patterns) {
					if(str.matches(pattern)){
						return str;
					}
				}
				ExceptionUtils.throwException(ErrorMsg.WRONG_FORMAT,lang);
			}
			return str;
		} catch (Exception e) {
			if(e instanceof NullPointerException){
				ExceptionUtils.throwException(ErrorMsg.NULL_POINT,lang);
			}
			if(e instanceof ClassCastException){
				ExceptionUtils.throwException(ErrorMsg.CAST_STRING_EXCEPTION,lang);
			}
			throw new RuntimeException(e.getMessage());
		}
	}

	public static final String checkBlankAndAssignString(final Object obj,final String...patterns){
		return checkBlankAndAssignString(obj,LangMark.CN,patterns);
	}
	
	
	/**
	 * @Title: checkBlankStringAndAssignNullIfIsBlank
	 * @Description: TODO(针对传入的Object类型参数进行非空(null)和空字符串("")和空内容的字符串("   ")校验并返回string数据类型,只要是null,""或"  "全部返回null,不会抛出异常，除非无法强转为String,其它情况正常转String)
	 * @Attention: 默认返回null
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @return String
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final String checkBlankStringAndAssignNullIfIsBlank(final Object obj,int lang, final String...patterns){
		try {
			if (obj == null) {
				return null;
			}
			String str=String.class.cast(obj);
			if("".equals(str.trim())){
				return null;
			}
			if(patterns.length!=0){//格式校验
				for (String pattern : patterns) {
					if(str.matches(pattern)){
						return str;
					}
				}
				ExceptionUtils.throwException(ErrorMsg.WRONG_FORMAT,lang);
			}
			return str;
		} catch (Exception e) {
			ExceptionUtils.throwException(ErrorMsg.CAST_STRING_EXCEPTION,lang);
			return null;
		}
	}

	public static final String checkBlankStringAndAssignNullIfIsBlank(final Object obj, final String...patterns){
		return checkBlankStringAndAssignNullIfIsBlank(obj,LangMark.CN,patterns);
	}
	
	/**
	 * @Title: checkBlankStringAndAssignEmptyIfIsBlank
	 * @Description: TODO(针对传入的参数进行非空(null)和空字符串("")和空内容的字符串("   ")校验并返回string数据类型,只要是null,""或"  "全部返回""空串,不会抛出异常，除非无法强转为String,其它情况正常转String)
	 * @Attention: 默认返回""
	 * @AddFuntion:增加多格式校验功能[2018/1/27 10:48AM]
	 * @return String
	 * @author ylx
	 * @date 2017年12月26日 下午3:48:40
	 */
	public static final String checkBlankStringAndAssignEmptyIfIsBlank(final Object obj, int lang ,final String...patterns){
		try {
			if (obj == null) {
				return "";
			}
			String str=String.class.cast(obj);
			if("".equals(str.trim())){
				return "";
			}
			if(patterns.length!=0){//格式校验
				for (String pattern : patterns) {
					if(str.matches(pattern)){
						return str;
					}
				}
				ExceptionUtils.throwException(ErrorMsg.WRONG_FORMAT,lang);
			}
			return str;
		} catch (Exception e) {
			ExceptionUtils.throwException(ErrorMsg.CAST_STRING_EXCEPTION,lang);
			return null;
		}
	}

	public static final String checkBlankStringAndAssignEmptyIfIsBlank(final Object obj, final String...patterns){
		return checkBlankStringAndAssignEmptyIfIsBlank(obj,LangMark.CN,patterns);
	}
	
	/**
	 * 
	* @Title: checkAndAssignDefaultInt
	* @Description: TODO(如果校验的Object不能转换Integer则抛出异常，如果是null，空字符串或者是长空串，统一返回用户指定的默认值defaultInt)
	* @Attention: obj为null，空字符串""或者是长空串"   "都不会抛异常，而是直接返回默认值
	* @return int    返回类型
	* @author ylx
	* @date 2017年12月27日 下午1:51:43
	 */
	public static final int checkAndAssignDefaultInt(final Object obj,int lang ,int defaultInt){
		String number = null;
		try {
			if (obj == null) {
				return defaultInt;
			}
			number = String.class.cast(obj);
			if("".equals(number.trim())){
				return defaultInt;
			}
			try {
				return Integer.parseInt(number.trim());
			} catch (NumberFormatException e) {
				throw new NumberFormatException();
			}
		} catch (Exception e) {
			if (e instanceof NumberFormatException) {
				ExceptionUtils.throwException(ErrorMsg.NUMBER_PARSE_EXCEPTION,lang);
			}
			Integer num = null;
			try {
				num = Integer.class.cast(obj);
			} catch (Exception e1) {
				ExceptionUtils.throwException(ErrorMsg.CAST_INTEGER_EXCEPTION,lang);
			}
			return num;
		}
	}

	public static final int checkAndAssignDefaultInt(final Object obj,int defaultInt){
		return checkAndAssignDefaultInt(obj,LangMark.CN,defaultInt);
	}
	
	
	/**
	 * 
	* @Title: checkAndAssignDefaultLong
	* @Description: TODO(如果校验的Object不能转换Long则抛出异常，如果是null，空字符串或者是长空串，统一返回用户指定的默认值defaultLong)
	* @Attention: obj为null，空字符串""或者是长空串"   "都不会抛异常，而是直接返回默认值
	* @Solved: 这里要特别注意下，Long.class.cast(obj)当obj是Integer时会出现异常，包装类不能像基本类型一样强转，所以这里改成num=Long.parseLong(""+obj);
	* @return long    返回类型
	* @author ylx
	* @date 2017年12月27日 下午1:51:43
	 */
	public static final long checkAndAssignDefaultLong(final Object obj,int lang,final long defaultLong){
		String number = null;
		try {
			if (obj == null) {
				return defaultLong;
			}
			number = String.class.cast(obj);
			if("".equals(number.trim())){
				return defaultLong;
			}
			try {
				return Long.parseLong(number.trim());
			} catch (NumberFormatException e) {
				throw new NumberFormatException();
			}
		} catch (Exception e) {
			if (e instanceof NumberFormatException) {
				ExceptionUtils.throwException(ErrorMsg.NUMBER_PARSE_EXCEPTION,lang);
			}
			Long num = null;
			try {
				num=Long.parseLong(String.valueOf(obj));
			} catch (Exception e1) {
				ExceptionUtils.throwException(ErrorMsg.CAST_LONG_EXCEPTION,lang);
			}
			return num;
		}
	}

	public static final long checkAndAssignDefaultLong(final Object obj,final long defaultLong){
		return checkAndAssignDefaultLong(obj,LangMark.CN,defaultLong);
	}
	
	/**
	 * 
	* @Title: checkAndAssignNullIntegerIfIsBlank
	* @Description: TODO(如果校验的Object不能转换Long则抛出异常，如果是null，空字符串或者是长空串，统一返回用户null
	* 				checkAndAssignInt的增强版(对传过来的空串等一系列问题进行扩展处理)
	* @Attention: obj为null，空字符串""或者是长空串"   "都不会抛异常，而是直接返回默认值null
	* @return Integer    返回类型
	* @author ylx
	* @date 2017年12月27日 下午1:51:43
	 */
	public static final Integer checkAndAssignNullIntegerIfIsBlank(final Object obj,int lang){
		String number = null;
		try {
			if (obj == null) {
				return null;
			}
			number = String.class.cast(obj);
			if("".equals(number.trim())){
				return null;
			}
			try {
				return Integer.parseInt(number.trim());
			} catch (NumberFormatException e) {
				throw new NumberFormatException();
			}
		} catch (Exception e) {
			if (e instanceof NumberFormatException) {
				ExceptionUtils.throwException(ErrorMsg.NUMBER_PARSE_EXCEPTION,lang);
			}
			Integer num = null;
			try {
				num=Integer.parseInt(String.valueOf(obj));
			} catch (Exception e1) {
				ExceptionUtils.throwException(ErrorMsg.CAST_INTEGER_EXCEPTION,lang);
			}
			return num;
		}
	}

	public static final Integer checkAndAssignNullIntegerIfIsBlank(final Object obj){
		return checkAndAssignNullIntegerIfIsBlank(obj,LangMark.CN);
	}
	
	/**
	 * 
	* @Title: checkNullString
	* @Description: TODO(检测字符串是否为null，否则就报错，如果传入格式要求还要符合传入的格式要求，不然也报错)
	* @param @param obj
	* @param @param patterns    设定文件
	* @return void    返回类型
	* @author ylx
	* @date 2018年1月29日 上午11:43:30
	* @throws
	 */
	public static final void checkNullString(Object obj,int lang,String...patterns){
		try {
			String str=String.class.cast(obj);
			if(null==str) {
				ExceptionUtils.throwException(ErrorMsg.EMPTY_STRING,lang);
			}
			if(patterns.length!=0){//格式校验
				for (String pattern : patterns) {
					if(str.matches(pattern)){
						return;
					}
				}
				ExceptionUtils.throwException(ErrorMsg.WRONG_FORMAT,lang);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public static final void checkNullString(Object obj,String...patterns){
		checkNullString(obj,LangMark.CN,patterns);
	}
	
	/**
	 * 
	* @Title: checkEmptyString
	* @Description: TODO(检测字符串是否为null/""，否则就报错，如果传入格式要求还要符合传入的格式要求，不然也报错)
	* @param @param obj
	* @param @param patterns    设定文件
	* @return void    返回类型
	* @author ylx
	* @date 2018年1月29日 上午11:43:30
	* @throws
	 */
	public static final void checkEmptyString(Object obj,int lang ,String...patterns){
		try {
			String str=String.class.cast(obj);
			if(null==str) {
				ExceptionUtils.throwException(ErrorMsg.EMPTY_STRING,lang);
			}
			if("".equals(str)) {
				ExceptionUtils.throwException(ErrorMsg.EMPTY_STRING,lang);
			}
			//格式校验
			if(patterns.length!=0){
				for (String pattern : patterns) {
					if(str.matches(pattern)){
						return;
					}
				}
				ExceptionUtils.throwException(ErrorMsg.WRONG_FORMAT,lang);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public static final void checkEmptyString(Object obj,String...patterns){
		checkEmptyString(obj,LangMark.CN,patterns);
	}

	/**
	 * 
	* @Title: checkBlankString
	* @Description: TODO(检测字符串是否为null/""/"  "，否则就报错，如果传入格式要求还要符合传入的格式要求，不然也报错)
	* @param @param obj
	* @param @param patterns    设定文件
	* @return void    返回类型
	* @author ylx
	* @date 2018年1月29日 上午11:43:30
	* @throws
	 */
	public static final void checkBlankString(Object obj,int lang,String...patterns){
		try {
			String str=String.class.cast(obj);
			if (null==str) {
				ExceptionUtils.throwException(ErrorMsg.EMPTY_STRING,lang);
			}
			if ("".equals(str.trim())) {
				ExceptionUtils.throwException(ErrorMsg.EMPTY_STRING,lang);
			}
			//格式校验
			if(patterns.length!=0){
				for (String pattern : patterns) {
					if(str.matches(pattern)){
						return;
					}
				}
				ExceptionUtils.throwException(ErrorMsg.WRONG_FORMAT,lang);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public static final void checkBlankString(Object obj,String...patterns){
		checkBlankString(obj,LangMark.CN,patterns);
	}
	
	/**
	 * 
	 * @param //如果传入的integer为Null，报错
	 */
	public static final void checkNullInteger(Integer integer,int lang){
		if(integer==null){
			ExceptionUtils.throwException(ErrorMsg.NULL_INTEGER,lang);
		}
	}
	public static final void checkNullInteger(Integer integer){
		checkNullInteger(integer,LangMark.CN);
	}

	/**
	 * 根据传入进来的对象进行解析，如果不能转换成Integer类型，则统一返回defaultInt
	 * @param obj
	 * @param defaultInt
	 * @return
	 */
	public static final Integer getInteger(Object obj,Integer defaultInt){
		try {
			int i = Integer.parseInt((String) obj);
			return i;
		}catch(Exception e){
			return defaultInt;
		}
	}
	
	
	public static void main(String[] args) {
		System.out.println(checkAndAssignNullIntegerIfIsBlank(" "));
	}
	
	
}

class OutOfRangeException extends RuntimeException{
	public OutOfRangeException() {}
	public OutOfRangeException(String message) {
		super(message);
	}
	@Override
	public String getMessage() {
		return super.getMessage();
	}
}
