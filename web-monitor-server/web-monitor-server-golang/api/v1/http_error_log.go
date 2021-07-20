package v1

import (
	"github.com/gin-gonic/gin"
	"go.uber.org/zap"
	"web.monitor.com/global"
	"web.monitor.com/model/response"
	"web.monitor.com/model/validation"
	"web.monitor.com/service"
)

func AddHttpErrorLog(c *gin.Context) {
	var err error
	var r validation.AddHttpErrorLog
	err = c.ShouldBind(&r)
	if err != nil {
		global.WM_LOG.Error("新增http异常日志失败", zap.Any("err", err))
		response.FailWithError(err, c)
		return
	}
	// 保存实体
	if err, data := service.AddHttpErrorLog(r); err != nil {
		global.WM_LOG.Error("新增http异常日志失败", zap.Any("err", err))
		response.FailWithError(err, c)
	} else {
		global.WM_LOG.Info("新增http异常日志成功", zap.Any("data", data))
		response.SuccessWithData(data, c)
	}
}

func GetHttpErrorLog(c *gin.Context) {
	var err error
	var r validation.GetHttpErrorLog
	err = c.ShouldBind(&r)
	if err != nil {
		global.WM_LOG.Error("条件查询http异常日志失败", zap.Any("err", err))
		response.FailWithError(err, c)
		return
	}
	// 保存实体
	if err, data := service.GetHttpErrorLog(r); err != nil {
		global.WM_LOG.Error("条件查询http异常日志失败", zap.Any("err", err))
		response.FailWithError(err, c)
	} else {
		global.WM_LOG.Info("条件查询http异常日志成功", zap.Any("data", data))
		response.SuccessWithData(data, c)
	}
}

func GetHttpErrorLogByGroup(c *gin.Context) {
	var err error
	var r validation.GetHttpErrorLogByGroup
	err = c.ShouldBind(&r)
	if err != nil {
		global.WM_LOG.Error("聚合查询http异常日志失败", zap.Any("err", err))
		response.FailWithError(err, c)
		return
	}
	// 保存实体
	if err, data := service.GetHttpErrorLogByGroup(r); err != nil {
		global.WM_LOG.Error("聚合查询http异常日志失败", zap.Any("err", err))
		response.FailWithError(err, c)
	} else {
		global.WM_LOG.Info("聚合查询http异常日志成功", zap.Any("data", data))
		response.SuccessWithData(data, c)
	}
}
