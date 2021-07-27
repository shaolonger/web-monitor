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
	if err, data := service.AddJsErrorLog(r); err != nil {
		global.WM_LOG.Error("新增js异常日志失败", zap.Any("err", err))
		response.FailWithError(err, c)
	} else {
		global.WM_LOG.Info("新增js异常日志成功", zap.Any("data", data))
		response.SuccessWithData(data, c)
	}
}

func GetJsErrorLog(c *gin.Context) {
	var err error
	var r validation.GetJsErrorLog
	err = c.ShouldBind(&r)
	if err != nil {
		global.WM_LOG.Error("条件查询js异常日志失败", zap.Any("err", err))
		response.FailWithError(err, c)
		return
	}
	if err, data := service.GetJsErrorLog(r); err != nil {
		global.WM_LOG.Error("条件查询js异常日志失败", zap.Any("err", err))
		response.FailWithError(err, c)
	} else {
		global.WM_LOG.Info("条件查询js异常日志成功", zap.Any("data", data))
		response.SuccessWithData(data, c)
	}
}

func GetJsErrorLogByGroup(c *gin.Context) {
	var err error
	var r validation.GetJsErrorLogByGroup
	err = c.ShouldBind(&r)
	if err != nil {
		global.WM_LOG.Error("聚合查询js异常日志失败", zap.Any("err", err))
		response.FailWithError(err, c)
		return
	}
	if err, data := service.GetJsErrorLogByGroup(r); err != nil {
		global.WM_LOG.Error("聚合查询js异常日志失败", zap.Any("err", err))
		response.FailWithError(err, c)
	} else {
		global.WM_LOG.Info("聚合查询js异常日志成功", zap.Any("data", data))
		response.SuccessWithData(data, c)
	}
}
