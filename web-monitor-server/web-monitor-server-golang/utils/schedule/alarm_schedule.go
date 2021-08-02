package schedule

import (
	"encoding/json"
	"fmt"
	"go.uber.org/zap"
	"strconv"
	"strings"
	"time"
	"web.monitor.com/global"
	"web.monitor.com/model"
	"web.monitor.com/model/validation"
	"web.monitor.com/service"
	"web.monitor.com/utils"
)

// StartAlarmSchedule
// 预警定时任务执行的内容
func StartAlarmSchedule(params string) error {
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
				tempList = append(tempList, service.CheckIsExceedAlarmThreshold(tableName, &ruleItem))
			}
		}
		// 聚合分析
		setResultListByTempList(tempList, resultList)
	} else {
		tableName := tableNameMap[category]
		for _, ruleItem := range rule.Rules {
			resultList = append(resultList, service.CheckIsExceedAlarmThreshold(tableName, &ruleItem))
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
func setResultListByTempList(tempList []*validation.AlarmScheduleResult, resultList []*validation.AlarmScheduleResult) {
	for _, tempItem := range tempList {
		var resultItem *validation.AlarmScheduleResult
		for _, tempRItem := range resultList {
			if tempRItem.TargetInd == tempItem.TargetInd {
				resultItem = tempRItem
			}
		}
		if resultItem == nil {
			resultList = append(resultList, tempItem)
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
	subscriberEntityList := service.GetAllByAlarmId(alarm.Id)

	// 保存报警记录
	alarmData, err := json.Marshal(resultList)
	if err != nil {
		return err
	}
	alarmRecord := model.AmsAlarmRecord{
		AlarmId:    alarm.Id,
		AlarmData:  string(alarmData),
		CreateTime: time.Time{},
	}
	err = service.AddAlarmRecord(&alarmRecord)
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
			_, projectName := service.GetProjectNameByAlarmId(alarmId)
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
						// TODO 发送钉钉推送
						_ = service.AddSubscriberNotifyRecord(&subscriberNotifyRecord)
					}
				}
			} else if subscriber.Category == 2 {
				// TODO 发送邮件通知
				_ = service.AddSubscriberNotifyRecord(&subscriberNotifyRecord)
			}
		}
	}
}
