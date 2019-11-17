package com.eap.common.response;

/**
 * @author billjiang 475572229@qq.com
 * @create 17-9-21
 */
public class BaseResponse {
    private int status=200;
    private String message;

    public BaseResponse(int status,String message){
        this.status=status;
        this.message=message;
    }

    public BaseResponse(){

    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
