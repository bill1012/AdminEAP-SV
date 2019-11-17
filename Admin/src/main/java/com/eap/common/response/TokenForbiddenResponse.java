package com.eap.common.response;

import com.eap.common.constant.RestCodeConstants;

/**
 * @author billjiang 475572229@qq.com
 * @create 17-9-21
 */
public class TokenForbiddenResponse extends BaseResponse {
    public TokenForbiddenResponse(String message) {
        super(RestCodeConstants.TOKEN_FORBIDDEN_CODE, message);
    }
}
