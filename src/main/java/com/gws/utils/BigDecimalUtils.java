/*
 * All rights Reserved, Designed By 趣玩棋牌 2016年12月16日 上午9:30:59
 */

package com.gws.utils;

import java.math.BigDecimal;

/**
 * 金额转换 处理
 * 
 * @author: Rayping
 */
public class BigDecimalUtils {
    
    private BigDecimalUtils(){
        throw new Error("工具类不能实例化！");
    }

    /**
     * 可变长度的加法
     * 
     * @param val1
     * @param decimals
     * @return
     */
    public static BigDecimal add(BigDecimal val1, BigDecimal... decimals) {

        if (isNull(val1)) {
            val1 = new BigDecimal(0);
        }
        BigDecimal sum = val1;
        for (BigDecimal decimal : decimals) {
            if (isNull(decimal)) {
                decimal = new BigDecimal(0);
            }
            sum = sum.add(decimal);
        }

        return sum;
    }


    /**
     * 可变长度的减法
     * 
     * @param val1
     * @param decimals
     * @return
     */
    public static BigDecimal subtract(BigDecimal val1, BigDecimal... decimals) {

        if (isNull(val1)) {
            val1 = new BigDecimal(0);
        }
        BigDecimal subtract = val1;
        for (BigDecimal decimal : decimals) {
            if (isNull(decimal)) {
                decimal = new BigDecimal(0);
            }
            subtract = subtract.subtract(decimal);
        }
        return subtract;
    }

    /**
	 * 除 val1 / val2
	 * 
	 * @param val1
	 * @param val2
	 * @param scale
	 *            scale of the BigDecimal quotient to be returned
	 * @param roundingMode
	 *            表示小数精确的位置
	 * @return
	 */
    public static BigDecimal divide(BigDecimal val1, BigDecimal val2, int scale, int roundingMode) {

        if (isNull(val1) || isZero(val1) || isNull(val2) || isZero(val2)) {
            return new BigDecimal(0);
        }
        return val1.divide(val2, scale, roundingMode);
    }

    /**
     * 可变长度乘法
     * 
     * @param val1
     * @param decimals
     * @return
     */
    public static BigDecimal multiply(BigDecimal val1, BigDecimal... decimals) {

        if (isNull(val1)) {
            val1 = new BigDecimal(0);
        }
        BigDecimal result = val1;
        for (BigDecimal decimal : decimals) {
            if (isNull(decimal)) {
                decimal = new BigDecimal(0);
            }
            result = result.multiply(decimal);
        }
        return result;
    }

    /**
     * 判断是否为null
     * 
     * @param val
     * @return
     */
    public static boolean isNull(BigDecimal val) {
        return null == val;
    }

    public static BigDecimal nullToZero(BigDecimal val) {

        if (isNull(val)) {
            return new BigDecimal(0);
        }
        return val;
    }

    /**
     * 判断是否为0
     * 
     * @param val
     * @return
     */
    public static boolean isZero(BigDecimal val) {
        if (isNull(val)) {
            return true;
        }
        return val.compareTo(new BigDecimal(0)) == 0;
    }
}
