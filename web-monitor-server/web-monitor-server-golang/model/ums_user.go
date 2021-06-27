package model

import "time"

type UmsUser struct {
	Id          uint64        `json:"id" gorm:"primaryKey;index;unique;autoIncrement;comment:ID"`
	Username    string        `json:"username" gorm:"not null;type:varchar(64);comment:用户名"`
	Password    string        `json:"password" gorm:"not null;type:varchar(64);comment:密码"`
	Phone       string        `json:"phone" gorm:"type:varchar(64);comment:电话"`
	Icon        string        `json:"icon" gorm:"type:text;comment:头像"`
	Gender      int8          `json:"gender" gorm:"type:int;comment:性别，0-未知，1-男，2-女"`
	Email       string        `json:"email" gorm:"not null;type:varchar(100);comment:邮箱"`
	IsAdmin     int8          `json:"isAdmin" gorm:"type:int;comment:是否超级管理员，0-否，1-是"`
	CreateTime  time.Time     `json:"createTime" gorm:"not null;comment:创建时间，格式为yyyy-MM-dd HH:mm:ss"`
	UpdateTime  time.Time     `json:"updateTime" gorm:"comment:更新时间，格式为yyyy-MM-dd HH:mm:ss"`
	PmsProjects []*PmsProject `gorm:"many2many:ums_user_project_relation;"`
}

func (u UmsUser) TableName() string {
	return "ums_user"
}
