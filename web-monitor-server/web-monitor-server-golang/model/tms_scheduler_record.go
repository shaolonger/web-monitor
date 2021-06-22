package model

import "time"

type TmsSchedulerRecord struct {
	Id          uint64    `json:"id" gorm:"primaryKey;autoIncrement;comment:ID"`
	SchedulerId uint64    `json:"schedulerId" gorm:"not null;comment:定时任务id"`
	State       int8      `json:"state" gorm:"not null;type:tinyint(1);comment:执行状态，0-暂停，1-运行中"`
	CreateTime  time.Time `json:"createTime" gorm:"not null;comment:创建时间，格式为yyyy-MM-dd HH:mm:ss"`
	TimeCost    int8      `json:"timeCost" gorm:"type:int;comment:定时任务执行的时长，单位毫秒"`
	ErrorMsg    string    `json:"errorMsg" gorm:"type:text;comment:执行失败的异常信息"`
}

func (t TmsSchedulerRecord) TableName() string {
	return "tms_scheduler_record"
}
