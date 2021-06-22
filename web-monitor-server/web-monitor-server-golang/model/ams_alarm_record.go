package model

import "time"

type AmsAlarmRecord struct {
	Id         uint64    `json:"id" gorm:"primaryKey;autoIncrement;comment:ID"`
	AlarmId    uint64    `json:"alarmId" gorm:"not null;comment:预警规则id"`
	AlarmData  string    `json:"alarmData" gorm:"not null;type:text;comment:报警内容，格式为JSON字符串"`
	CreateTime time.Time `json:"createTime" gorm:"not null;comment:创建时间，格式为yyyy-MM-dd HH:mm:ss"`
}

func (a AmsAlarmRecord) TableName() string {
	return "ams_alarm_record"
}
