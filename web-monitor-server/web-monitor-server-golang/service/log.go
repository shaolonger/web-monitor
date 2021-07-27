package service

import (
	"go.uber.org/zap"
	"time"
	"web.monitor.com/global"
	"web.monitor.com/model"
	"web.monitor.com/model/validation"
)

func AddClient(r validation.AddClient) (err error, data interface{}) {
	db := global.WM_DB.Model(&model.LmsClientUser{})

	// 保存实体
	log := model.LmsClientUser{
		CreateTime: time.Now(),
		UpdateTime: time.Now(),
		Cuuid:      r.Cuuid,
		Buid:       r.Buid,
		Buname:     r.Buname,
	}
	err = db.Create(&log).Error
	if err != nil {
		global.WM_LOG.Error("新增日志客户端用户失败", zap.Any("err", err))
		return err, false
	}
	return nil, true
}
