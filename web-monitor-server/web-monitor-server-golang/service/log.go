package service

import (
	"errors"
	"go.uber.org/zap"
	"gorm.io/gorm"
	"math"
	"time"
	"web.monitor.com/global"
	"web.monitor.com/model"
	"web.monitor.com/model/validation"
	"web.monitor.com/utils"
)

func AddClient(r validation.AddClient) (err error, data interface{}) {
	db := global.WM_DB.Model(&model.LmsClientUser{})

	// 保存实体
	log := model.LmsClientUser{
		CreateTime: time.Now(),
		UpdateTime: time.Now(),
		Cuuid:      r.Cuuid,
		Buid:       r.Buid,
		Buname:     r.Buname,
	}
	err = db.Create(&log).Error
	if err != nil {
		global.WM_LOG.Error("新增日志客户端用户失败", zap.Any("err", err))
		return err, false
	}
	return nil, true
}

func ListLog(r validation.ListLog) (err error, data interface{}) {
	var db *gorm.DB
	var records = make([]map[string]interface{}, 0)
	limit := r.PageSize
	offset := limit * (r.PageNum - 1)
	var totalNum int64

	switch r.LogType {
	case "jsErrorLog":
		db = global.WM_DB.Model(&model.LmsJsErrorLog{})
		break
	case "httpErrorLog":
		db = global.WM_DB.Model(&model.LmsHttpErrorLog{})
		break
	case "resourceLoadErrorLog":
		db = global.WM_DB.Model(&model.LmsResourceLoadErrorLog{})
		break
	case "customErrorLog":
		db = global.WM_DB.Model(&model.LmsCustomErrorLog{})
		break
	default:
		err = errors.New("logType参数不合规")
		return err, nil
	}

	// 项目标识
	if r.ProjectIdentifier != "" {
		db = db.Where("`project_identifier` = ?", r.ProjectIdentifier)
	}
	// 开始时间、结束时间
	if r.StartTime != "" && r.EndTime != "" {
		db = db.Where("`create_time` BETWEEN ? AND ?", r.StartTime, r.EndTime)
	} else if r.StartTime != "" {
		db = db.Where("`create_time` >= ?", r.StartTime)
	} else if r.EndTime != "" {
		db = db.Where("`create_time` <= ?", r.EndTime)
	}
	// 处理条件参数
	for _, con := range r.ConditionList {
		db = setParamSqlBuilder(db, con.Key, con.Value, con.Op)
	}

	err = db.Count(&totalNum).Error
	err = db.Limit(limit).Offset(offset).Find(&records).Order("create_time desc").Error
	data = map[string]interface{}{
		"totalNum":  totalNum,
		"totalPage": math.Ceil(float64(totalNum) / float64(r.PageSize)),
		"pageNum":   r.PageNum,
		"pageSize":  r.PageSize,
		"records":   records,
	}
	return err, data
}

func setParamSqlBuilder(db *gorm.DB, key string, value string, op string) *gorm.DB {
	if key == "" || value == "" || op == "" {
		return db
	}

	// 可以直接拼接的操作符
	canDirectAppendOpList := []string{"=", ">", ">=", "<", "<=", "!="}
	if utils.IsStringArrContainsStr(canDirectAppendOpList, op) {
		query := key + " " + op + " ?"
		db = db.Where(query, value)
	} else if op == "wildcard" {
		// 不可以直接拼接的操作符
		// 通配符查询，即模糊查询
		query := key + " like ?"
		db = db.Where(query, "%"+value+"%")
	}

	return db
}
