package service

import (
	"gorm.io/gorm"
	"time"
	"web.monitor.com/model"
)

const (
	BeanName       = "AlarmScheduler"
	MethodName     = "startScheduler"
	CronExpression = "*/10 * * * * ?"
)

// AddScheduler 保存定时任务
func AddScheduler(tx *gorm.DB, params string) (error, *model.TmsScheduler) {
	db := tx.Model(&model.TmsScheduler{})
	schedulerEntity := model.TmsScheduler{
		BeanName:       BeanName,
		MethodName:     MethodName,
		Params:         params,
		CronExpression: CronExpression,
		CreateTime:     time.Now(),
		UpdateTime:     time.Now(),
		State:          1,
	}
	err := db.Save(&schedulerEntity).Error
	if err != nil {
		return err, nil
	}
	return nil, &schedulerEntity
}
