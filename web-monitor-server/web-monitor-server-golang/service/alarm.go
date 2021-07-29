package service

import (
	"go.uber.org/zap"
	"gorm.io/gorm"
	"time"
	"web.monitor.com/global"
	"web.monitor.com/model"
	"web.monitor.com/model/validation"
)

func AddAlarm(r validation.AddAlarm, userId uint64) (err error, data interface{}) {
	var entity model.AmsAlarm

	// 使用事务的方式提交，避免中途异常后导致错误改动了关联关系的问题
	err = global.WM_DB.Transaction(func(tx *gorm.DB) error {
		db := tx.Model(&model.AmsAlarm{})

		// 保存实体
		entity = model.AmsAlarm{
			Name:              r.Name,
			Level:             *r.Level,
			Category:          *r.Category,
			Rule:              r.Rule,
			StartTime:         r.StartTime,
			EndTime:           r.EndTime,
			SilentPeriod:      *r.SilentPeriod,
			IsActive:          *r.IsActive,
			ProjectIdentifier: r.ProjectIdentifier,
			IsDeleted:         0,
			CreateTime:        time.Now(),
			UpdateTime:        time.Now(),
			CreateBy:          userId,
		}
		err = db.Create(&entity).Error
		if err != nil {
			global.WM_LOG.Error("新增日志客户端用户失败", zap.Any("err", err))
			return err
		}

		// 保存subscriberList
		alarmId := entity.Id
		if len(r.SubscriberList) > 0 {
			for _, subscribe := range r.SubscriberList {
				err = AddSubscriber(tx, subscribe, alarmId)
				if err != nil {
					return err
				}
			}
		}

		return nil
	})

	if err != nil {
		global.WM_LOG.Error("新增日志客户端用户失败-事务回滚", zap.Any("err", err))
		return err, nil
	}

	// 启动预警定时任务
	if entity.IsActive == 1 {
		// TODO 创建定时任务
	}

	return nil, entity
}
