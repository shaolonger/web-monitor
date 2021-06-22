package model

import "time"

type AmsSubscriber struct {
	Id          uint64    `json:"id" gorm:"primaryKey;autoIncrement;comment:ID"`
	AlarmId     uint64    `json:"alarmId" gorm:"not null;comment:预警规则id"`
	Subscriber  string    `json:"subscriber" gorm:"type:text;comment:报警订阅方，内容为JSON格式，例如[]。1、若订阅方式为钉钉，则内容为钉钉的机器人Webhook的access_token2、若为邮件订阅，则内容为邮件地址"`
	SchedulerId uint64    `json:"schedulerId" gorm:"not null;comment:定时任务id"`
	CreateTime  time.Time `json:"createTime" gorm:"not null;comment:创建时间，格式为yyyy-MM-dd HH:mm:ss"`
}

func (a AmsSubscriber) TableName() string {
	return "ams_subscriber"
}
