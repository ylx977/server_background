package com.gws.utils;

import java.math.BigDecimal;

public class DataFormatUtil {
    /**
     * 四舍五入，保留2位小数
     * @param number
     * @return
     */
    public static BigDecimal round(BigDecimal number) {
        return number.setScale(2, BigDecimal.ROUND_DOWN);
    }
}
