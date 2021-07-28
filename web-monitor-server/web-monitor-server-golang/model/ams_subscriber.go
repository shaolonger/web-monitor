package model

type AmsSubscriber struct {
	Id         uint64 `json:"id" gorm:"primaryKey;autoIncrement;comment:ID"`
	AlarmId    uint64 `json:"alarmId" gorm:"not null;comment:预警规则id"`
	Subscriber string `json:"subscriber" gorm:"type:text;comment:报警订阅方，内容为JSON格式，例如[]。1、若订阅方式为钉钉，则内容为钉钉的机器人Webhook的access_token2、若为邮件订阅，则内容为邮件地址"`
	IsActive   int8   `json:"isActive" gorm:"not null;type:tinyint(1);comment:是否启用，0-否，1-是"`
	Category   int8   `json:"category" gorm:"not null;type:tinyint(1);comment:订阅类型，1-钉钉机器人，2-邮箱"`
}

func (a AmsSubscriber) TableName() string {
	return "ams_subscriber"
}
