package model

import "time"

type PmsProject struct {
	Id                uint64     `json:"id" gorm:"primaryKey;index;unique;autoIncrement;comment:ID"`
	ProjectName       string     `json:"projectName" gorm:"not null;type:varchar(100);comment:项目名"`
	ProjectIdentifier string     `json:"projectIdentifier" gorm:"not null;type:varchar(200);comment:项目标识符"`
	Description       string     `json:"description" gorm:"type:varchar(200);comment:项目描述"`
	AccessType        string     `json:"accessType" gorm:"type:varchar(20);comment:接入类型，script、npm"`
	ActiveFuncs       string     `json:"activeFuncs" gorm:"type:varchar(100);comment:开启功能，JS异常、HTTP异常、静态资源异常、自定义异常"`
	IsAutoUpload      uint8      `json:"isAutoUpload" gorm:"not null;type:tinyint(1);comment:是否自动上报，0-否，1-是"`
	CreateTime        time.Time  `json:"createTime" gorm:"not null;comment:创建时间，格式为yyyy-MM-dd HH:mm:ss"`
	UpdateTime        time.Time  `json:"updateTime" gorm:"comment:更新时间，格式为yyyy-MM-dd HH:mm:ss"`
	NotifyDtToken     string     `json:"notifyDtToken" gorm:"type:text;comment:预警模块中的钉钉机器人access_token，用于预警模块中发送报警推送，多个用英文逗号隔开"`
	NotifyEmail       string     `json:"notifyEmail" gorm:"type:text;comment:预警模块中的邮件推送地址，多个用英文逗号隔开"`
	UmsUsers          []*UmsUser `json:"-" gorm:"many2many:ums_user_project_relation;"`
}

func (p PmsProject) TableName() string {
	return "pms_project"
}
