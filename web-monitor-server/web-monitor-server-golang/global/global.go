package global

import (
	"github.com/spf13/viper"
	"go.uber.org/zap"
	"web.monitor.com/config"
)

var (
	WM_CONFIG config.Server
	WM_VP     *viper.Viper
	WM_LOG    *zap.Logger
)
