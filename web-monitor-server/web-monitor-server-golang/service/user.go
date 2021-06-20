package service

import (
	"errors"
	"gorm.io/gorm"
	"math"
	"time"
	"web.monitor.com/global"
	"web.monitor.com/model"
	"web.monitor.com/model/validation"
	"web.monitor.com/utils"
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

func GetUserRegisterRecord(r validation.GetUserRegisterRecord) (err error, data interface{}) {
	limit := r.PageSize
	offset := limit * (r.PageNum - 1)
	db := global.WM_DB.Model(&model.UmsUserRegisterRecord{})
	var totalNum int64
	var records []model.UmsUserRegisterRecord

	// 审核结果
	if r.AuditResult != "" {
		db = db.Where("`audit_result` = ?", r.AuditResult)
	}
	// 开始时间、结束时间
	if r.StartTime != "" && r.EndTime != "" {
		db = db.Where("`create_time` BETWEEN ? AND ?", r.StartTime, r.EndTime)
	} else if r.StartTime != "" {
		db = db.Where("`create_time` >= ?", r.StartTime)
	} else if r.EndTime != "" {
		db = db.Where("`create_time` <= ?", r.EndTime)
	}

	err = db.Count(&totalNum).Error
	err = db.Limit(limit).Offset(offset).Find(&records).Error
	data = map[string]interface{}{
		"totalNum":  totalNum,
		"totalPage": math.Ceil(float64(totalNum) / float64(r.PageSize)),
		"pageNum":   r.PageNum,
		"pageSize":  r.PageSize,
		"records":   records,
	}
	return err, data
}

func Login(r validation.Login) (err error, data interface{}) {
	db := global.WM_DB.Model(&model.UmsUser{})
	db = db.Where("`username` = ? AND `password` = ?", r.Username, r.Password)
	var userList []model.UmsUser
	err = db.Find(&userList).Error

	// 找不到用户
	if len(userList) == 0 {
		err = errors.New("用户名或密码不正确")
		data = nil
		return
	}

	// 创建token
	token := utils.GetToken()
	user := userList[0]
	err, loginUser := AddOrUpdateToken(token, user.Id, user.Username, user.IsAdmin)
	if err != nil {
		return
	} else {
		return nil, loginUser
	}
}
