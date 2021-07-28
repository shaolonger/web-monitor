package model

import "time"

type AmsAlarmSchedulerRelation struct {
	Id          uint64    `json:"id" gorm:"primaryKey;autoIncrement;comment:ID"`
	AlarmId     uint64    `json:"alarmId" gorm:"not null;comment:预警规则id"`
	SchedulerId uint64    `json:"schedulerId" gorm:"not null;comment:定时任务id"`
	CreateTime  time.Time `json:"createTime" gorm:"comment:创建时间，即通知时间，格式为yyyy-MM-dd HH:mm:ss"`
}

func (a AmsAlarmSchedulerRelation) TableName() string {
	return "ams_alarm_scheduler_relation"
}
