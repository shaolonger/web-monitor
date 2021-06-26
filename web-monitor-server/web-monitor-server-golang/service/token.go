package service

import (
	"encoding/json"
	"errors"
	"github.com/gin-gonic/gin"
	"strconv"
	"time"
	"web.monitor.com/global"
	"web.monitor.com/model/response"
)

const (
	RedisTokenKey     = "umsLoginToken"
	DefaultExpiration = 60 * time.Minute
)

func HasToken(token string) bool {
	return HHasKey(RedisTokenKey, token)
}

func AddOrUpdateToken(token string, userId uint64, username string, isAdmin int8) (err error, loginUser response.LoginUser) {
	if HasToken(token) {
		err, loginUser = GetUserByToken(token)
		if err != nil {
			return
		} else {
			// 若token已存在，则刷新缓存时间
			Expire(RedisTokenKey, getExpiration())
			return nil, loginUser
		}
	} else {
		// 若token不存在，则新增
		var value []byte
		loginUser = response.LoginUser{
			Id:       userId,
			Username: username,
			IsAdmin:  isAdmin,
			Token:    token,
		}
		value, err = json.Marshal(loginUser)
		HSet(RedisTokenKey, token, value, getExpiration())
		return nil, loginUser
	}
}

func GetUserByToken(token string) (err error, loginUser response.LoginUser) {
	var value string
	if token == "" {
		err = errors.New("token不能为空")
		return
	}
	if !HasToken(token) {
		err = errors.New("token已过期")
		return
	}
	if err, value = HGet(RedisTokenKey, token); err != nil {
		return
	} else {
		err = json.Unmarshal([]byte(value), &loginUser)
		return
	}
}

func GetUserIdByContext(c *gin.Context) (err error, userId uint64) {
	token := GetTokenByContext(c)
	err, loginUser := GetUserByToken(token)
	if err != nil {
		return err, 0
	}
	return nil, loginUser.Id
}

func getExpiration() time.Duration {
	expiration, err := strconv.Atoi(global.WM_CONFIG.Redis.Expiration)
	if err != nil {
		return DefaultExpiration
	} else {
		return time.Duration(expiration) * time.Minute
	}
}

func GetTokenByContext(c *gin.Context) (token string) {
	return c.GetHeader("token")
}
