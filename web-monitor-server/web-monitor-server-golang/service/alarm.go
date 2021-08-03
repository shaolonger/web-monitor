package service

import (
	"encoding/json"
	"errors"
	"fmt"
	"go.uber.org/zap"
	"gorm.io/gorm"
	"math"
	"strconv"
	"strings"
	"time"
	"web.monitor.com/global"
	"web.monitor.com/model"
	"web.monitor.com/model/validation"
	"web.monitor.com/utils"
	"web.monitor.com/utils/dingtalk"
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

		// 启动预警定时任务
		if entity.IsActive == 1 {
			err = startAlarmScheduler(tx, &entity)
			if err != nil {
				return err
			}
		}

		return nil
	})

	if err != nil {
		global.WM_LOG.Error("新增预警失败-事务回滚", zap.Any("err", err))
		return err, nil
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

// 启动预警定时任务
func startAlarmScheduler(tx *gorm.DB, entity *model.AmsAlarm) error {
	params, err := utils.StructToJson(entity)
	if err != nil {
		global.WM_LOG.Error("启动预警定时任务失败", zap.Any("err", err))
		return err
	}

	// 保存定时任务
	err, schedulerEntity := AddScheduler(tx, params)
	if err != nil {
		return err
	}

	// 保存预警-定时任务关联表
	alarmId := entity.Id
	schedulerId := schedulerEntity.Id
	err = AddAlarmSchedulerRelation(tx, alarmId, schedulerId)
	if err != nil {
		return err
	}

	// 创建定时任务并启动
	s := utils.Scheduler{
		BeanName:       schedulerEntity.BeanName,
		MethodName:     schedulerEntity.MethodName,
		Params:         schedulerEntity.Params,
		SchedulerId:    schedulerEntity.Id,
		CronExpression: schedulerEntity.CronExpression,
	}
	err = s.Start(func(params string) error {
		var errTemp error
		state := 0
		errorMsg := ""
		timeBefore := time.Now()

		global.WM_LOG.Info("定时任务开始执行", zap.Any("info", fmt.Sprintf("bean：%v，方法：%v，参数：%v", s.BeanName, s.MethodName, s.Params)))

		// 要执行的任务
		errTemp = startAlarmSchedule(params)

		if errTemp != nil {
			errorMsg = errTemp.Error()
			global.WM_LOG.Info("定时任务执行异常", zap.Any("info", fmt.Sprintf("bean：%v，方法：%v，参数：%v，异常：%v", s.BeanName, s.MethodName, s.Params, errTemp)))
		} else {
			state = 1
		}
		timeAfter := time.Now()
		timeCost := timeAfter.Sub(timeBefore).Milliseconds()
		global.WM_LOG.Info("定时任务执行结束", zap.Any("info", fmt.Sprintf("bean：%v，方法：%v，参数：%v，耗时：%v毫秒", s.BeanName, s.MethodName, s.Params, timeCost)))

		// 保存定时任务执行记录
		// 这里由于cron库的定时任务是通过goroutine启动的异步函数，因此下面保存无法用tx的方式，要么只能使用gorm的手动控制事务的方式
		// gorm手动事务的文档地址：https://gorm.io/docs/transactions.html
		db := global.WM_DB.Model(&model.TmsSchedulerRecord{})
		record := model.TmsSchedulerRecord{
			SchedulerId: s.SchedulerId,
			State:       int8(state),
			CreateTime:  time.Now(),
			TimeCost:    int8(timeCost),
			ErrorMsg:    errorMsg,
		}
		errTemp = db.Save(&record).Error

		return errTemp
	})

	return err
}

// GetProjectNameByAlarmId 根据alarmId获取关联的项目名称
func GetProjectNameByAlarmId(alarmId uint64) (error, string) {
	db := global.WM_DB.Model(&model.AmsAlarm{})
	var err error
	var alarm model.AmsAlarm
	err = db.Where("`id` = ?", alarmId).First(&alarm).Error
	if err != nil {
		if errors.Is(err, gorm.ErrRecordNotFound) {
			err = errors.New("找不到预警信息")
		}
		return err, ""
	}
	err, projectName := GetProjectNameByProjectIdentifier(alarm.ProjectIdentifier)
	if err != nil {
		return err, ""
	}
	return nil, projectName
}

// startAlarmSchedule 预警定时任务执行的内容
func startAlarmSchedule(params string) error {
	var entity model.AmsAlarm
	err := json.Unmarshal([]byte(params), &entity)
	if err != nil {
		global.WM_LOG.Error("定时任务执行失败-解析params参数异常", zap.Any("err", err))
		return err
	}

	// 获取预警规则
	var rule validation.SchedulerRule
	err = json.Unmarshal([]byte(entity.Rule), &rule)
	if err != nil {
		return err
	}

	// 过滤条件
	category := entity.Category
	tableNameMap := map[int8]string{
		1: "lms_js_error_log",
		2: "lms_http_error_log",
		3: "lms_resource_load_error_log",
		4: "lms_custom_error_log",
	}

	resultList := make([]*validation.AlarmScheduleResult, 0)

	// 根据category过滤条件，查询对应的日志表
	if category == 0 {
		tempList := make([]*validation.AlarmScheduleResult, 0)
		for _, tableName := range tableNameMap {
			for _, ruleItem := range rule.Rules {
				tempList = append(tempList, CheckIsExceedAlarmThreshold(tableName, &ruleItem))
			}
		}
		// 聚合分析
		setResultListByTempList(&tempList, &resultList)
	} else {
		tableName := tableNameMap[category]
		for _, ruleItem := range rule.Rules {
			resultList = append(resultList, CheckIsExceedAlarmThreshold(tableName, &ruleItem))
		}
	}

	// 根据条件规则判断是否触发报警
	op := rule.Op
	isAnd := true
	isOr := false
	for _, resultItem := range resultList {
		isExceedAlarmThreshold := resultItem.IsExceedAlarmThreshold
		isAnd = isAnd && isExceedAlarmThreshold
		isOr = isOr || isExceedAlarmThreshold
		if isExceedAlarmThreshold {
			// 整合报警内容
			//thresholdValue := strconv.FormatFloat(resultItem.ThresholdValue, 'f', 2, 64)
			//actualValue := strconv.FormatFloat(resultItem.ActualValue, 'f', 2, 64)
			//alarmContent := "[预警指标]" + string(resultItem.TargetInd) + ", " + "[预警阈值]" + thresholdValue + ", " + "[实际值]" + actualValue
		}
	}
	if (op == "&&" && isAnd) || (op == "||" && isOr) {
		// 触发预警条件，添加报警记录
		global.WM_LOG.Info("[预警定时任务]", zap.Any("info", fmt.Sprintf("预警名称：%v，报警内容：%v", entity.Name, resultList)))
		err := saveAlarmRecordAndNotifyAllSubscribers(&entity, resultList)
		if err != nil {
			return err
		}
	}

	return nil
}

// 当category为0时，即选择的过滤条件为全部，此时需要对各个表的计算结果进行聚合分析
func setResultListByTempList(tempList *[]*validation.AlarmScheduleResult, resultList *[]*validation.AlarmScheduleResult) {
	for _, tempItem := range *tempList {
		var resultItem *validation.AlarmScheduleResult
		for _, tempRItem := range *resultList {
			if tempRItem.TargetInd == tempItem.TargetInd {
				resultItem = tempRItem
			}
		}
		if resultItem == nil {
			*resultList = append(*resultList, tempItem)
		} else {
			thresholdValue := resultItem.ThresholdValue
			oldActualValue := resultItem.ActualValue
			newActualValue := oldActualValue + tempItem.ActualValue
			resultItem.ActualValue = newActualValue
			resultItem.IsExceedAlarmThreshold = newActualValue > thresholdValue
		}
	}
}

// 保存报警记录，同时通知所有报警订阅方
func saveAlarmRecordAndNotifyAllSubscribers(alarm *model.AmsAlarm, resultList []*validation.AlarmScheduleResult) error {
	var err error
	subscriberEntityList := GetAllByAlarmId(alarm.Id)

	// 保存报警记录
	alarmData, err := json.Marshal(resultList)
	if err != nil {
		return err
	}
	alarmRecord := model.AmsAlarmRecord{
		AlarmId:    alarm.Id,
		AlarmData:  string(alarmData),
		CreateTime: time.Now(),
	}
	err = AddAlarmRecord(&alarmRecord)
	if err != nil {
		return err
	}

	// 通知所有报警订阅方
	if len(resultList) > 0 && len(subscriberEntityList) > 0 {
		notifyAllSubscribers(alarm.Id, alarmRecord.AlarmId, resultList, subscriberEntityList)
	}

	return nil
}

// 通知所有报警订阅方
func notifyAllSubscribers(alarmId uint64, alarmRecordId uint64, resultList []*validation.AlarmScheduleResult, subscriberEntityList []*model.AmsSubscriber) {
	for _, subscriber := range subscriberEntityList {
		if subscriber.IsActive == 1 {
			subscriberNotifyRecord := model.AmsSubscriberNotifyRecord{
				AlarmRecordId: alarmRecordId,
				SubscriberId:  subscriber.Id,
			}

			// 设置通知内容
			content := strings.Builder{}
			_, projectName := GetProjectNameByAlarmId(alarmId)
			alarmTime := utils.GetCurrentTimeByDefaultLayout()
			content.WriteString("项目名：")
			content.WriteString(projectName)
			content.WriteString("\n")
			content.WriteString("报警时间：")
			content.WriteString(alarmTime)
			content.WriteString("\n")
			content.WriteString("报警内容：")
			content.WriteString("\n")
			for index, resultItem := range resultList {
				thresholdValue := strconv.FormatFloat(resultItem.ThresholdValue, 'f', 2, 64)
				actualValue := strconv.FormatFloat(resultItem.ActualValue, 'f', 2, 64)
				startTimeStr := utils.PassTimeToStrByDefaultLayout(resultItem.StartTime)

				// 简化endTimeStr，如果与startTime年月日相同，则省略
				preStart := utils.PassTimeToStrByLayout(resultItem.StartTime, "2006-01-02")
				preEnd := utils.PassTimeToStrByLayout(resultItem.EndTime, "2006-01-02")
				var endTimePattern string
				if preStart == preEnd {
					endTimePattern = "15:04:05"
				} else {
					endTimePattern = utils.DefaultLayout
				}
				endTimeStr := utils.PassTimeToStrByLayout(resultItem.EndTime, endTimePattern)

				content.WriteString(string(rune(index + 1)))
				content.WriteString(".")
				content.WriteString("[")
				content.WriteString(resultItem.TargetInd)
				content.WriteString("]")
				content.WriteString("区间：")
				content.WriteString(startTimeStr)
				content.WriteString("至")
				content.WriteString(endTimeStr)
				content.WriteString("，")
				content.WriteString("阈值：")
				content.WriteString(thresholdValue)
				content.WriteString("，")
				content.WriteString(actualValue)
				if index < len(resultList) {
					content.WriteString("\n")
				}
			}
			subscriberNotifyRecord.Content = content.String()

			if subscriber.Category == 1 {
				// 钉钉机器人
				// 若为钉钉机器人，则subscriberList为access_token列表，多个以逗号隔开
				subscriberList := strings.Split(subscriber.Subscriber, ",")
				for _, accessToken := range subscriberList {
					if accessToken != "" {

						// config.yaml配置文件中,获取钉钉推送关键词
						keyword := global.WM_CONFIG.Dingtalk.Keyword

						// 从application配置文件中，获取是否开启钉钉推送
						isEnableDingTalk := global.WM_CONFIG.Dingtalk.Enable
						if isEnableDingTalk {
							config := &dingtalk.Config{
								AccessToken: accessToken,
								KeyWord:     keyword,
							}
							err, client := dingtalk.NewClient(config)
							if err != nil {
								global.WM_LOG.Error("发送钉钉机器人通知失败", zap.Any("err", err))
							}
							params := dingtalk.GetTextParams()
							params.Text = dingtalk.TextContent{
								Content: keyword + "\n" + content.String(),
							}
							err = client.SendText(params)
							if err != nil {
								global.WM_LOG.Error("发送钉钉机器人通知失败", zap.Any("err", err))
								subscriberNotifyRecord.State = 0
							} else {
								subscriberNotifyRecord.State = 1
							}
						} else {
							subscriberNotifyRecord.State = 0
						}
						_ = AddSubscriberNotifyRecord(&subscriberNotifyRecord)
					}
				}
			} else if subscriber.Category == 2 {
				// TODO 发送邮件通知
				_ = AddSubscriberNotifyRecord(&subscriberNotifyRecord)
			}
		}
	}
}
