package com.eap.common.jwt;

import com.eap.common.vo.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Map;

/**
 * @Author 鬼王
 * @Date 2018/04/18 13:06
 */
public class JwtTokenUtil {

    public static String generateToken(Map<String, Object> claims, String secret, Integer expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate(expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }


    /**
     * 返回jwt过期时间
     *
     * @param expiration
     * @return
     */
    private static Date generateExpirationDate(Integer expiration) {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    public static String getUsernameFromToken(String token, String secret) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token, secret);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 根据token获取用户id
     *
     * @param token
     * @param secret
     * @return
     */
    public static Object getObjectFromToken(String token, String secret, String data) {
        Claims claims = getClaimsFromToken(token, secret);
        return claims.get(data);
    }

    private static Claims getClaimsFromToken(String token, String secret) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * 检测jwt是否过期
     *
     * @param token
     * @param secret
     * @return 如果返回-1 表示过期，否则返回距离过期的秒数
     */
    public static long checkJwtExpired(String token, String secret) {
        long expired = -1;
        try {
            Claims claims = getClaimsFromToken(token, secret);
            if (null != claims)
                expired = (claims.getExpiration().getTime() - new Date().getTime()) / 1000;
        } catch (ExpiredJwtException e) {
            System.out.println("jwt已经过期了，不可以使用");
            return expired;
        }
        System.out.println("token距离过期时间还有:" + expired + "秒");
        return expired;
    }

    public static boolean canTokenBeRefreshed(String token, String secret,String createdKey,Date updateTime) {
        final Date created = getCreatedDateFromToken(token, secret, createdKey);
        return !isCreatedBeforeLastPasswordReset(created, updateTime)
                && !isTokenExpired(token,secret);
    }

    public static boolean isTokenExpired(String token,String secret) {
        final Date expiration = getExpirationDateFromToken(token,secret);
        return expiration.before(new Date());
    }

    private static Date getExpirationDateFromToken(String token,String secret) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token,secret);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    private static boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    private static Date getCreatedDateFromToken(String token,String secret,String createdKey) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token,secret);
            created = new Date((Long) claims.get(createdKey));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }


    public static String refreshToken(String token,String secret,String createdKey,Date updateTime,int expiration) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token,secret);
            claims.put(createdKey, updateTime);
            refreshedToken = generateToken(claims,secret,expiration);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public static boolean validateToken(String token,String existToken, String secret,UserInfo info) {
        if (StringUtils.isBlank(token)) {
            return false;
        }
        if (token.equals(existToken)) {
            final String username = getUsernameFromToken(token,secret);
            return (
                    username.equals(info.getUsername())
                            && !isTokenExpired(token,secret));
        } else {
            return false;
        }
    }
}
