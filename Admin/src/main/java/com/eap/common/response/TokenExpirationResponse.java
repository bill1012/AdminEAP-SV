package com.eap.common.response;

import com.eap.common.constant.RestCodeConstants;

/**
 * @author billjiang 475572229@qq.com
 * @create 17-9-21
 */
public class TokenExpirationResponse extends BaseResponse {
    public TokenExpirationResponse(String message) {
        super(RestCodeConstants.TOKEN_ERROR_CODE, message);
    }
}
