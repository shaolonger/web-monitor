package service

import (
	"go.uber.org/zap"
	"gorm.io/gorm"
	"web.monitor.com/global"
	"web.monitor.com/model"
	"web.monitor.com/model/validation"
)

// AddSubscriber 保存预警订阅通知记录
func AddSubscriber(tx *gorm.DB, s validation.Subscriber, alarmId uint64) (err error) {
	db := tx.Model(&model.AmsSubscriber{})

	// 保存实体
	entity := model.AmsSubscriber{
		AlarmId:    alarmId,
		Subscriber: s.Subscriber,
		IsActive:   *s.IsActive,
		Category:   *s.Category,
	}
	err = db.Create(&entity).Error
	if err != nil {
		global.WM_LOG.Error("保存预警订阅通知记录失败", zap.Any("err", err))
	}
	return err
}
