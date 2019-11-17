package com.eap.common.jwt;

import io.undertow.util.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @Auther: sharps
 * @Date: 19-6-27 15:40
 * @Description: 获取当前登录的用户名
 */
public class SecurityContextHolder {
    public static UserDetails getUserDetails() {
        UserDetails userDetails = null;
        userDetails = (UserDetails) org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails;
    }
}
