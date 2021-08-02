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

func AddResourceLoadErrorLog(r validation.AddResourceLoadErrorLog) (err error, data interface{}) {
	db := global.WM_DB.Model(&model.LmsResourceLoadErrorLog{})

	// 保存实体
	log := model.LmsResourceLoadErrorLog{
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
		ResourceUrl:       r.ResourceUrl,
		ResourceType:      r.ResourceType,
		Status:            r.Status,
	}
	err = db.Create(&log).Error
	if err != nil {
		global.WM_LOG.Error("保存http异常日志失败", zap.Any("err", err))
		return err, false
	}
	return nil, true
}

func GetResourceLoadErrorLog(r validation.GetResourceLoadErrorLog) (err error, data interface{}) {
	limit := r.PageSize
	offset := limit * (r.PageNum - 1)
	db := global.WM_DB.Model(&model.LmsResourceLoadErrorLog{})
	var totalNum int64
	var records []model.LmsResourceLoadErrorLog

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
	// 资源链接
	if r.ResourceUrl != "" {
		db = db.Where("`resource_url` like ?", "%"+r.ResourceUrl+"%")
	}
	// 资源类型
	if r.ResourceType != "" {
		db = db.Where("`resource_type` like ?", "%"+r.ResourceType+"%")
	}
	// 状态
	if r.Status != "" {
		db = db.Where("`status` like ?", "%"+r.Status+"%")
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

func GetResourceLoadErrorLogByGroup(r validation.GetResourceLoadErrorLogByGroup) (err error, data interface{}) {
	limit := r.PageSize
	offset := limit * (r.PageNum - 1)
	db := global.WM_DB.Model(&model.LmsResourceLoadErrorLog{})
	var totalNum int64
	var records []response.GetResourceLoadErrorLogByGroup

	// 基础查询
	dbCount := global.WM_DB.Model(&model.LmsResourceLoadErrorLog{})
	dbData := db.Select("count(id) as count, max(create_time) as latest_record_time, count(distinct c_uuid) as affect_user_count, resource_url as error_message")

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
	// 资源链接
	if r.ResourceUrl != "" {
		dbCount = dbCount.Where("`resource_url` like ?", "%"+r.ResourceUrl+"%")
		dbData = dbData.Where("`resource_url` like ?", "%"+r.ResourceUrl+"%")
	}
	// 资源类型
	if r.ResourceType != "" {
		dbCount = dbCount.Where("`resource_type` like ?", "%"+r.ResourceType+"%")
		dbData = dbData.Where("`resource_type` like ?", "%"+r.ResourceType+"%")
	}
	// 状态
	if r.Status != "" {
		dbCount = dbCount.Where("`status` like ?", "%"+r.Status+"%")
		dbData = dbData.Where("`status` like ?", "%"+r.Status+"%")
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
	dbCount = dbCount.Group("resource_url")
	dbData = dbData.Group("resource_url").Order("count desc")

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

func GetOverallByTimeRange(r validation.GetOverallByTimeRange) (err error, data interface{}) {
	var result response.GetOverallByTimeRange
	db := global.WM_DB.Model(&model.LmsResourceLoadErrorLog{})

	// 筛选条件
	db = db.Select("count(id) as affect_counts, count(distinct page_url) as affect_pages, count(distinct c_uuid) as affect_users")
	db = db.Where("`project_identifier` = ?", r.ProjectIdentifier)
	db = db.Where("`create_time` BETWEEN ? AND ?", r.StartTime, r.EndTime)

	err = db.Scan(&result).Error
	if err != nil {
		return err, nil
	}
	return nil, result
}

// GetResCountByIdBetweenStartTimeAndEndTime 查询某个时间段内的日志总数
func GetResCountByIdBetweenStartTimeAndEndTime(projectIdentifier string, startTime string, endTime string) (error, int64) {
	var err error
	var count int64
	db := global.WM_DB.Model(&model.LmsResourceLoadErrorLog{})
	db = db.Where("`project_identifier` = ?", projectIdentifier)
	db = db.Where("`create_time` BETWEEN ? AND ?", startTime, endTime)
	err = db.Count(&count).Error
	return err, count
}

// GetResLogCountByHours 按小时间隔获取各小时内的日志数量
func GetResLogCountByHours(projectIdentifier string, startTime time.Time, endTime time.Time) (error, []response.GetLogCountByHours) {
	var err error
	var results []response.GetLogCountByHours
	db := global.WM_DB.Model(&model.LmsResourceLoadErrorLog{})
	db = db.Select("date_format(create_time, '%Y-%m-%d %H') as hour, count(id) as count")
	db = db.Where("`project_identifier` = ?", projectIdentifier)
	db = db.Where("`create_time` BETWEEN ? AND ?", startTime, endTime)
	db = db.Group("hour")
	err = db.Find(&results).Error
	return err, results
}

// GetResLogCountByDays 按天间隔获取各天内的日志数量
func GetResLogCountByDays(projectIdentifier string, startTime time.Time, endTime time.Time) (error, []response.GetLogCountByDays) {
	var err error
	var results []response.GetLogCountByDays
	db := global.WM_DB.Model(&model.LmsResourceLoadErrorLog{})
	db = db.Select("date_format(create_time, '%Y-%m-%d') as day, count(id) as count")
	db = db.Where("`project_identifier` = ?", projectIdentifier)
	db = db.Where("`create_time` BETWEEN ? AND ?", startTime, endTime)
	db = db.Group("day")
	err = db.Find(&results).Error
	return err, results
}

// GetResLogListByCreateTimeAndProjectIdentifier 获取时间间隔内的简易日志信息
func GetResLogListByCreateTimeAndProjectIdentifier(projectIdentifier string, startTime time.Time, endTime time.Time) (error, []response.GetLogListByCreateTimeAndProjectIdentifier) {
	var err error
	var results []response.GetLogListByCreateTimeAndProjectIdentifier
	db := global.WM_DB.Model(&model.LmsResourceLoadErrorLog{})
	db = db.Select("id, c_uuid, create_time")
	db = db.Where("`project_identifier` = ?", projectIdentifier)
	db = db.Where("`create_time` BETWEEN ? AND ?", startTime, endTime)
	err = db.Find(&results).Error
	return err, results
}

// GetResAllLogsBetweenStartTimeAndEndTime 获取时间间隔内的所有日志
func GetResAllLogsBetweenStartTimeAndEndTime(projectIdentifier string, startTime time.Time, endTime time.Time) (error, []response.GetAllLogsBetweenStartTimeAndEndTime) {
	var err error
	var results []response.GetAllLogsBetweenStartTimeAndEndTime
	db := global.WM_DB.Model(&model.LmsResourceLoadErrorLog{})
	db = db.Select("net_type, device_name, os, browser_name, resource_type, status")
	db = db.Where("`project_identifier` = ?", projectIdentifier)
	db = db.Where("`create_time` BETWEEN ? AND ?", startTime, endTime)
	err = db.Find(&results).Error
	return err, results
}

// CountResDistinctCUuidByCreateTimeBetween 获取时间范围内的影响用户数
func CountResDistinctCUuidByCreateTimeBetween(startTime time.Time, endTime time.Time) int64 {
	var count int64
	db := global.WM_DB.Model(&model.LmsResourceLoadErrorLog{})
	db.Where("`create_time` BETWEEN ? AND ?", startTime, endTime).Distinct("c_uuid").Count(&count)
	return count
}

// CountResByCreateTimeBetween 获取时间范围内的日志数
func CountResByCreateTimeBetween(startTime time.Time, endTime time.Time) int64 {
	var count int64
	db := global.WM_DB.Model(&model.LmsResourceLoadErrorLog{})
	db.Where("`create_time` BETWEEN ? AND ?", startTime, endTime).Count(&count)
	return count
}
