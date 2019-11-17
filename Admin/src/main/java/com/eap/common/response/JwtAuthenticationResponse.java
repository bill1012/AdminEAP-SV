package com.eap.common.response;

import java.io.Serializable;

/**
 * @author billjiang 475572229@qq.com
 * @create 17-9-21
 */
public class JwtAuthenticationResponse implements Serializable {

    private static final long serialVersionUID = 1250166508152483573L;

    private String token;

    private boolean result;

    private String message;

    private int code;

    public JwtAuthenticationResponse(String token) {
        this.token = token;
        this.result = true;
    }

    public JwtAuthenticationResponse(String token, boolean result, String message) {
        this.token = token;
        this.result = result;
        this.message = message;
    }

    public JwtAuthenticationResponse(boolean result, String message) {
        this.result = result;
        this.message = message;
    }

    public JwtAuthenticationResponse(boolean result, String message, int code) {
        this.result = result;
        this.message = message;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }
}
