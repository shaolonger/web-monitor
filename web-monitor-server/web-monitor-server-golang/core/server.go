package core

import (
	"fmt"
	"go.uber.org/zap"
	"web.monitor.com/global"
	"web.monitor.com/initialize"
)

func RunWindowsServer() {
	Router := initialize.Router()
	address := fmt.Sprintf(":%d", global.WM_CONFIG.System.Addr)
	s := initServer(address, Router)
	global.WM_LOG.Info("web服务成功运行在端口号", zap.String("address", address))
	global.WM_LOG.Error(s.ListenAndServe().Error())
}
