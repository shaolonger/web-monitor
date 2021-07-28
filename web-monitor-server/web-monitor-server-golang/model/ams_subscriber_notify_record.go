package model

import "time"

type AmsSubscriberNotifyRecord struct {
	Id            uint64    `json:"id" gorm:"primaryKey;autoIncrement;comment:ID"`
	AlarmRecordId uint64    `json:"alarmRecordId" gorm:"not null;comment:报警记录id"`
	SubscriberId  uint64    `json:"subscriberId" gorm:"not null;comment:报警订阅方id"`
	State         int8      `json:"state" gorm:"not null;type:tinyint(1);comment:通知状态，0-失败，1-成功"`
	Content       string    `json:"content" gorm:"not null;type:text;comment:通知内容"`
	CreateTime    time.Time `json:"createTime" gorm:"not null;comment:创建时间，格式为yyyy-MM-dd HH:mm:ss"`
}

func (a AmsSubscriberNotifyRecord) TableName() string {
	return "ams_subscriber_notify_record"
}
