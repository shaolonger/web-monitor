package service

import (
	"time"
	"web.monitor.com/model/response"
	"web.monitor.com/model/validation"
	"web.monitor.com/utils"
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

func GetLogCountByHours(r validation.GetLogCountByHours) (err error, data interface{}) {
	var resultMap = make(map[string]interface{})
	var nowMap = make(map[string]int64)
	var agoMap = make(map[string]int64)

	startTime, err := utils.ParseTimeStrInLocationByDefault(r.StartTime)
	if err != nil {
		return err, nil
	}
	endTime, err := utils.ParseTimeStrInLocationByDefault(r.EndTime)
	if err != nil {
		return err, nil
	}
	hoursGap := utils.GetHoursBetweenDateRange(startTime, endTime)

	for i := 0; i < hoursGap; i++ {
		nowtStartDate := startTime.Add(time.Hour * time.Duration(i+1))
		agoStartDate := startTime.Add(time.Hour * time.Duration((i+1)-7*24))
		nowKey := nowtStartDate.Format("2006-01-02 15")
		agoKey := agoStartDate.Format("2006-01-02 15")
		nowMap[nowKey] = 0
		agoMap[agoKey] = 0
	}

	var nowSearchList []response.GetLogCountByHours
	var agoSearchList []response.GetLogCountByHours
	agoStartTime := utils.GetDateBeforeOrAfterByDays(startTime, -7)
	agoEndTime := utils.GetDateBeforeOrAfterByDays(endTime, -7)

	switch r.LogType {
	case "jsErrorLog":
		_, nowSearchList = getJsLogCountByHours(r.ProjectIdentifier, startTime, endTime)
		_, agoSearchList = getJsLogCountByHours(r.ProjectIdentifier, agoStartTime, agoEndTime)
		break
	case "httpErrorLog":
		_, nowSearchList = getHttpLogCountByHours(r.ProjectIdentifier, startTime, endTime)
		_, agoSearchList = getHttpLogCountByHours(r.ProjectIdentifier, agoStartTime, agoEndTime)
		break
	case "resourceLoadErrorLog":
		_, nowSearchList = getResLogCountByHours(r.ProjectIdentifier, startTime, endTime)
		_, agoSearchList = getResLogCountByHours(r.ProjectIdentifier, agoStartTime, agoEndTime)
		break
	case "customErrorLog":
		_, nowSearchList = getCusLogCountByHours(r.ProjectIdentifier, startTime, endTime)
		_, agoSearchList = getCusLogCountByHours(r.ProjectIdentifier, agoStartTime, agoEndTime)
		break
	}

	if nowSearchList != nil && agoSearchList != nil {
		for _, valueMap := range nowSearchList {
			nowMap[valueMap.Hour] = valueMap.Count
		}
		for _, valueMap := range agoSearchList {
			agoMap[valueMap.Hour] = valueMap.Count
		}
	}

	resultMap["now"] = nowMap
	resultMap["ago"] = agoMap
	return nil, resultMap
}

func GetLogCountByDays(r validation.GetLogCountByDays) (err error, data interface{}) {
	var resultMap = make(map[string]int64)

	layout := "2006-01-02"
	startTime, err := utils.ParseTimeStrInLocationByLayout(r.StartTime, layout)
	if err != nil {
		return err, nil
	}
	endTime, err := utils.ParseTimeStrInLocationByLayout(r.EndTime, layout)
	if err != nil {
		return err, nil
	}

	daysGap := utils.GetDaysBetweenDateRange(startTime, endTime)
	for i := 0; i < daysGap; i++ {
		nowtStartDate := startTime.Add(time.Hour * time.Duration(i*24))
		nowKey := nowtStartDate.Format("2006-01-02")
		resultMap[nowKey] = 0
	}

	var searchList []response.GetLogCountByDays
	switch r.LogType {
	case "jsErrorLog":
		_, searchList = getJsLogCountByDays(r.ProjectIdentifier, startTime, endTime)
		break
	case "httpErrorLog":
		_, searchList = getHttpLogCountByDays(r.ProjectIdentifier, startTime, endTime)
		break
	case "resourceLoadErrorLog":
		_, searchList = getResLogCountByDays(r.ProjectIdentifier, startTime, endTime)
		break
	case "customErrorLog":
		_, searchList = getCusLogCountByDays(r.ProjectIdentifier, startTime, endTime)
		break
	}

	if searchList != nil {
		for _, valueMap := range searchList {
			resultMap[valueMap.Day] = valueMap.Count
		}
	}

	return nil, resultMap
}
