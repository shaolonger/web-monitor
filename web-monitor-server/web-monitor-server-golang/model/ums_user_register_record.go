package model

import "time"

type UmsUserRegisterRecord struct {
	Id          uint64    `json:"id" gorm:"primaryKey;autoIncrement;comment:ID"`
	Username    string    `json:"username" gorm:"not null;type:varchar(64);comment:用户名"`
	Password    string    `json:"password" gorm:"not null;type:varchar(64);comment:密码"`
	Email       string    `json:"email" gorm:"not null;type:varchar(100);comment:邮箱"`
	Phone       string    `json:"phone" gorm:"type:varchar(64);comment:电话"`
	Icon        string    `json:"icon" gorm:"type:text;comment:头像"`
	Gender      int8      `json:"gender" gorm:"type:int;comment:性别，0-未知，1-男，2-女"`
	CreateTime  time.Time `json:"createTime" gorm:"not null;comment:创建时间，格式为yyyy-MM-dd HH:mm:ss"`
	UpdateTime  time.Time `json:"updateTime" gorm:"comment:更新时间，格式为yyyy-MM-dd HH:mm:ss"`
	AuditUser   uint64    `json:"auditUser" gorm:"comment:审批人"`
	AuditResult int8      `json:"auditResult" gorm:"type:tinyint(1);comment:审批结果，-1-未审核，0-不通过，1-通过"`
}

func (u UmsUserRegisterRecord) TableName() string {
	return "ums_user_register_record"
}
