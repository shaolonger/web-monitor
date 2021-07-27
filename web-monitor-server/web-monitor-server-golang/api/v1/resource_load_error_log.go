package v1

import (
	"github.com/gin-gonic/gin"
	"go.uber.org/zap"
	"web.monitor.com/global"
	"web.monitor.com/model/response"
	"web.monitor.com/model/validation"
	"web.monitor.com/service"
)

func AddResourceLoadErrorLog(c *gin.Context) {
	var err error
	var r validation.AddResourceLoadErrorLog
	err = c.ShouldBind(&r)
	if err != nil {
		global.WM_LOG.Error("新增resourceLoad异常日志失败", zap.Any("err", err))
		response.FailWithError(err, c)
		return
	}
	if err, data := service.AddResourceLoadErrorLog(r); err != nil {
		global.WM_LOG.Error("新增resourceLoad异常日志失败", zap.Any("err", err))
		response.FailWithError(err, c)
	} else {
		global.WM_LOG.Info("新增resourceLoad异常日志成功", zap.Any("data", data))
		response.SuccessWithData(data, c)
	}
}

func GetResourceLoadErrorLog(c *gin.Context) {
	var err error
	var r validation.GetResourceLoadErrorLog
	err = c.ShouldBind(&r)
	if err != nil {
		global.WM_LOG.Error("条件查询resourceLoad异常日志失败", zap.Any("err", err))
		response.FailWithError(err, c)
		return
	}
	if err, data := service.GetResourceLoadErrorLog(r); err != nil {
		global.WM_LOG.Error("条件查询resourceLoad异常日志失败", zap.Any("err", err))
		response.FailWithError(err, c)
	} else {
		global.WM_LOG.Info("条件查询resourceLoad异常日志成功", zap.Any("data", data))
		response.SuccessWithData(data, c)
	}
}

func GetResourceLoadErrorLogByGroup(c *gin.Context) {
	var err error
	var r validation.GetResourceLoadErrorLogByGroup
	err = c.ShouldBind(&r)
	if err != nil {
		global.WM_LOG.Error("聚合查询resourceLoad异常日志失败", zap.Any("err", err))
		response.FailWithError(err, c)
		return
	}
	if err, data := service.GetResourceLoadErrorLogByGroup(r); err != nil {
		global.WM_LOG.Error("聚合查询resourceLoad异常日志失败", zap.Any("err", err))
		response.FailWithError(err, c)
	} else {
		global.WM_LOG.Info("聚合查询resourceLoad异常日志成功", zap.Any("data", data))
		response.SuccessWithData(data, c)
	}
}

func GetOverallByTimeRange(c *gin.Context) {
	var err error
	var r validation.GetOverallByTimeRange
	err = c.ShouldBind(&r)
	if err != nil {
		global.WM_LOG.Error("获取总览统计信息失败", zap.Any("err", err))
		response.FailWithError(err, c)
		return
	}
	if err, data := service.GetOverallByTimeRange(r); err != nil {
		global.WM_LOG.Error("获取总览统计信息失败", zap.Any("err", err))
		response.FailWithError(err, c)
	} else {
		global.WM_LOG.Info("获取总览统计信息成功", zap.Any("data", data))
		response.SuccessWithData(data, c)
	}
}
