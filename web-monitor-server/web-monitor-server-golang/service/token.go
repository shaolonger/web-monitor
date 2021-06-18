package service

import (
	"web.monitor.com/utils"
)

const (
	RedisTokenKey = "umsLoginToken"
)

func HasToken(token string) bool {
	return utils.HHasKey(RedisTokenKey, token)
}
