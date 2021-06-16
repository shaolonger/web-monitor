package model

import "time"

type AmsAlarmSchedulerRelation struct {
	Id          uint64    `json:"id" gorm:"primaryKey;autoIncrement;comment:ID"`
	AlarmId     uint64    `json:"alarmId" gorm:"not null;comment:预警规则id"`
	SchedulerId uint64    `json:"schedulerId" gorm:"not null;comment:定时任务id"`
	IsActive    int8      `json:"isActive" gorm:"not null;type:tinyint(1);comment:是否启用，0-否，1-是"`
	Category    int8      `json:"category" gorm:"not null;type:tinyint(1);comment:订阅类型，1-钉钉机器人，2-邮箱"`
	CreateTime  time.Time `json:"createTime" gorm:"comment:创建时间，即通知时间，格式为yyyy-MM-dd HH:mm:ss"`
}
