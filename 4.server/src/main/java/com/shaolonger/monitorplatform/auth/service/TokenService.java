package com.shaolonger.monitorplatform.auth.service;

import com.shaolonger.monitorplatform.auth.dto.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TokenService {

    @Autowired
    RedisService redisService;

    /**
     * token在redis中的key
     */
    public static final String REDIS_TOKEN_KEY = "umsLoginToken";

    /**
     * token在redis中的有效时间
     * 单位：分钟
     */
    public static final int REDIS_TOKEN_EXPIRE = 10;

    /**
     * 判断token是否存在
     *
     * @param token token
     * @return Boolean
     */
    public Boolean hasToken(String token) {
        if (StringUtils.isEmpty(token)) return false;
        return redisService.hHasKey(REDIS_TOKEN_KEY, token);
    }

    /**
     * 根据token获取用户信息
     *
     * @param token token
     * @return LoginUser
     */
    public LoginUser getUserByToken(String token) {
        if (StringUtils.isEmpty(token)) return null;
        if (!hasToken(token)) {
            return null;
        } else {
            return (LoginUser) redisService.hGet(REDIS_TOKEN_KEY, token);
        }
    }

    /**
     * 添加token
     *
     * @param token token
     * @param userId userId
     * @param username username
     * @return LoginUser
     */
    public LoginUser addOrUpdateToken(String token, Long userId, String username) {
        if (hasToken(token)) {
            LoginUser loginUser = getUserByToken(token);
            if (loginUser != null) {
                // 若token已存在，则刷新缓存时间
                redisService.expire(REDIS_TOKEN_KEY, REDIS_TOKEN_EXPIRE);
                return loginUser;
            } else {
                return null;
            }
        } else {
            // 若token不存在，则新增
            LoginUser loginUser = new LoginUser();
            loginUser.setId(userId);
            loginUser.setUsername(username);
            loginUser.setToken(token);
            redisService.hSet(REDIS_TOKEN_KEY, token, loginUser, REDIS_TOKEN_EXPIRE);
            return loginUser;
        }
    }

}
