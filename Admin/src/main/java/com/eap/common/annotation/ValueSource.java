package com.eap.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 設置Entity屬性默認值
 *
 * @Author: 鬼王
 * @Date: 2018/03/27 09:26
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface ValueSource {

    SourceWay value();

    boolean update() default false;

}
