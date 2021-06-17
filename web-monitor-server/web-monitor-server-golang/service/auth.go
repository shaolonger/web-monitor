package service

import (
	"errors"
	"gorm.io/gorm"
	"time"
	"web.monitor.com/global"
	"web.monitor.com/model"
	"web.monitor.com/model/validation"
)

func AddUserRegisterRecord(r validation.AddUserRegisterRecord) (err error, entity model.UmsUserRegisterRecord) {
	e := model.UmsUserRegisterRecord{
		Username:    r.Username,
		Password:    r.Password,
		Email:       r.Email,
		Phone:       r.Phone,
		Icon:        r.Icon,
		Gender:      r.Gender,
		CreateTime:  time.Now(),
		UpdateTime:  time.Now(),
		AuditResult: -1,
	}
	// 判断用户名是否已注册
	if !errors.Is(global.WM_DB.Where("username = ?", e.Username).First(&e).Error, gorm.ErrRecordNotFound) {
		return errors.New("用户已注册"), e
	}
	err = global.WM_DB.Create(&e).Error
	return err, e
}
