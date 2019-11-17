package com.eap.common.filter;

import com.alibaba.fastjson.JSON;
import com.eap.admin.service.UserService;
import com.eap.common.config.JwtConfig;
import com.eap.common.constant.BaseConstants;
import com.eap.common.jwt.JwtTokenUtil;
import com.eap.common.response.PermissionForbiddenResponse;
import com.eap.common.response.TokenErrorResponse;
import com.eap.common.response.TokenExpirationResponse;
import com.eap.common.response.TokenForbiddenResponse;
import com.eap.common.util.ClientUtil;
import com.eap.common.vo.LogInfo;
import com.eap.common.vo.PermissionInfo;
import com.eap.common.vo.UserInfo;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Order(1)
@WebFilter(filterName = "accessFilter", urlPatterns = "/*")
public class SessionAccessFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(SessionAccessFilter.class);

    @Value("${environments.security.ignore.startWith}")
    private String startWith;

    @Value("${environments.security.ignore.contains}")
    private String contains;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private UserService userService;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String uri = request.getRequestURI();
        String method=request.getMethod();
        // 不拦截的地址
        if (isStartWith(uri) || isContains(uri)) {
            filterChain.doFilter(request, response);
        } else {
            // 登录校验
            UserInfo user = getJwtUser(request,response);
            String authToken = request.getHeader(this.jwtConfig.getHeader());
            if (user != null) {
                //设置头部校验信息
                request.getSession().setAttribute(BaseConstants.REQUEST_HEADER_UNAME, user.getName());
                request.getSession().setAttribute(BaseConstants.REQUEST_HEADER_UID, user.getId());
                request.getSession().setAttribute(BaseConstants.REQUEST_HEADER_USER, user);
            } else {
                setFailedRequest(response, JSON.toJSONString(new TokenForbiddenResponse("Token Forbidden")), 200);
                return;
            }
            // token 超时 getJwtUser已验证
            /*if(JwtTokenUtil.isTokenExpired(authToken,jwtConfig.getSecert())){
                setFailedRequest(response,JSON.toJSONString(new TokenExpirationResponse("Token 过期，请重新登录")),200);
                return;
            }*/
            List<PermissionInfo> permissionInfos = this.getAllPermissionInfo(request);
            //判断资源是否启用权限约束
            Collection<PermissionInfo> result = getPermissionInfos(uri, method, permissionInfos);

            String host = ClientUtil.getClientIp(request);
            request.getSession().setAttribute(BaseConstants.REQUEST_HEADER_HOST, host);

            if (!result.isEmpty()) {
                if (user != null) {
                    checkAllow(uri, method,  user.getUsername(),request,response);
                    filterChain.doFilter(request, response);
                }else{
                    setFailedRequest(response, JSON.toJSONString(new TokenForbiddenResponse("用户未登录")), 200);
                    return;
                }
            }else {
                // 未启动权限约束
                filterChain.doFilter(request, response);;
            }

            // 授权校验

        }


    }

    @Override
    public void destroy() {

    }

    private void checkAllow(final String requestUri, final String method, String username, HttpServletRequest request,HttpServletResponse response) {
        log.debug("uri：" + requestUri + "----method：" + method);
        List<PermissionInfo> permissionInfos = getPermissionInfos(request, username);
        Collection<PermissionInfo> result = getPermissionInfos(requestUri, method, permissionInfos);
        if (result.isEmpty()) {
            setFailedRequest(response,JSON.toJSONString(new PermissionForbiddenResponse("您没有[" + requestUri + ":" + method + "]操作权限，请联系管理员授权!")), 200);
        } else {
            PermissionInfo[] pms = result.toArray(new PermissionInfo[]{});
            PermissionInfo pm = pms[0];
        }
    }



    private List<PermissionInfo> getPermissionInfos(HttpServletRequest request, String username) {
        List<PermissionInfo> permissionInfos;
        if (request.getSession().getAttribute("permission-user") == null) {
            permissionInfos = userService.getPermissionByUsername(username);
            request.getSession().setAttribute("permission-user", permissionInfos);
        } else {
            permissionInfos = (List<PermissionInfo>) request.getSession().getAttribute("permission-user");
        }
        return permissionInfos;
    }



    private List<PermissionInfo> getAllPermissionInfo(HttpServletRequest request) {
        List<PermissionInfo> permissionInfos;
        if (request.getSession().getAttribute("permission-all") == null) {
            permissionInfos = userService.getAllPermissionInfo();
            request.getSession().setAttribute("permission-all", permissionInfos);
        } else {
            permissionInfos = (List<PermissionInfo>) request.getSession().getAttribute("permission-all");
        }
        return permissionInfos;

    }


    private Collection<PermissionInfo> getPermissionInfos(final String requestUri, final String method, List<PermissionInfo> serviceInfo) {
        return Collections2.filter(serviceInfo, new Predicate<PermissionInfo>() {
            @Override
            public boolean apply(PermissionInfo permissionInfo) {
                String url = permissionInfo.getUri();
                if (StringUtils.isBlank(url)) {
                    return false;
                }
                String uri = url.replaceAll("\\{\\*\\}", "[a-zA-Z\\\\d]+");
                String regEx = "^" + uri + "$";
                return (Pattern.compile(regEx).matcher(requestUri).find() || requestUri.startsWith(url + "/"))
                        && method.equals(permissionInfo.getMethod());
            }
        });
        //return Collections.EMPTY_LIST;
    }


    private void setFailedRequest(HttpServletResponse response,String body, int code) {
        HttpServletResponse httpResponse = response;
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json; charset=utf-8");
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE,PATCH,PUT");
        httpResponse.setHeader("Access-Control-Max-Age", "3600");
        httpResponse.setHeader("Access-Control-Allow-Headers", "Origin,X-Requested-With,x-requested-with,X-Custom-Header," +
                "Content-Type,Accept,Authorization");

        PrintWriter writer = null;
        try {
            writer = httpResponse.getWriter();
            writer.write(body);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            log.error("setFailedRequest关闭异常:" + e.getMessage());
        }
    }


    private boolean isStartWith(String requestUri) {
        boolean flag = false;
        if (StringUtils.isEmpty(startWith))
            return flag;
        for (String s : startWith.split(",")) {
            if (requestUri.startsWith(s))
                return true;
        }
        return flag;
    }


    private boolean isContains(String requestUri) {
        boolean flag = false;
        if (StringUtils.isEmpty(contains))
            return flag;
        for (String s : contains.split(",")) {
            if (requestUri.contains(s))
                return true;
        }
        return flag;
    }

    private UserInfo getJwtUser(HttpServletRequest request,HttpServletResponse response) {
        String authToken = request.getHeader(this.jwtConfig.getHeader());
        if (authToken != null) {
            String username = JwtTokenUtil.getUsernameFromToken(authToken, jwtConfig.getSecert());
            log.info("checking authentication " + username);
            if (username != null) {
                UserInfo userDetails = userService.getUserInfoByUsername(username);
                if (JwtTokenUtil.validateToken(authToken,authToken,jwtConfig.getSecert(),userDetails)) {
                    return userDetails;
                }
            }
            setFailedRequest(response,JSON.toJSONString(new TokenErrorResponse("Token Error or Token Expired!")), 200);
            return null;
        } else {
            return null;
        }
    }


}
