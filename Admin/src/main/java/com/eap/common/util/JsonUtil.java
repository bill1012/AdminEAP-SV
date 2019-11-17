package com.eap.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonUtil {
    public static String hello(){
        return  "hello";
    }
    private static ObjectMapper mapper;
    static {
        mapper = new ObjectMapper();
    }

    /**
     * 转换成Json
     * @param object
     * @return 返回json字符串
     */
    public static String ObjectToJson(Object object){
        String res = null;
        try {
            res = mapper.writeValueAsString(object);
        } catch (IOException e) {
            System.out.println("Json转换异常");
        }
        return  res;
    }
}
