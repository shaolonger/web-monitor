package service

import (
	"web.monitor.com/model/response"
	"web.monitor.com/model/validation"
)

func GetStatisticOverallByTimeRange(r validation.GetStatisticOverallByTimeRange) (err error, data interface{}) {
	// js异常日志
	err, jsErrorLogCount := GetJsCountByIdBetweenStartTimeAndEndTime(r.ProjectIdentifier, r.StartTime, r.EndTime)
	if err != nil {
		return err, nil
	}
	// res异常日志
	err, resourceLoadErrorLogCount := GetResCountByIdBetweenStartTimeAndEndTime(r.ProjectIdentifier, r.StartTime, r.EndTime)
	if err != nil {
		return err, nil
	}
	// http异常日志
	err, httpErrorLogCount := GetHttpCountByIdBetweenStartTimeAndEndTime(r.ProjectIdentifier, r.StartTime, r.EndTime)
	if err != nil {
		return err, nil
	}
	// custom异常日志
	err, customErrorLogCount := GetCusCountByIdBetweenStartTimeAndEndTime(r.ProjectIdentifier, r.StartTime, r.EndTime)
	if err != nil {
		return err, nil
	}
	result := response.GetStatisticOverallByTimeRange{
		CustomErrorLogCount:       customErrorLogCount,
		HttpErrorLogCount:         httpErrorLogCount,
		ResourceLoadErrorLogCount: resourceLoadErrorLogCount,
		JsErrorLogCount:           jsErrorLogCount,
	}
	return nil, result
}
