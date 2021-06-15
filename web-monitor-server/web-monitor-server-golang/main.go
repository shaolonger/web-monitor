package main

import (
	"web.monitor.com/core"
	"web.monitor.com/global"
	"web.monitor.com/initialize"
)

func main() {
	// 初始化Viper配置工具
	global.WM_VP = core.Viper()
	// 初始化zap日志
	global.WM_LOG = core.Zap()
	// 初始化gorm连接数据库
	global.WM_DB = initialize.Gorm()
}
