package com.eap.common.constant;

/**
 * @Author 鬼王
 * @Date 2018/04/18 13:04
 */
public class ResponseConstant {

    /**
     * 用户的id
     */
    public static final String USER_ID = "USER_ID";
    /**
     * 登录用户的名称
     */
    public static final String USERNAME = "USERNAME";


    /**
     * AES对称加密密钥
     */
    public static final String AES_SECRET = "PTRkygl@413";

    /**
     * 传递token的key值
     */
    public static final String ACCESS_TOKEN_KEY = "access-token";
    /**
     * 跟新token时候的key值
     */
    public static final String REPLACE_TOKEN_KEY = "REPLACE_TOKEN";

    /**
     * 返回成功 状态200
     */
    public static final int WEB_RESPONSE_STATUES_SUCCESS = 200;


    /**
     * 登录失败  登录名或者密码错误  308001
     */
    public static final int WEB_RESPONSE_STATUES_LOGIN_ERROR = 308001;
    /**
     * TOKEN 错误
     */
    public static final int WEB_RESPONSE_STATUES_TOKEN_ERROR = 308010;

    /**
     * 提示前端跟新token
     */
    public static final int WEB_RESPONSE_STATUES_REPLACE_TOKEN = 308100;

    /**
     * 404
     */
    public static final int WEB_RESPONSE_STATUES_ERROR = 404;

}
