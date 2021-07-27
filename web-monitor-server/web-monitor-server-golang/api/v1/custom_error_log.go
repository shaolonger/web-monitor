package v1

import (
	"github.com/gin-gonic/gin"
	"go.uber.org/zap"
	"web.monitor.com/global"
	"web.monitor.com/model/response"
	"web.monitor.com/model/validation"
	"web.monitor.com/service"
)

func AddCustomErrorLog(c *gin.Context) {
	var err error
	var r validation.AddCustomErrorLog
	err = c.ShouldBind(&r)
	if err != nil {
		global.WM_LOG.Error("新增custom异常日志失败", zap.Any("err", err))
		response.FailWithError(err, c)
		return
	}
	if err, data := service.AddCustomErrorLog(r); err != nil {
		global.WM_LOG.Error("新增custom异常日志失败", zap.Any("err", err))
		response.FailWithError(err, c)
	} else {
		global.WM_LOG.Info("新增custom异常日志成功", zap.Any("data", data))
		response.SuccessWithData(data, c)
	}
}

func GetCustomErrorLog(c *gin.Context) {
	var err error
	var r validation.GetCustomErrorLog
	err = c.ShouldBind(&r)
	if err != nil {
		global.WM_LOG.Error("条件查询custom异常日志失败", zap.Any("err", err))
		response.FailWithError(err, c)
		return
	}
	if err, data := service.GetCustomErrorLog(r); err != nil {
		global.WM_LOG.Error("条件查询custom异常日志失败", zap.Any("err", err))
		response.FailWithError(err, c)
	} else {
		global.WM_LOG.Info("条件查询custom异常日志成功", zap.Any("data", data))
		response.SuccessWithData(data, c)
	}
}

func GetCustomErrorLogByGroup(c *gin.Context) {
	var err error
	var r validation.GetCustomErrorLogByGroup
	err = c.ShouldBind(&r)
	if err != nil {
		global.WM_LOG.Error("聚合查询custom异常日志失败", zap.Any("err", err))
		response.FailWithError(err, c)
		return
	}
	if err, data := service.GetCustomErrorLogByGroup(r); err != nil {
		global.WM_LOG.Error("聚合查询custom异常日志失败", zap.Any("err", err))
		response.FailWithError(err, c)
	} else {
		global.WM_LOG.Info("聚合查询custom异常日志成功", zap.Any("data", data))
		response.SuccessWithData(data, c)
	}
}
