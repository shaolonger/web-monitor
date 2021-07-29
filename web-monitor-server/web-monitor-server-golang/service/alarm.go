package service

import (
	"errors"
	"go.uber.org/zap"
	"gorm.io/gorm"
	"math"
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
			global.WM_LOG.Error("新增预警失败", zap.Any("err", err))
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
		global.WM_LOG.Error("新增预警失败-事务回滚", zap.Any("err", err))
		return err, nil
	}

	// 启动预警定时任务
	if entity.IsActive == 1 {
		// TODO 创建定时任务
	}

	return nil, entity
}

func UpdateAlarm(r validation.UpdateAlarm) (err error, data interface{}) {
	var entity model.AmsAlarm

	// 使用事务的方式提交，避免中途异常后导致错误改动了关联关系的问题
	err = global.WM_DB.Transaction(func(tx *gorm.DB) error {
		db := tx.Model(&model.AmsAlarm{})
		err := db.Where("`id` = ?", r.Id).First(&entity).Error

		// 预警不存在
		if err != nil {
			if errors.Is(err, gorm.ErrRecordNotFound) {
				err = errors.New("该预警不存在")
			}
			return err
		}

		// 编辑内容
		// 预警名称
		if r.Name != "" {
			entity.Name = r.Name
		}
		// 项目标识
		if r.ProjectIdentifier != "" {
			entity.ProjectIdentifier = r.ProjectIdentifier
		}
		// 报警等级
		if r.Level != nil {
			entity.Level = *r.Level
		}
		// 过滤条件
		if r.Category != nil {
			entity.Category = *r.Category
		}
		// 预警规则
		if r.Rule != "" {
			entity.Rule = r.Rule
		}
		// 报警时段-开始时间
		if r.StartTime != "" {
			entity.StartTime = r.StartTime
		}
		// 报警时段-结束时间
		if r.EndTime != "" {
			entity.EndTime = r.EndTime
		}
		// 静默期
		if r.SilentPeriod != nil {
			entity.SilentPeriod = *r.SilentPeriod
		}
		// 是否启用
		if r.IsActive != nil {
			// 若状态改为启动，则先停止已有的定时任务，再重新启动对应的定时任务
			// TODO 停止已有的定时任务
			if *r.IsActive == 1 {
				// TODO 启动定时任务
			}
			entity.IsActive = *r.IsActive
		}
		// 是否已被删除
		if r.IsDeleted != nil {
			entity.IsDeleted = *r.IsDeleted
		}
		if len(r.SubscriberList) > 0 {
			alarmId := entity.Id
			// 先删除已有的关联关系
			err = DeleteAllByAlarmId(tx, alarmId)
			if err != nil {
				return err
			}

			// 再创建新的关联关系
			if len(r.SubscriberList) > 0 {
				for _, subscribe := range r.SubscriberList {
					err = AddSubscriber(tx, subscribe, alarmId)
					if err != nil {
						return err
					}
				}
			}
		}
		entity.UpdateTime = time.Now()
		err = db.Save(&entity).Error
		return err
	})

	if err != nil {
		global.WM_LOG.Error("编辑预警失败-事务回滚", zap.Any("err", err))
		return err, nil
	}

	return nil, entity
}

func GetAlarm(r validation.GetAlarm) (err error, data interface{}) {
	limit := r.PageSize
	offset := limit * (r.PageNum - 1)
	db := global.WM_DB.Model(&model.AmsAlarm{})
	var totalNum int64
	var records []model.AmsAlarm

	// 预警名称
	if r.Name != "" {
		db = db.Where("`name` = ?", r.Name)
	}
	// 项目标识
	if r.ProjectIdentifier != "" {
		db = db.Where("`project_identifier` = ?", r.ProjectIdentifier)
	}
	// 报警等级
	if r.Level != nil {
		db = db.Where("`level` = ?", r.Level)
	}
	// 过滤条件
	if r.Category != nil {
		db = db.Where("`category` = ?", r.Category)
	}
	// 预警规则
	if r.Rule != "" {
		db = db.Where("`rule` like ?", "%"+r.Rule+"%")
	}
	// 报警时段-开始时间
	if r.StartTime != "" {
		db = db.Where("`start_time` = ?", r.StartTime)
	}
	// 报警时段-结束时间
	if r.EndTime != "" {
		db = db.Where("`end_time` = ?", r.EndTime)
	}
	// 静默期
	if r.SilentPeriod != nil {
		db = db.Where("`silent_period` = ?", r.SilentPeriod)
	}
	// 是否启用
	if r.IsActive != nil {
		db = db.Where("`is_active` = ?", r.IsActive)
	}
	// 创建人ID
	if r.CreateBy != nil {
		db = db.Where("`create_by` = ?", r.CreateBy)
	}
	// 是否已被删除
	if r.IsDeleted != nil {
		db = db.Where("`is_deleted` = ?", r.IsDeleted)
	}

	err = db.Count(&totalNum).Error
	err = db.Limit(limit).Offset(offset).Find(&records).Error
	data = map[string]interface{}{
		"totalNum":  totalNum,
		"totalPage": math.Ceil(float64(totalNum) / float64(r.PageSize)),
		"pageNum":   r.PageNum,
		"pageSize":  r.PageSize,
		"records":   records,
	}
	return err, data
}

func DeleteAlarm(alarmId uint64) (err error, data interface{}) {
	var entity model.AmsAlarm

	// 使用事务的方式提交，避免中途异常后导致错误改动了关联关系的问题
	err = global.WM_DB.Transaction(func(tx *gorm.DB) error {

		db := tx.Model(&model.AmsAlarm{})
		err = db.Where("`id` = ?", alarmId).First(&entity).Error
		if errors.Is(err, gorm.ErrRecordNotFound) {
			return errors.New("预警不存在")
		}

		// 删除关联的订阅者
		err = DeleteAllByAlarmId(tx, alarmId)
		if err != nil {
			return err
		}

		// 停止预警定时任务
		// TODO 停止已有的定时任务

		// 删除实体记录
		err = db.Delete(&entity).Error
		return err
	})

	if err != nil {
		global.WM_LOG.Error("删除预警失败-事务回滚", zap.Any("err", err))
		return err, nil
	}

	return nil, true
}
