package com.eap.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Author: 鬼王
 * @Date: 2018/03/27 10:27
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface ValueInject { }
