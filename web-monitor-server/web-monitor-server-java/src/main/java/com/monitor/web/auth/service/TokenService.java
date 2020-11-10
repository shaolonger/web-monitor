package com.monitor.web.auth.service;

import com.monitor.web.auth.dto.LoginUserDTO;
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
    public static final int REDIS_TOKEN_EXPIRE = 60;

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
    public LoginUserDTO getUserByToken(String token) {
        if (StringUtils.isEmpty(token)) return null;
        if (!hasToken(token)) {
            return null;
        } else {
            return (LoginUserDTO) redisService.hGet(REDIS_TOKEN_KEY, token);
        }
    }

    /**
     * 添加token
     *
     * @param token token
     * @param userId userId
     * @param username username
     * @param isAdmin isAdmin
     * @return LoginUser
     */
    public LoginUserDTO addOrUpdateToken(String token, Long userId, String username, Integer isAdmin) {
        if (hasToken(token)) {
            LoginUserDTO loginUserDTO = getUserByToken(token);
            if (loginUserDTO != null) {
                // 若token已存在，则刷新缓存时间
                redisService.expire(REDIS_TOKEN_KEY, REDIS_TOKEN_EXPIRE);
                return loginUserDTO;
            } else {
                return null;
            }
        } else {
            // 若token不存在，则新增
            LoginUserDTO loginUserDTO = new LoginUserDTO();
            loginUserDTO.setId(userId);
            loginUserDTO.setUsername(username);
            loginUserDTO.setIsAdmin(isAdmin);
            loginUserDTO.setToken(token);
            redisService.hSet(REDIS_TOKEN_KEY, token, loginUserDTO, REDIS_TOKEN_EXPIRE);
            return loginUserDTO;
        }
    }

}
