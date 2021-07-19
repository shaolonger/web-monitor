package v1

import (
	"github.com/gin-gonic/gin"
	"go.uber.org/zap"
	"web.monitor.com/global"
	"web.monitor.com/model/response"
	"web.monitor.com/model/validation"
	"web.monitor.com/service"
)

func AddJsErrorLog(c *gin.Context) {
	var err error
	var r validation.AddJsErrorLog
	err = c.ShouldBind(&r)
	if err != nil {
		global.WM_LOG.Error("新增js异常日志失败", zap.Any("err", err))
		response.FailWithError(err, c)
		return
	}
	// 保存实体
	if err, data := service.AddJsErrorLog(r); err != nil {
		global.WM_LOG.Error("新增js异常日志失败", zap.Any("err", err))
		response.FailWithError(err, c)
	} else {
		global.WM_LOG.Info("新增js异常日志成功", zap.Any("data", data))
		response.SuccessWithData(data, c)
	}
}
