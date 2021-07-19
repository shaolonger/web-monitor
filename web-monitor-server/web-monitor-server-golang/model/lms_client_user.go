package model

import "time"

type LmsClientUser struct {
	Id         uint64    `json:"id" gorm:"primaryKey;autoIncrement;comment:ID"`
	Cuuid      string    `json:"cUuid" gorm:"not null;type:char(36);comment:客户端唯一识别码，全称client uuid"`
	Buid       uint64    `json:"bUid" gorm:"not null;comment:业务用户ID"`
	Buname     string    `json:"bUname" gorm:"not null;type:varchar(64);comment:业务用户名"`
	CreateTime time.Time `json:"createTime" gorm:"not null;comment:创建时间，格式为yyyy-MM-dd HH:mm:ss"`
	UpdateTime time.Time `json:"updateTime" gorm:"not null;comment:更新时间，格式为yyyy-MM-dd HH:mm:ss"`
}

func (l LmsClientUser) TableName() string {
	return "lms_client_user"
}
