package com.joolun.framework.web.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import jakarta.servlet.http.HttpServletRequest;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.joolun.common.constant.CacheConstants;
import com.joolun.common.constant.Constants;
import com.joolun.common.core.domain.model.LoginUser;
import com.joolun.common.core.redis.RedisCache;
import com.joolun.common.utils.ServletUtils;
import com.joolun.common.utils.StringUtils;
import com.joolun.common.utils.ip.AddressUtils;
import com.joolun.common.utils.ip.IpUtils;
import com.joolun.common.utils.uuid.IdUtils;
import eu.bitwalker.useragentutils.UserAgent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * token 验证处理
 *
 * @author ruoyi
 */
@Component
public class TokenService
{
    private static final Logger log = LoggerFactory.getLogger(TokenService.class);

    /**
     * 请求头中的令牌字段名。
     */
    @Value("${token.header}")
    private String header;

    /**
     * JWT 签名密钥原文。
     */
    @Value("${token.secret}")
    private String secret;

    /**
     * 令牌有效期，单位：分钟。
     */
    @Value("${token.expireTime}")
    private int expireTime;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    /**
     * 距离过期不足 20 分钟时自动续签。
     */
    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

    @Autowired
    private RedisCache redisCache;

    /**
     * 从请求中解析当前登录用户。
     *
     * @param request HTTP 请求
     * @return 登录用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request)
    {
        String token = getToken(request);
        if (StringUtils.isNotEmpty(token))
        {
            try
            {
                Claims claims = parseToken(token);
                String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);
                String userKey = getTokenKey(uuid);
                return redisCache.getCacheObject(userKey);
            }
            catch (io.jsonwebtoken.security.SecurityException e)
            {
                log.error("令牌验证失败：{}", e.getMessage());
            }
            catch (MalformedJwtException e)
            {
                log.error("令牌格式错误：{}", e.getMessage());
            }
            catch (ExpiredJwtException e)
            {
                log.error("令牌已过期：{}", e.getMessage());
            }
            catch (Exception e)
            {
                log.error("获取登录用户信息异常：{}", e.getMessage());
            }
        }
        return null;
    }

    /**
     * 刷新用户登录态缓存。
     *
     * @param loginUser 登录用户
     */
    public void setLoginUser(LoginUser loginUser)
    {
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken()))
        {
            refreshToken(loginUser);
        }
    }

    /**
     * 删除用户登录态缓存。
     *
     * @param token 令牌
     */
    public void delLoginUser(String token)
    {
        if (StringUtils.isNotEmpty(token))
        {
            String userKey = getTokenKey(token);
            redisCache.deleteObject(userKey);
        }
    }

    /**
     * 创建登录令牌。
     *
     * @param loginUser 登录用户
     * @return JWT 字符串
     */
    public String createToken(LoginUser loginUser)
    {
        String token = IdUtils.fastUUID();
        loginUser.setToken(token);
        setUserAgent(loginUser);
        refreshToken(loginUser);

        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.LOGIN_USER_KEY, token);
        return createToken(claims);
    }

    /**
     * 校验令牌是否即将过期，若快过期则自动刷新。
     *
     * @param loginUser 登录用户
     */
    public void verifyToken(LoginUser loginUser)
    {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN)
        {
            refreshToken(loginUser);
        }
    }

    /**
     * 更新 Redis 中的登录态有效时间。
     *
     * @param loginUser 登录用户
     */
    public void refreshToken(LoginUser loginUser)
    {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        String userKey = getTokenKey(loginUser.getToken());
        redisCache.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }

    /**
     * 记录登录终端信息，方便后台审计登录设备、IP 和地区。
     *
     * @param loginUser 登录用户
     */
    public void setUserAgent(LoginUser loginUser)
    {
        UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        String ip = IpUtils.getIpAddr();
        loginUser.setIpaddr(ip);
        loginUser.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
        loginUser.setBrowser(userAgent.getBrowser().getName());
        loginUser.setOs(userAgent.getOperatingSystem().getName());
    }

    /**
     * 生成 JWT。
     *
     * @param claims 业务载荷
     * @return JWT 字符串
     */
    private String createToken(Map<String, Object> claims)
    {
        SecretKey key = generateSecureKey(secret);
        return Jwts.builder()
            .setClaims(claims)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();
    }

    /**
     * 解析 JWT 载荷。
     *
     * @param token JWT 字符串
     * @return 载荷声明
     */
    private Claims parseToken(String token)
    {
        SecretKey key = generateSecureKey(secret);
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    /**
     * 从 JWT 中读取用户名。
     *
     * @param token JWT 字符串
     * @return 用户名
     */
    public String getUsernameFromToken(String token)
    {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    /**
     * 从请求头中提取 token，并去掉系统约定的前缀。
     *
     * @param request HTTP 请求
     * @return 纯 token 字符串
     */
    private String getToken(HttpServletRequest request)
    {
        String token = request.getHeader(header);
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX))
        {
            token = token.replace(Constants.TOKEN_PREFIX, "");
        }
        return token;
    }

    /**
     * 生成 Redis 登录态缓存 key。
     *
     * @param uuid 登录 token 标识
     * @return 缓存 key
     */
    private String getTokenKey(String uuid)
    {
        return CacheConstants.LOGIN_TOKEN_KEY + uuid;
    }

    /**
     * 生成满足 HS512 要求的签名密钥。
     * 当前项目统一按原始字符串处理，不再尝试 Base64 解码，避免把普通配置误判为 Base64。
     *
     * @param originalSecret 配置中的原始密钥
     * @return 可用于 HS512 的安全密钥
     */
    private SecretKey generateSecureKey(String originalSecret)
    {
        byte[] keyBytes = originalSecret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 64)
        {
            try
            {
                keyBytes = MessageDigest.getInstance("SHA-512").digest(keyBytes);
            }
            catch (NoSuchAlgorithmException e)
            {
                StringBuilder extendedKey = new StringBuilder(originalSecret);
                while (extendedKey.length() < 64)
                {
                    extendedKey.append(originalSecret);
                }
                keyBytes = extendedKey.toString().substring(0, 64).getBytes(StandardCharsets.UTF_8);
            }
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
