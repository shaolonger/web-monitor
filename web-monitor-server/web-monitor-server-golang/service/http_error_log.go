package service

import (
	"go.uber.org/zap"
	"math"
	"time"
	"web.monitor.com/global"
	"web.monitor.com/model"
	"web.monitor.com/model/response"
	"web.monitor.com/model/validation"
)

func AddHttpErrorLog(r validation.AddHttpErrorLog) (err error, data interface{}) {
	db := global.WM_DB.Model(&model.LmsHttpErrorLog{})

	// 保存实体
	log := model.LmsHttpErrorLog{
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
		HttpType:          r.HttpType,
		HttpUrlComplete:   r.HttpUrlComplete,
		HttpUrlShort:      r.HttpUrlShort,
		Status:            r.Status,
		StatusText:        r.StatusText,
		ResTime:           r.ResTime,
	}
	err = db.Create(&log).Error
	if err != nil {
		global.WM_LOG.Error("保存http异常日志失败", zap.Any("err", err))
		return err, false
	}
	return nil, true
}

func GetHttpErrorLog(r validation.GetHttpErrorLog) (err error, data interface{}) {
	limit := r.PageSize
	offset := limit * (r.PageNum - 1)
	db := global.WM_DB.Model(&model.LmsHttpErrorLog{})
	var totalNum int64
	var records []model.LmsHttpErrorLog

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
	// Http类型，如request、response
	if r.HttpType != "" {
		db = db.Where("`http_type` like ?", "%"+r.HttpType+"%")
	}
	// 完整的Http请求地址
	if r.HttpUrlComplete != "" {
		db = db.Where("`http_url_complete` like ?", "%"+r.HttpUrlComplete+"%")
	}
	// 缩写的Http请求地址
	if r.HttpUrlShort != "" {
		db = db.Where("`http_url_short` like ?", "%"+r.HttpUrlShort+"%")
	}
	// 状态
	if r.Status != "" {
		db = db.Where("`status` like ?", "%"+r.Status+"%")
	}
	// 状态的文字描述
	if r.StatusText != "" {
		db = db.Where("`status_text` like ?", "%"+r.StatusText+"%")
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

func GetHttpErrorLogByGroup(r validation.GetHttpErrorLogByGroup) (err error, data interface{}) {
	limit := r.PageSize
	offset := limit * (r.PageNum - 1)
	db := global.WM_DB.Model(&model.LmsHttpErrorLog{})
	var totalNum int64
	var records []response.GetHttpErrorLogByGroup

	// 基础查询
	dbCount := global.WM_DB.Model(&model.LmsHttpErrorLog{})
	dbData := db.Select("count(id) as count, max(create_time) as latest_record_time, count(distinct c_uuid) as affect_user_count, http_url_complete")

	// 日志类型
	if r.LogType != "" {
		dbCount = dbCount.Where("`log_type` like ?", "%"+r.LogType+"%")
		dbData = dbData.Where("`log_type` like ?", "%"+r.LogType+"%")
	}
	// 项目标识
	if r.ProjectIdentifier != "" {
		dbCount = dbCount.Where("`project_identifier` = ?", r.ProjectIdentifier)
		dbData = dbData.Where("`project_identifier` = ?", r.ProjectIdentifier)
	}
	// 用户名
	if r.Buname != "" {
		dbCount = dbCount.Where("`b_uname` like ?", "%"+r.Buname+"%")
		dbData = dbData.Where("`b_uname` like ?", "%"+r.Buname+"%")
	}
	// 页面URL
	if r.PageUrl != "" {
		dbCount = dbCount.Where("`page_url` like ?", "%"+r.PageUrl+"%")
		dbData = dbData.Where("`page_url` like ?", "%"+r.PageUrl+"%")
	}
	// Http类型，如request、response
	if r.HttpType != "" {
		dbCount = dbCount.Where("`http_type` like ?", "%"+r.HttpType+"%")
		dbData = dbData.Where("`http_type` like ?", "%"+r.HttpType+"%")
	}
	// 完整的Http请求地址
	if r.HttpUrlComplete != "" {
		dbCount = dbCount.Where("`http_url_complete` like ?", "%"+r.HttpUrlComplete+"%")
		dbData = dbData.Where("`http_url_complete` like ?", "%"+r.HttpUrlComplete+"%")
	}
	// 缩写的Http请求地址
	if r.HttpUrlShort != "" {
		dbCount = dbCount.Where("`http_url_short` like ?", "%"+r.HttpUrlShort+"%")
		dbData = dbData.Where("`http_url_short` like ?", "%"+r.HttpUrlShort+"%")
	}
	// 状态
	if r.Status != "" {
		dbCount = dbCount.Where("`status` like ?", "%"+r.Status+"%")
		dbData = dbData.Where("`status` like ?", "%"+r.Status+"%")
	}
	// 状态的文字描述
	if r.StatusText != "" {
		dbCount = dbCount.Where("`status_text` like ?", "%"+r.StatusText+"%")
		dbData = dbData.Where("`status_text` like ?", "%"+r.StatusText+"%")
	}

	// 开始时间、结束时间
	if r.StartTime != "" && r.EndTime != "" {
		dbCount = dbCount.Where("`create_time` BETWEEN ? AND ?", r.StartTime, r.EndTime)
		dbData = dbData.Where("`create_time` BETWEEN ? AND ?", r.StartTime, r.EndTime)
	} else if r.StartTime != "" {
		dbCount = dbCount.Where("`create_time` >= ?", r.StartTime)
		dbData = dbData.Where("`create_time` >= ?", r.StartTime)
	} else if r.EndTime != "" {
		dbCount = dbCount.Where("`create_time` <= ?", r.EndTime)
		dbData = dbData.Where("`create_time` <= ?", r.EndTime)
	}

	// 分组
	dbCount = dbCount.Group("http_url_complete")
	dbData = dbData.Group("http_url_complete").Order("count desc")

	// 计算总计
	err = dbCount.Count(&totalNum).Error
	if err != nil {
		return err, nil
	}

	// 分组
	err = dbData.Limit(limit).Offset(offset).Find(&records).Error

	data = map[string]interface{}{
		"totalNum":  totalNum,
		"totalPage": math.Ceil(float64(totalNum) / float64(r.PageSize)),
		"pageNum":   r.PageNum,
		"pageSize":  r.PageSize,
		"records":   records,
	}
	return err, data
}
