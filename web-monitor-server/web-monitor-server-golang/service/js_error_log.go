package service

import (
	"go.uber.org/zap"
	"time"
	"web.monitor.com/global"
	"web.monitor.com/model"
	"web.monitor.com/model/validation"
)

func AddJsErrorLog(r validation.AddJsErrorLog) (err error, data interface{}) {
	db := global.WM_DB.Model(&model.LmsJsErrorLog{})

	// 保存实体
	log := model.LmsJsErrorLog{
		LogType:           r.LogType,
		ProjectIdentifier: r.ProjectIdentifier,
		CreateTime:        time.Now(),
		Cuuid:             r.Cuuid,
		Buid:              r.Buid,
		Buname:            r.Buname,
		PageUrl:           r.PageUrl,
		PageKey:           r.PageKey,
		DeviceName:        r.DeviceName,
		Os:                r.Os,
		OsVersion:         r.OsVersion,
		BrowserName:       r.BrowserName,
		BrowserVersion:    r.BrowserVersion,
		IpAddress:         r.IpAddress,
		Address:           r.Address,
		NetType:           r.NetType,
		ErrorType:         r.ErrorType,
		ErrorMessage:      r.ErrorMessage,
		ErrorStack:        r.ErrorStack,
	}
	err = db.Create(&log).Error
	if err != nil {
		global.WM_LOG.Error("保存js异常日志失败", zap.Any("err", err))
		return err, false
	}
	return nil, true
}
