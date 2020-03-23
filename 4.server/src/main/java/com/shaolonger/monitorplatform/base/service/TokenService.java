package com.shaolonger.monitorplatform.base.service;

import com.shaolonger.monitorplatform.user.entity.UserEntity;
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

}
