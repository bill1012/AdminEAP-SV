package com.eap.admin.controller;

import com.eap.admin.service.AuthService;
import com.eap.admin.vo.UserVo;
import com.eap.common.constant.BaseConstants;
import com.eap.common.constant.RestCodeConstants;
import com.eap.common.constant.UserConstant;
import com.eap.common.response.JwtAuthenticationResponse;
import com.eap.common.vo.JwtAuthenticationRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author 鬼王
 * @Date 2018/04/18 11:06
 */
@RestController
@RequestMapping("jwt")
public class AuthController {

    @Value("${environments.jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthService authService;


    @RequestMapping(value = "token",method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest){
        final String token=authService.login(authenticationRequest.getUsername(),authenticationRequest.getPassword());
        UserVo userInfo = authService.getUserInfo(token);
        if(userInfo==null || userInfo.getRoles()== null || userInfo.getRoles().size() == 0){
            //登录名错误
            return ResponseEntity.ok(new JwtAuthenticationResponse(false,BaseConstants.MSG_LOGIN_NOROLE_ERROR, RestCodeConstants.USER_NOT_EXIST));
        }
        if(StringUtils.isEmpty(token)){
            //密码错误
            return ResponseEntity.ok(new JwtAuthenticationResponse(false,BaseConstants.MSG_LOGIN_EMPTY, RestCodeConstants.USER_PASSWORD_ERROR));
        }else if(token.equals(UserConstant.USER_NOT_EXIST)){
            //登录名错误
            return ResponseEntity.ok(new JwtAuthenticationResponse(false,BaseConstants.MSG_LOGIN_ERROR, RestCodeConstants.USER_NOT_EXIST));
        }else
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }



    @PostMapping("invalid")
    public ResponseEntity<?> invalid(String token, HttpServletRequest request) {
        request.getSession().invalidate();

        return ResponseEntity.ok(true);
    }

    @GetMapping("user")
    public ResponseEntity<?> user(String token) {
        UserVo userInfo = authService.getUserInfo(token);
        if(userInfo==null)
            return ResponseEntity.status(401).body(false);
        else
            return ResponseEntity.ok(userInfo);
    }

    @RequestMapping(value = "refresh", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(
            HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String refreshedToken = authService.refresh(token);
        if(refreshedToken == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
        }
    }

    @RequestMapping(value = "verify", method = RequestMethod.GET)
    public ResponseEntity<?> verify(String token,String resource){
        if(authService.validate(token,resource))
            return ResponseEntity.ok(true);
        else
            return ResponseEntity.status(401).body(false);
    }

}
