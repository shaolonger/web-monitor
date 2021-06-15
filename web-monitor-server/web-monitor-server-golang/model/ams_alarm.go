package model

import "time"

type AmsAlarm struct {
	Id                uint64    `json:"id" gorm:"comment:ID"`
	Name              string    `json:"name" gorm:"comment:预警名称"`
	Level             int8      `json:"level" gorm:"comment:报警等级，-1-P4低，0-P3中，1-P2高，2-P1紧急"`
	Category          int8      `json:"category" gorm:"comment:过滤条件。0-全部，1-JS_ERROR，2-HTTP_ERROR，3-RESOURCE_LOAD，4-CUSTOM_ERROR"`
	Rule              string    `json:"rule" gorm:"comment:预警规则，存放JSON格式"`
	StartTime         string    `json:"startTime" gorm:"comment:报警时段-开始时间，例如00:00:00"`
	EndTime           string    `json:"endTime" gorm:"comment:报警时段-结束时间，例如23:59:59"`
	SilentPeriod      int8      `json:"silentPeriod" gorm:"comment:静默期，0-不静默，1-5分钟，2-10分钟，3-15分钟，4-30分钟，5-1小时，6-3小时，7-12小时，8-24小时，9-当天"`
	IsActive          int8      `json:"isActive" gorm:"comment:是否启用，0-否，1-是"`
	CreateTime        time.Time `json:"createTime" gorm:"comment:创建时间，格式yyyy-MM-dd HH:mm:ss"`
	UpdateTime        time.Time `json:"updateTime" gorm:"comment:更新时间，格式yyyy-MM-dd HH:mm:ss"`
	CreateBy          uint64    `json:"createBy" gorm:"comment:创建人ID"`
	ProjectIdentifier string    `json:"projectIdentifier" gorm:"comment:项目标识"`
	IsDeleted         int8      `json:"isDeleted" gorm:"comment:是否已被删除，0-否，1-是"`
}
