package v1

import (
	"github.com/gin-gonic/gin"
	"go.uber.org/zap"
	"web.monitor.com/global"
	"web.monitor.com/model/response"
	"web.monitor.com/model/validation"
	"web.monitor.com/service"
)

func AddLog(c *gin.Context) {
	var err error
	var data interface{}
	var r validation.AddLog
	err = c.ShouldBind(&r)
	if err != nil {
		global.WM_LOG.Error("通用日志打点上传失败", zap.Any("err", err))
		response.FailWithError(err, c)
		return
	}
	switch r.LogType {
	case "JS_ERROR":
		var rJs validation.AddJsErrorLog
		err = c.ShouldBind(&rJs)
		if err == nil {
			err, data = service.AddJsErrorLog(rJs)
		}
		break
	case "HTTP_ERROR":
		var rHttp validation.AddHttpErrorLog
		err = c.ShouldBind(&rHttp)
		if err == nil {
			err, data = service.AddHttpErrorLog(rHttp)
		}
		break
	case "RESOURCE_LOAD_ERROR":
		var rRes validation.AddResourceLoadErrorLog
		err = c.ShouldBind(&rRes)
		if err == nil {
			err, data = service.AddResourceLoadErrorLog(rRes)
		}
		break
	case "CUSTOM_ERROR":
		var rCus validation.AddCustomErrorLog
		err = c.ShouldBind(&rCus)
		if err == nil {
			err, data = service.AddCustomErrorLog(rCus)
		}
		break
	default:
		break
	}
	if err != nil {
		global.WM_LOG.Error("通用日志打点上传失败", zap.Any("err", err))
		response.FailWithError(err, c)
	} else {
		global.WM_LOG.Info("通用日志打点上传成功", zap.Any("data", data))
		response.SuccessWithData(data, c)
	}
}

func AddClient(c *gin.Context) {
	var err error
	var r validation.AddClient
	err = c.ShouldBind(&r)
	if err != nil {
		global.WM_LOG.Error("新增日志客户端用户失败", zap.Any("err", err))
		response.FailWithError(err, c)
		return
	}
	if err, data := service.AddClient(r); err != nil {
		global.WM_LOG.Error("新增日志客户端用户失败", zap.Any("err", err))
		response.FailWithError(err, c)
	} else {
		global.WM_LOG.Info("新增日志客户端用户成功", zap.Any("data", data))
		response.SuccessWithData(data, c)
	}
}

func ListLog(c *gin.Context) {
	var err error
	var r validation.ListLog
	// 因gin暂时还不支持从content-type: multipart/form-data中解析出array等复杂结构
	// ，因此这里暂时改为content-type: application/json的方式，跟java后台写的方案不一致
	err = c.ShouldBind(&r)
	if err != nil {
		global.WM_LOG.Error("多条件高级查询失败", zap.Any("err", err))
		response.FailWithError(err, c)
		return
	}
	if err, data := service.ListLog(r); err != nil {
		global.WM_LOG.Error("多条件高级查询失败", zap.Any("err", err))
		response.FailWithError(err, c)
	} else {
		global.WM_LOG.Info("多条件高级查询成功", zap.Any("data", data))
		response.SuccessWithData(data, c)
	}
}
