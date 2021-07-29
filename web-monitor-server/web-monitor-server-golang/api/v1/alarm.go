package v1

import (
	"github.com/gin-gonic/gin"
	"go.uber.org/zap"
	"strconv"
	"web.monitor.com/global"
	"web.monitor.com/model/response"
	"web.monitor.com/model/validation"
	"web.monitor.com/service"
)

func AddAlarm(c *gin.Context) {
	var err error
	var r validation.AddAlarm
	// 因gin暂时还不支持从content-type: multipart/form-data中解析出array等复杂结构
	// ，因此这里暂时改为content-type: application/json的方式，跟java后台写的方案不一致
	err = c.ShouldBind(&r)
	if err != nil {
		global.WM_LOG.Error("新增预警失败", zap.Any("err", err))
		response.FailWithError(err, c)
		return
	}
	err, userId := service.GetUserIdByContext(c)
	if err != nil {
		global.WM_LOG.Error("新增预警失败", zap.Any("err", err))
		response.FailWithError(err, c)
		return
	}
	if err, data := service.AddAlarm(r, userId); err != nil {
		global.WM_LOG.Error("新增预警失败", zap.Any("err", err))
		response.FailWithError(err, c)
	} else {
		global.WM_LOG.Info("新增预警成功", zap.Any("data", data))
		response.SuccessWithData(data, c)
	}
}

func UpdateAlarm(c *gin.Context) {
	var err error
	var r validation.UpdateAlarm
	// 因gin暂时还不支持从content-type: multipart/form-data中解析出array等复杂结构
	// ，因此这里暂时改为content-type: application/json的方式，跟java后台写的方案不一致
	err = c.ShouldBind(&r)
	if err != nil {
		global.WM_LOG.Error("编辑预警失败", zap.Any("err", err))
		response.FailWithError(err, c)
		return
	}
	if err, data := service.UpdateAlarm(r); err != nil {
		global.WM_LOG.Error("编辑预警失败", zap.Any("err", err))
		response.FailWithError(err, c)
	} else {
		global.WM_LOG.Info("编辑预警成功", zap.Any("data", data))
		response.SuccessWithData(data, c)
	}
}

func GetAlarm(c *gin.Context) {
	var err error
	var r validation.GetAlarm
	err = c.ShouldBind(&r)
	if err != nil {
		global.WM_LOG.Error("查询预警失败", zap.Any("err", err))
		response.FailWithError(err, c)
		return
	}
	if err, data := service.GetAlarm(r); err != nil {
		global.WM_LOG.Error("查询预警失败", zap.Any("err", err))
		response.FailWithError(err, c)
	} else {
		global.WM_LOG.Info("查询预警成功", zap.Any("data", data))
		response.SuccessWithData(data, c)
	}
}

func DeleteAlarm(c *gin.Context) {
	var err error
	id := c.Param("id")
	if id == "" {
		global.WM_LOG.Error("删除预警失败，id不能为空", zap.Any("err", err))
		response.FailWithError(err, c)
		return
	}
	alarmId, err := strconv.ParseUint(id, 10, 64)
	if err != nil {
		global.WM_LOG.Error("删除预警失败", zap.Any("err", err))
		response.FailWithError(err, c)
		return
	}
	err, data := service.DeleteAlarm(alarmId)
	if err != nil {
		global.WM_LOG.Error("删除预警失败", zap.Any("err", err))
		response.FailWithError(err, c)
	} else {
		response.SuccessWithData(data, c)
	}
}
