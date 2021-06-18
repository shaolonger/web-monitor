package utils

import "web.monitor.com/global"

func HHasKey(key string, field string) bool {
	if key == "" {
		return false
	}
	if result, err := global.WM_REDIS.HExists(key, field).Result(); err != nil {
		return false
	} else {
		return result
	}
}
