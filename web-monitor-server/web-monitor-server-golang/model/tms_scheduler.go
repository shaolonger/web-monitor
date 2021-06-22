package model

import "time"

type TmsScheduler struct {
	Id             uint64    `json:"id" gorm:"primaryKey;autoIncrement;comment:ID"`
	BeanName       string    `json:"beanName" gorm:"not null;type:varchar(255);comment:bean名称"`
	MethodName     string    `json:"methodName" gorm:"not null;type:varchar(255);comment:bean中执行的方法名称"`
	Params         string    `json:"params" gorm:"type:text;comment:方法的参数内容，JSON格式"`
	CronExpression string    `json:"cronExpression" gorm:"type:varchar(255);comment:cron表达式"`
	CreateTime     time.Time `json:"createTime" gorm:"not null;comment:创建时间，格式为yyyy-MM-dd HH:mm:ss"`
	UpdateTime     time.Time `json:"updateTime" gorm:"comment:更新时间，格式为yyyy-MM-dd HH:mm:ss"`
	State          int8      `json:"state" gorm:"not null;type:tinyint(1);comment:执行状态，0-暂停，1-运行中"`
}

func (t TmsScheduler) TableName() string {
	return "tms_scheduler"
}
