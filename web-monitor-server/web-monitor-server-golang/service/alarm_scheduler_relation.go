package service

import (
	"go.uber.org/zap"
	"gorm.io/gorm"
	"time"
	"web.monitor.com/global"
	"web.monitor.com/model"
)

func AddAlarmSchedulerRelation(tx *gorm.DB, alarmId uint64, schedulerId uint64) error {
	db := tx.Model(&model.AmsAlarmSchedulerRelation{})
	entity := model.AmsAlarmSchedulerRelation{
		AlarmId:     alarmId,
		SchedulerId: schedulerId,
		CreateTime:  time.Now(),
	}
	err := db.Save(&entity).Error
	if err != nil {
		global.WM_LOG.Error("新增预警计划关联关系失败", zap.Any("err", err))
		return err
	}
	return nil
}
