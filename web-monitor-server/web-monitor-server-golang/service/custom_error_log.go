package service

import (
	"go.uber.org/zap"
	"math"
	"time"
	"web.monitor.com/global"
	"web.monitor.com/model"
	"web.monitor.com/model/validation"
)

func AddCustomErrorLog(r validation.AddCustomErrorLog) (err error, data interface{}) {
	db := global.WM_DB.Model(&model.LmsCustomErrorLog{})

	// 保存实体
	log := model.LmsCustomErrorLog{
		LogType:           r.LogType,
		ProjectIdentifier: r.ProjectIdentifier,
		CreateTime:        time.Now(),
		Cuuid:             r.Cuuid,
		Buid:              r.Buid,
		Buname:            r.Buname,
		PageUrl:           r.PageUrl,
		PageKey:           r.PageKey,
		DeviceName:        r.DeviceName,
		Os:                r.Os,
		OsVersion:         r.OsVersion,
		BrowserName:       r.BrowserName,
		BrowserVersion:    r.BrowserVersion,
		IpAddress:         r.IpAddress,
		Address:           r.Address,
		NetType:           r.NetType,
		ErrorType:         r.ErrorType,
		ErrorMessage:      r.ErrorMessage,
		ErrorStack:        r.ErrorStack,
	}
	err = db.Create(&log).Error
	if err != nil {
		global.WM_LOG.Error("保存custom异常日志失败", zap.Any("err", err))
		return err, false
	}
	return nil, true
}

func GetCustomErrorLog(r validation.GetCustomErrorLog) (err error, data interface{}) {
	limit := r.PageSize
	offset := limit * (r.PageNum - 1)
	db := global.WM_DB.Model(&model.LmsCustomErrorLog{})
	var totalNum int64
	var records []model.LmsCustomErrorLog

	// 日志类型
	if r.LogType != "" {
		db = db.Where("`log_type` like ?", "%"+r.LogType+"%")
	}
	// 项目标识
	if r.ProjectIdentifier != "" {
		db = db.Where("`project_identifier` = ?", r.ProjectIdentifier)
	}
	// 用户名
	if r.Buname != "" {
		db = db.Where("`b_uname` like ?", "%"+r.Buname+"%")
	}
	// 页面URL
	if r.PageUrl != "" {
		db = db.Where("`page_url` like ?", "%"+r.PageUrl+"%")
	}
	// JS错误类型
	if r.ErrorType != "" {
		db = db.Where("`error_type` like ?", "%"+r.ErrorType+"%")
	}
	// JS错误信息
	if r.ErrorMessage != "" {
		db = db.Where("`error_message` like ?", "%"+r.ErrorMessage+"%")
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
