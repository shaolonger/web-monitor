package com.shaolonger.monitorplatform.base.service;

import com.shaolonger.monitorplatform.user.entity.UserEntity;
import org.apache.catalina.User;
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
     * @return UserEntity
     */
    public UserEntity getUserByToken(String token) {
        if (StringUtils.isEmpty(token)) return null;
        if (!hasToken(token)) {
            return null;
        } else {
            return (UserEntity) redisService.hGet(REDIS_TOKEN_KEY, token);
        }
    }

    /**
     * 添加token
     *
     * @param token token
     * @param userId userId
     * @param username username
     * @return UserEntity
     */
    public UserEntity addOrUpdateToken(String token, Long userId, String username) {
        if (hasToken(token)) {
            UserEntity userEntity = getUserByToken(token);
            if (userEntity != null) {
                // 若token已存在，则刷新缓存时间
                redisService.hSet(REDIS_TOKEN_KEY, token, userEntity, REDIS_TOKEN_EXPIRE);
                return userEntity;
            } else {
                return null;
            }
        } else {
            // 若token不存在，则新增
            UserEntity userEntity = new UserEntity();
            userEntity.setId(userId);
            userEntity.setUsername(username);
            redisService.hSet(REDIS_TOKEN_KEY, token, userEntity, REDIS_TOKEN_EXPIRE);
            return userEntity;
        }
    }

}
