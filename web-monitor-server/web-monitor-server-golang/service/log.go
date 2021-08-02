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

// CountLogDistinctCUuid 计算总cUuid数
func CountLogDistinctCUuid() int64 {
	var count int64
	db := global.WM_DB.Model(&model.LmsClientUser{})
	db.Distinct("c_uuid").Count(&count)
	return count
}

// CheckIsExceedAlarmThreshold
// 根据预警规则，判断是否已超过预警阈值
func CheckIsExceedAlarmThreshold(tableName string, ruleItem *validation.SchedulerRuleItem) *validation.AlarmScheduleResult {

	timeSpan := ruleItem.TimeSpan
	interval := ruleItem.Interval
	ind := ruleItem.Ind
	op := ruleItem.Op
	agg := ruleItem.Agg
	val := ruleItem.Val

	// 当聚合维度为平均时，gap用于计算平均值
	gap := timeSpan / interval

	endTime := time.Now()
	startTime := utils.GetDateBeforeOrAfterByMinutes(endTime, -timeSpan)

	// 返回结果
	resultMap := validation.AlarmScheduleResult{}
	isExceedAlarmThreshold := false
	var targetInd string
	var actualValue float64

	// ------------------------ 影响用户数 ------------------------
	if ind == "uvCount" {
		targetInd = "影响用户数"
		var uvCount int64
		switch tableName {
		case "lms_js_error_log":
			uvCount = CountJsDistinctCUuidByCreateTimeBetween(startTime, endTime)
			break
		case "lms_http_error_log":
			uvCount = CountHttpDistinctCUuidByCreateTimeBetween(startTime, endTime)
			break
		case "lms_resource_load_error_log":
			uvCount = CountResDistinctCUuidByCreateTimeBetween(startTime, endTime)
			break
		case "lms_custom_error_log":
			uvCount = CountCusDistinctCUuidByCreateTimeBetween(startTime, endTime)
			break
		default:
			break
		}
		if op == ">" {
			if agg == "count" {
				isExceedAlarmThreshold = float64(uvCount) > val
			}
			if agg == "avg" {
				isExceedAlarmThreshold = float64(uvCount/int64(gap)) > val
			}
			actualValue = float64(uvCount)
		}
		if op == "<" {
			if agg == "count" {
				isExceedAlarmThreshold = float64(uvCount) < val
			}
			if agg == "avg" {
				isExceedAlarmThreshold = float64(uvCount/int64(gap)) < val
			}
			actualValue = float64(uvCount)
		}
		// 环比昨天上涨、下跌
		if op == "d_up" || op == "d_down" {
			preStartTime := utils.GetDateBeforeOrAfterByDays(startTime, -1)
			preEndTime := utils.GetDateBeforeOrAfterByDays(endTime, -1)
			var preUvCount int64
			switch tableName {
			case "lms_js_error_log":
				preUvCount = CountJsDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime)
				break
			case "lms_http_error_log":
				preUvCount = CountHttpDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime)
				break
			case "lms_resource_load_error_log":
				preUvCount = CountResDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime)
				break
			case "lms_custom_error_log":
				preUvCount = CountCusDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime)
				break
			default:
				break
			}
			if op == "d_up" {
				if agg == "count" {
					isExceedAlarmThreshold = uvCount > preUvCount
					actualValue = float64(uvCount)
				}
				if agg == "avg" {
					isExceedAlarmThreshold = int(uvCount)/gap > int(preUvCount)
					actualValue = float64(int(uvCount) / gap)
				}
			}
			if op == "d_down" {
				if agg == "count" {
					isExceedAlarmThreshold = uvCount < preUvCount
					actualValue = float64(int(uvCount))
				}
				if agg == "avg" {
					isExceedAlarmThreshold = int(uvCount)/gap < int(preUvCount)
					actualValue = float64(int(uvCount) / gap)
				}
			}
		}
		// 环比上周上涨、下跌
		if op == "w_up" || op == "w_down" {
			preStartTime := utils.GetDateBeforeOrAfterByDays(startTime, -7)
			preEndTime := utils.GetDateBeforeOrAfterByDays(endTime, -7)
			var preUvCount int64
			switch tableName {
			case "lms_js_error_log":
				preUvCount = CountJsDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime)
				break
			case "lms_http_error_log":
				preUvCount = CountHttpDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime)
				break
			case "lms_resource_load_error_log":
				preUvCount = CountResDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime)
				break
			case "lms_custom_error_log":
				preUvCount = CountCusDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime)
				break
			default:
				break
			}
			if op == "w_up" {
				if agg == "count" {
					isExceedAlarmThreshold = uvCount > preUvCount
					actualValue = float64(int(uvCount))
				}
				if agg == "avg" {
					isExceedAlarmThreshold = int(uvCount)/gap > int(preUvCount)
					actualValue = float64(int(uvCount) / gap)
				}
			}
			if op == "w_down" {
				if agg == "count" {
					isExceedAlarmThreshold = uvCount < preUvCount
					actualValue = float64(int(uvCount))
				}
				if agg == "avg" {
					isExceedAlarmThreshold = int(uvCount)/gap < int(preUvCount)
					actualValue = float64(int(uvCount) / gap)
				}
			}
		}
	}

	// ------------------------ 影响用户率 ------------------------
	if ind == "uvRate" {
		targetInd = "影响用户率"
		var uvCount int64
		uvTotal := CountLogDistinctCUuid()
		switch tableName {
		case "lms_js_error_log":
			uvCount = CountJsDistinctCUuidByCreateTimeBetween(startTime, endTime)
			break
		case "lms_http_error_log":
			uvCount = CountHttpDistinctCUuidByCreateTimeBetween(startTime, endTime)
			break
		case "lms_resource_load_error_log":
			uvCount = CountResDistinctCUuidByCreateTimeBetween(startTime, endTime)
			break
		case "lms_custom_error_log":
			uvCount = CountCusDistinctCUuidByCreateTimeBetween(startTime, endTime)
			break
		default:
			break
		}
		uvRate := float64(uvCount / uvTotal * 100)
		if op == ">" {
			if agg == "count" {
				isExceedAlarmThreshold = uvRate > val
				actualValue = uvRate
			}
			if agg == "avg" {
				isExceedAlarmThreshold = uvRate/float64(gap) > val
				actualValue = uvRate / float64(gap)
			}
		}
		if op == "<" {
			if agg == "count" {
				isExceedAlarmThreshold = float64(uvCount) < val
				actualValue = uvRate
			}
			if agg == "avg" {
				isExceedAlarmThreshold = float64(uvCount/int64(gap)) < val
				actualValue = uvRate / float64(gap)
			}
		}
		// 环比昨天上涨、下跌
		if op == "d_up" || op == "d_down" {
			preStartTime := utils.GetDateBeforeOrAfterByDays(startTime, -1)
			preEndTime := utils.GetDateBeforeOrAfterByDays(endTime, -1)
			var preUvCount int64
			switch tableName {
			case "lms_js_error_log":
				preUvCount = CountJsDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime)
				break
			case "lms_http_error_log":
				preUvCount = CountHttpDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime)
				break
			case "lms_resource_load_error_log":
				preUvCount = CountResDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime)
				break
			case "lms_custom_error_log":
				preUvCount = CountCusDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime)
				break
			default:
				break
			}
			preUvRate := float64(preUvCount / uvTotal)
			if op == "d_up" {
				if agg == "count" {
					isExceedAlarmThreshold = preUvRate > uvRate
					actualValue = preUvRate
				}
				if agg == "avg" {
					isExceedAlarmThreshold = preUvRate/float64(gap) > uvRate
					actualValue = preUvRate / float64(gap)
				}
			}
			if op == "d_down" {
				if agg == "count" {
					isExceedAlarmThreshold = preUvRate < uvRate
					actualValue = preUvRate
				}
				if agg == "avg" {
					isExceedAlarmThreshold = preUvRate/float64(gap) < uvRate
					actualValue = preUvRate / float64(gap)
				}
			}
		}
		// 环比上周上涨、下跌
		if op == "w_up" || op == "w_down" {
			preStartTime := utils.GetDateBeforeOrAfterByDays(startTime, -7)
			preEndTime := utils.GetDateBeforeOrAfterByDays(endTime, -7)
			var preUvCount int64
			switch tableName {
			case "lms_js_error_log":
				preUvCount = CountJsDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime)
				break
			case "lms_http_error_log":
				preUvCount = CountHttpDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime)
				break
			case "lms_resource_load_error_log":
				preUvCount = CountResDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime)
				break
			case "lms_custom_error_log":
				preUvCount = CountCusDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime)
				break
			default:
				break
			}
			preUvRate := float64(preUvCount / uvTotal)
			if op == "w_up" {
				if agg == "count" {
					isExceedAlarmThreshold = preUvRate > uvRate
					actualValue = preUvRate
				}
				if agg == "avg" {
					isExceedAlarmThreshold = preUvRate/float64(gap) > uvRate
					actualValue = preUvRate / float64(gap)
				}
			}
			if op == "w_down" {
				if agg == "count" {
					isExceedAlarmThreshold = preUvRate < uvRate
					actualValue = preUvRate
				}
				if agg == "avg" {
					isExceedAlarmThreshold = preUvRate/float64(gap) < uvRate
					actualValue = preUvRate / float64(gap)
				}
			}
		}
	}

	// ------------------------ 人均异常次数 ------------------------
	if ind == "perPV" {
		targetInd = "人均异常次数"
		var pvCount int64
		uvTotal := CountLogDistinctCUuid()
		switch tableName {
		case "lms_js_error_log":
			pvCount = CountJsByCreateTimeBetween(startTime, endTime)
			break
		case "lms_http_error_log":
			pvCount = CountHttpByCreateTimeBetween(startTime, endTime)
			break
		case "lms_resource_load_error_log":
			pvCount = CountResByCreateTimeBetween(startTime, endTime)
			break
		case "lms_custom_error_log":
			pvCount = CountCusByCreateTimeBetween(startTime, endTime)
			break
		default:
			break
		}
		pvRate := float64(pvCount / uvTotal)
		if op == ">" {
			if agg == "count" {
				isExceedAlarmThreshold = pvRate > val
				actualValue = pvRate
			}
			if agg == "avg" {
				isExceedAlarmThreshold = pvRate/float64(gap) > val
				actualValue = pvRate / float64(gap)
			}
		}
		if op == "<" {
			if agg == "count" {
				isExceedAlarmThreshold = pvRate < val
				actualValue = pvRate
			}
			if agg == "avg" {
				isExceedAlarmThreshold = pvRate/float64(gap) < val
				actualValue = pvRate / float64(gap)
			}
		}
		// 环比昨天上涨、下跌
		if op == "d_up" || op == "d_down" {
			preStartTime := utils.GetDateBeforeOrAfterByDays(startTime, -1)
			preEndTime := utils.GetDateBeforeOrAfterByDays(endTime, -1)
			var prePvCount int64
			switch tableName {
			case "lms_js_error_log":
				prePvCount = CountJsByCreateTimeBetween(preStartTime, preEndTime)
				break
			case "lms_http_error_log":
				prePvCount = CountHttpByCreateTimeBetween(preStartTime, preEndTime)
				break
			case "lms_resource_load_error_log":
				prePvCount = CountResByCreateTimeBetween(preStartTime, preEndTime)
				break
			case "lms_custom_error_log":
				prePvCount = CountCusByCreateTimeBetween(preStartTime, preEndTime)
				break
			default:
				break
			}
			preUvRate := float64(prePvCount / uvTotal)
			if op == "d_up" {
				if agg == "count" {
					isExceedAlarmThreshold = preUvRate > pvRate
					actualValue = preUvRate
				}
				if agg == "avg" {
					isExceedAlarmThreshold = preUvRate/float64(gap) > pvRate
					actualValue = preUvRate / float64(gap)
				}
			}
			if op == "d_down" {
				if agg == "count" {
					isExceedAlarmThreshold = preUvRate < pvRate
					actualValue = preUvRate
				}
				if agg == "avg" {
					isExceedAlarmThreshold = preUvRate/float64(gap) < pvRate
					actualValue = preUvRate / float64(gap)
				}
			}
		}
		// 环比上周上涨、下跌
		if op == "w_up" || op == "w_down" {
			preStartTime := utils.GetDateBeforeOrAfterByDays(startTime, -7)
			preEndTime := utils.GetDateBeforeOrAfterByDays(endTime, -7)
			var preUvCount int64
			switch tableName {
			case "lms_js_error_log":
				preUvCount = CountJsByCreateTimeBetween(preStartTime, preEndTime)
				break
			case "lms_http_error_log":
				preUvCount = CountHttpByCreateTimeBetween(preStartTime, preEndTime)
				break
			case "lms_resource_load_error_log":
				preUvCount = CountResByCreateTimeBetween(preStartTime, preEndTime)
				break
			case "lms_custom_error_log":
				preUvCount = CountCusByCreateTimeBetween(preStartTime, preEndTime)
				break
			default:
				break
			}
			preUvRate := float64(preUvCount / uvTotal)
			if op == "w_up" {
				if agg == "count" {
					isExceedAlarmThreshold = preUvRate > pvRate
					actualValue = preUvRate
				}
				if agg == "avg" {
					isExceedAlarmThreshold = preUvRate/float64(gap) > pvRate
					actualValue = preUvRate / float64(gap)
				}
			}
			if op == "w_down" {
				if agg == "count" {
					isExceedAlarmThreshold = preUvRate < pvRate
					actualValue = preUvRate
				}
				if agg == "avg" {
					isExceedAlarmThreshold = preUvRate/float64(gap) < pvRate
					actualValue = preUvRate / float64(gap)
				}
			}
		}
	}

	// ------------------------ 新增异常数 ------------------------
	if ind == "newPV" {
		targetInd = "新增异常数"
		var pvCount int64
		switch tableName {
		case "lms_js_error_log":
			pvCount = CountJsDistinctCUuidByCreateTimeBetween(startTime, endTime)
			break
		case "lms_http_error_log":
			pvCount = CountHttpDistinctCUuidByCreateTimeBetween(startTime, endTime)
			break
		case "lms_resource_load_error_log":
			pvCount = CountResDistinctCUuidByCreateTimeBetween(startTime, endTime)
			break
		case "lms_custom_error_log":
			pvCount = CountCusDistinctCUuidByCreateTimeBetween(startTime, endTime)
			break
		default:
			break
		}
		if op == ">" {
			if agg == "count" {
				isExceedAlarmThreshold = float64(pvCount) > val
				actualValue = float64(pvCount)
			}
			if agg == "avg" {
				isExceedAlarmThreshold = float64(pvCount/int64(gap)) > val
				actualValue = float64(pvCount / int64(gap))
			}
		}
		if op == "<" {
			if agg == "count" {
				isExceedAlarmThreshold = float64(pvCount) < val
				actualValue = float64(pvCount)
			}
			if agg == "avg" {
				isExceedAlarmThreshold = float64(pvCount/int64(gap)) < val
				actualValue = float64(pvCount / int64(gap))
			}
		}
		// 环比昨天上涨、下跌
		if op == "d_up" || op == "d_down" {
			preStartTime := utils.GetDateBeforeOrAfterByDays(startTime, -1)
			preEndTime := utils.GetDateBeforeOrAfterByDays(endTime, -1)
			var prePvCount int64
			switch tableName {
			case "lms_js_error_log":
				prePvCount = CountJsDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime)
				break
			case "lms_http_error_log":
				prePvCount = CountHttpDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime)
				break
			case "lms_resource_load_error_log":
				prePvCount = CountResDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime)
				break
			case "lms_custom_error_log":
				prePvCount = CountCusDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime)
				break
			default:
				break
			}
			if op == "d_up" {
				if agg == "count" {
					isExceedAlarmThreshold = pvCount > prePvCount
					actualValue = float64(pvCount)
				}
				if agg == "avg" {
					isExceedAlarmThreshold = pvCount/int64(gap) > prePvCount
					actualValue = float64(pvCount / int64(gap))
				}
			}
			if op == "d_down" {
				if agg == "count" {
					isExceedAlarmThreshold = pvCount < prePvCount
					actualValue = float64(pvCount)
				}
				if agg == "avg" {
					isExceedAlarmThreshold = pvCount/int64(gap) < prePvCount
					actualValue = float64(pvCount / int64(gap))
				}
			}
		}
		// 环比上周上涨、下跌
		if op == "w_up" || op == "w_down" {
			preStartTime := utils.GetDateBeforeOrAfterByDays(startTime, -7)
			preEndTime := utils.GetDateBeforeOrAfterByDays(endTime, -7)
			var prePvCount int64
			switch tableName {
			case "lms_js_error_log":
				prePvCount = CountJsDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime)
				break
			case "lms_http_error_log":
				prePvCount = CountHttpDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime)
				break
			case "lms_resource_load_error_log":
				prePvCount = CountResDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime)
				break
			case "lms_custom_error_log":
				prePvCount = CountCusDistinctCUuidByCreateTimeBetween(preStartTime, preEndTime)
				break
			default:
				break
			}
			if op == "w_up" {
				if agg == "count" {
					isExceedAlarmThreshold = pvCount > prePvCount
					actualValue = float64(pvCount)
				}
				if agg == "avg" {
					isExceedAlarmThreshold = pvCount/int64(gap) > prePvCount
					actualValue = float64(pvCount / int64(gap))
				}
			}
			if op == "w_down" {
				if agg == "count" {
					isExceedAlarmThreshold = pvCount < prePvCount
					actualValue = float64(pvCount)
				}
				if agg == "avg" {
					isExceedAlarmThreshold = pvCount/int64(gap) < prePvCount
					actualValue = float64(pvCount / int64(gap))
				}
			}
		}
	}

	// 设置预警检查结果
	resultMap.IsExceedAlarmThreshold = isExceedAlarmThreshold
	resultMap.TargetInd = targetInd
	resultMap.ActualValue = actualValue
	resultMap.ThresholdValue = val
	resultMap.StartTime = startTime
	resultMap.EndTime = endTime

	return &resultMap
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
