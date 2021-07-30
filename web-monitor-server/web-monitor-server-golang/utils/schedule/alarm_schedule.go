package schedule

import (
	"encoding/json"
	"go.uber.org/zap"
	"web.monitor.com/global"
	"web.monitor.com/model"
)

// StartAlarmSchedule
// 预警定时任务执行的内容
func StartAlarmSchedule(params string) error {
	var entity model.AmsAlarm
	err := json.Unmarshal([]byte(params), &entity)
	if err != nil {
		global.WM_LOG.Error("预警定时任务执行失败", zap.Any("err", err))
		return err
	}
	return nil
}
