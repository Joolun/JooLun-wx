package com.joolun.framework.web.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Base64;
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
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;

/**
 * token验证处理
 *
 * @author ruoyi
 */
@Component
public class TokenService
{
    private static final Logger log = LoggerFactory.getLogger(TokenService.class);

    // 令牌自定义标识
    @Value("${token.header}")
    private String header;

    // 令牌秘钥
    @Value("${token.secret}")
    private String secret;

    // 令牌有效期（默认30分钟）
    @Value("${token.expireTime}")
    private int expireTime;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

    @Autowired
    private RedisCache redisCache;

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request)
    {
        // 获取请求携带的令牌
        String token = getToken(request);
        if (StringUtils.isNotEmpty(token))
        {
            try
            {
                Claims claims = parseToken(token);
                // 解析对应的权限以及用户信息
                String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);
                String userKey = getTokenKey(uuid);
                LoginUser user = redisCache.getCacheObject(userKey);
                return user;
            }
            catch (io.jsonwebtoken.security.SecurityException e)
            {
                log.error("令牌验证失败'{}'", e.getMessage());
            }
            catch (io.jsonwebtoken.MalformedJwtException e)
            {
                log.error("令牌格式错误'{}'", e.getMessage());
            }
            catch (io.jsonwebtoken.ExpiredJwtException e)
            {
                log.error("令牌已过期'{}'", e.getMessage());
            }
            catch (Exception e)
            {
                log.error("获取用户信息异常'{}'", e.getMessage());
            }
        }
        return null;
    }

    /**
     * 设置用户身份信息
     */
    public void setLoginUser(LoginUser loginUser)
    {
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken()))
        {
            refreshToken(loginUser);
        }
    }

    /**
     * 删除用户身份信息
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
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
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
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     *
     * @param loginUser
     * @return 令牌
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
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser)
    {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
        redisCache.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }

    /**
     * 设置用户代理信息
     *
     * @param loginUser 登录信息
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
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String createToken(Map<String, Object> claims)
    {
        // 确保密钥长度足够，如果原始密钥长度不足则扩展它
        SecretKey key = generateSecureKey(secret);
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS512).compact();
        return token;
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims parseToken(String token)
    {
        // 确保密钥长度足够
        SecretKey key = generateSecureKey(secret);
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token)
    {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    /**
     * 获取请求token
     *
     * @param request
     * @return token
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

    private String getTokenKey(String uuid)
    {
        return CacheConstants.LOGIN_TOKEN_KEY + uuid;
    }
    
    /**
     * 生成安全的密钥，确保其长度满足JWT HMAC-SHA算法的要求
     *
     * @param originalSecret 原始密钥字符串
     * @return 符合安全要求的密钥
     */
    private SecretKey generateSecureKey(String originalSecret) {
        // 如果原始密钥是Base64编码的，则先解码
        byte[] keyBytes;
        try {
            // 尝试解码Base64
            keyBytes = Base64.getDecoder().decode(originalSecret);
        } catch (IllegalArgumentException e) {
            // 如果不是有效的Base64编码，则直接使用原始字符串的字节
            keyBytes = originalSecret.getBytes();
        }
        
        // 检查密钥长度，如果不足则扩展
        // HS512算法要求至少512位（64字节）
        if (keyBytes.length < 64) { // 512 bits = 64 bytes
            // 使用SHA-512哈希来生成足够长的密钥
            try {
                java.security.MessageDigest sha512 = java.security.MessageDigest.getInstance("SHA-512");
                byte[] hashedBytes = sha512.digest(keyBytes);
                
                // 如果哈希结果还不够长，扩展它
                if (hashedBytes.length < 64) {
                    // 创建一个64字节的数组并填充哈希值
                    keyBytes = new byte[64];
                    System.arraycopy(hashedBytes, 0, keyBytes, 0, Math.min(hashedBytes.length, 64));
                } else {
                    keyBytes = hashedBytes;
                }
            } catch (java.security.NoSuchAlgorithmException e) {
                // 如果SHA-512不可用，则通过重复原密钥来扩展
                StringBuilder extendedKey = new StringBuilder(originalSecret);
                while (extendedKey.length() < 64) {
                    extendedKey.append(originalSecret);
                }
                keyBytes = extendedKey.toString().substring(0, 64).getBytes();
            }
        }
        
        // 创建符合要求的密钥
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
