package service

import (
	"go.uber.org/zap"
	"time"
	"web.monitor.com/global"
)

func HHasKey(key string, field string) bool {
	if key == "" {
		return false
	}
	if result, err := global.WM_REDIS.Client.HExists(*global.WM_REDIS.Context, key, field).Result(); err != nil {
		return false
	} else {
		return result
	}
}

func HGet(key string, field string) (err error, value string) {
	if result, err := global.WM_REDIS.Client.HGet(*global.WM_REDIS.Context, key, field).Result(); err != nil {
		return err, value
	} else {
		return nil, result
	}
}

func HSet(key string, field string, value interface{}, expiration time.Duration) bool {
	if _, err := global.WM_REDIS.Client.HSet(*global.WM_REDIS.Context, key, field, value).Result(); err != nil {
		global.WM_LOG.Error("HSet错误", zap.Any("err", err))
		return false
	} else {
		// 设置过期时间
		Expire(key, expiration)
		return true
	}
}

func Expire(key string, expiration time.Duration) {
	global.WM_REDIS.Client.Expire(*global.WM_REDIS.Context, key, expiration)
}
