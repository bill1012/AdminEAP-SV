package com.eap.admin.service;

import com.eap.admin.entity.User;
import com.eap.admin.vo.UserVo;
import com.eap.common.config.JwtConfig;
import com.eap.common.constant.BaseConstants;
import com.eap.common.constant.UserConstant;
import com.eap.common.jwt.JwtTokenUtil;
import com.eap.common.vo.PermissionInfo;
import com.eap.common.vo.UserInfo;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author 鬼王
 * @Date 2018/04/18 13:45
 */
@Service
public class AuthService {


    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    @Autowired
    private UserService userService;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    public String login(String username, String password) {
        User info = userService.getUserByUsername(username);
        if (info.getId().equals(UserConstant.USER_NOT_EXIST)) {
            return UserConstant.USER_NOT_EXIST;
        }
        String token = "";
        if (encoder.matches(password, info.getPassword())) {
            Object old = redisTemplate.opsForValue().get(info.getUsername());
            if (old == null) {
                Map<String, Object> claims = new HashMap<>();
                claims.put(jwtConfig.getKeyUsername(), info.getUsername());
                claims.put(jwtConfig.getKeyCreated(), new Date());
                token = JwtTokenUtil.generateToken(claims, jwtConfig.getSecert(), jwtConfig.getExpiration());
            } else {
                token = old.toString();
                if (JwtTokenUtil.isTokenExpired(token, jwtConfig.getSecert())) {
                    token = refresh(token);
                }
            }
        }
        return token;
    }

    public String refresh(String token) {
        String username = JwtTokenUtil.getUsernameFromToken(token, jwtConfig.getSecert());
        UserInfo info = userService.getUserInfoByUsername(username);
        if (JwtTokenUtil.canTokenBeRefreshed(token, jwtConfig.getSecert(), jwtConfig.getKeyCreated(), info.getUpdateTime())) {
            return JwtTokenUtil.refreshToken(token, jwtConfig.getSecert(), jwtConfig.getKeyCreated(), info.getUpdateTime(), jwtConfig.getExpiration());
        }
        return null;
    }

    public Boolean validate(String token, String resource) {
        String username = JwtTokenUtil.getUsernameFromToken(token, jwtConfig.getSecert());
        UserInfo info = userService.getUserInfoByUsername(username);
        return info.getUsername().equals(username) && !JwtTokenUtil.isTokenExpired(token, jwtConfig.getSecert()) && validateResource(username, resource);
    }

    public UserVo getUserInfo(String token) {
        String username = JwtTokenUtil.getUsernameFromToken(token, jwtConfig.getSecert());
        if (username == null)
            return null;
        UserInfo user = userService.getUserInfoByUsername(username);
        UserVo uservo = new UserVo();
        BeanUtils.copyProperties(user, uservo);
        List<PermissionInfo> permissionInfos = userService.getPermissionByUsername(username);
        Stream<PermissionInfo> menus = permissionInfos.parallelStream().filter((permission) -> {
            return !BaseConstants.RESOURCE_TYPE_ELEMENT.equals(permission.getType());
        });
        uservo.setMenus(menus.collect(Collectors.toList()));
        Stream<PermissionInfo> elements = permissionInfos.parallelStream().filter((permission) -> {
            return BaseConstants.RESOURCE_TYPE_ELEMENT.equals(permission.getType());
        });
        uservo.setElements(elements.collect(Collectors.toList()));
        uservo.setRoles(userService.getRoleCodesByUsername(username));
        return uservo;
    }

    public Boolean invalid(String token) {
        String username = JwtTokenUtil.getUsernameFromToken(token, jwtConfig.getSecert());
        return redisTemplate.opsForValue().setIfAbsent(username, null);

    }

    public Boolean validateResource(String username, String resource) {
        String[] res = resource.split(":");
        final String requestUri = res[0];
        final String method = res[1];
        List<PermissionInfo> clientPermissionInfo = userService.getPermissionByUsername(username);
        Collection<PermissionInfo> result = Collections2.filter(clientPermissionInfo, new Predicate<PermissionInfo>() {
            @Override
            public boolean apply(PermissionInfo permissionInfo) {
                String url = permissionInfo.getUri();
                String uri = url.replaceAll("\\{\\*\\}", "[a-zA-Z\\\\d]+");
                String regEx = "^" + uri + "$";
                return (Pattern.compile(regEx).matcher(requestUri).find() || requestUri.startsWith(url + "/"))
                        && method.equals(permissionInfo.getMethod());
            }
        });
        if (result.size() <= 0) {
            return false;
        }
        return true;
    }

}
