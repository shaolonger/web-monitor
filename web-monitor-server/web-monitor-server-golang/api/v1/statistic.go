package v1

import (
	"github.com/gin-gonic/gin"
	"go.uber.org/zap"
	"web.monitor.com/global"
	"web.monitor.com/model/response"
	"web.monitor.com/model/validation"
	"web.monitor.com/service"
)

func GetStatisticOverallByTimeRange(c *gin.Context) {
	var err error
	var r validation.GetStatisticOverallByTimeRange
	err = c.ShouldBind(&r)
	if err != nil {
		global.WM_LOG.Error("获取总览页信息失败", zap.Any("err", err))
		response.FailWithError(err, c)
		return
	}
	// 保存实体
	if err, data := service.GetStatisticOverallByTimeRange(r); err != nil {
		global.WM_LOG.Error("获取总览页信息失败", zap.Any("err", err))
		response.FailWithError(err, c)
	} else {
		global.WM_LOG.Info("获取总览页信息成功", zap.Any("data", data))
		response.SuccessWithData(data, c)
	}
}

func GetLogCountByHours(c *gin.Context) {
	var err error
	var r validation.GetLogCountByHours
	err = c.ShouldBind(&r)
	if err != nil {
		global.WM_LOG.Error("按小时间隔获取各小时内的日志数量失败", zap.Any("err", err))
		response.FailWithError(err, c)
		return
	}
	// 保存实体
	if err, data := service.GetLogCountByHours(r); err != nil {
		global.WM_LOG.Error("按小时间隔获取各小时内的日志数量失败", zap.Any("err", err))
		response.FailWithError(err, c)
	} else {
		global.WM_LOG.Info("按小时间隔获取各小时内的日志数量成功", zap.Any("data", data))
		response.SuccessWithData(data, c)
	}
}
