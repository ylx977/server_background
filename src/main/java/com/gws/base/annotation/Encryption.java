package com.gws.base.annotation;

import java.lang.annotation.*;

/**
 * isDecrypt: 入参是否需要解密
 * isEncrypt: 出参是否需要加密
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Encryption {
    boolean isDecrypt() default true;
    boolean isEncrypt() default true;
}
