package com.eap.common.annotation;

import com.eap.common.constant.BaseConstants;

import java.util.Date;

/**
 * Entity屬性默認值來源方式: request.getHeader(key)
 *
 * @Author: 鬼王
 * @Date: 2018/03/27 09:31
 */
public enum SourceWay {

    IP(BaseConstants.REQUEST_HEADER_HOST),
    USERID(BaseConstants.REQUEST_HEADER_UID),
    USERNAME(BaseConstants.REQUEST_HEADER_UNAME),
    SYSDATE(Date.class);

    private Object key;

    SourceWay(Object key) {
        this.key = key;
    }

    public Object getKey() {
        return key;
    }

}
