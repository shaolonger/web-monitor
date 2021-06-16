package initialize

import (
	"github.com/go-redis/redis"
	"go.uber.org/zap"
	"web.monitor.com/global"
)

func Redis() {
	redisConfig := global.WM_CONFIG.Redis
	client := redis.NewClient(&redis.Options{
		Addr:     redisConfig.Addr,
		Password: redisConfig.Password,
		DB:       redisConfig.DB,
	})
	pong, err := client.Ping().Result()
	if err != nil {
		global.WM_LOG.Error("redis连接失败, err:", zap.Any("err", err))
	} else {
		global.WM_LOG.Info("redis连接成功:", zap.String("pong", pong))
		global.WM_REDIS = client
	}
}
