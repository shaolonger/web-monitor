package initialize

import (
	"context"
	"github.com/go-redis/redis/v8"
	"go.uber.org/zap"
	"os"
	"web.monitor.com/global"
)

var ctx = context.Background()

func Redis() {
	redisConfig := global.WM_CONFIG.Redis
	client := redis.NewClient(&redis.Options{
		Addr:     redisConfig.Addr,
		Password: redisConfig.Password,
		DB:       redisConfig.DB,
	})
	pong, err := client.Ping(ctx).Result()
	if err != nil {
		global.WM_LOG.Error("redis连接失败, err:", zap.Any("err", err))
		os.Exit(0)
	} else {
		global.WM_LOG.Info("redis连接成功:", zap.String("pong", pong))
		global.WM_REDIS = &global.Redis{
			Client:  client,
			Context: &ctx,
		}
	}
}
