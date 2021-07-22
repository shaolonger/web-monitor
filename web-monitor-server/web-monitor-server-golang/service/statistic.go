package service

import (
	"errors"
	"strings"
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
		_, nowSearchList = GetJsLogCountByHours(r.ProjectIdentifier, startTime, endTime)
		_, agoSearchList = GetJsLogCountByHours(r.ProjectIdentifier, agoStartTime, agoEndTime)
		break
	case "httpErrorLog":
		_, nowSearchList = GetHttpLogCountByHours(r.ProjectIdentifier, startTime, endTime)
		_, agoSearchList = GetHttpLogCountByHours(r.ProjectIdentifier, agoStartTime, agoEndTime)
		break
	case "resourceLoadErrorLog":
		_, nowSearchList = GetResLogCountByHours(r.ProjectIdentifier, startTime, endTime)
		_, agoSearchList = GetResLogCountByHours(r.ProjectIdentifier, agoStartTime, agoEndTime)
		break
	case "customErrorLog":
		_, nowSearchList = GetCusLogCountByHours(r.ProjectIdentifier, startTime, endTime)
		_, agoSearchList = GetCusLogCountByHours(r.ProjectIdentifier, agoStartTime, agoEndTime)
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
		_, searchList = GetJsLogCountByDays(r.ProjectIdentifier, startTime, endTime)
		break
	case "httpErrorLog":
		_, searchList = GetHttpLogCountByDays(r.ProjectIdentifier, startTime, endTime)
		break
	case "resourceLoadErrorLog":
		_, searchList = GetResLogCountByDays(r.ProjectIdentifier, startTime, endTime)
		break
	case "customErrorLog":
		_, searchList = GetCusLogCountByDays(r.ProjectIdentifier, startTime, endTime)
		break
	}

	if searchList != nil {
		for _, valueMap := range searchList {
			resultMap[valueMap.Day] = valueMap.Count
		}
	}

	return nil, resultMap
}

func GetLogCountBetweenDiffDate(r validation.GetLogCountBetweenDiffDate) (err error, data interface{}) {
	// 校验参数
	logTypeLists := strings.Split(r.LogTypeList, ",")
	if len(logTypeLists) == 0 {
		err = errors.New("logTypeList格式错误，要以,隔开")
		return err, nil
	}
	IndicatorList := strings.Split(r.IndicatorList, ",")
	if len(IndicatorList) == 0 {
		err = errors.New("indicatorList格式错误，要以,隔开")
		return err, nil
	}

	resultMap := make(map[string]interface{})
	for _, logType := range logTypeLists {
		originResultList := make([]response.GetLogListByCreateTimeAndProjectIdentifier, 0)
		startTime, err := utils.ParseTimeStrInLocationByDefault(r.StartTime)
		if err != nil {
			return err, nil
		}
		endTime, err := utils.ParseTimeStrInLocationByDefault(r.EndTime)
		if err != nil {
			return err, nil
		}
		switch logType {
		case "jsErrorLog":
			_, originResultList = GetJsLogListByCreateTimeAndProjectIdentifier(r.ProjectIdentifier, startTime, endTime)
			break
		case "httpErrorLog":
			_, originResultList = GetHttpLogListByCreateTimeAndProjectIdentifier(r.ProjectIdentifier, startTime, endTime)
			break
		case "resourceLoadErrorLog":
			_, originResultList = GetResLogListByCreateTimeAndProjectIdentifier(r.ProjectIdentifier, startTime, endTime)
			break
		case "customErrorLog":
			_, originResultList = GetCusLogListByCreateTimeAndProjectIdentifier(r.ProjectIdentifier, startTime, endTime)
			break
		}
		countGap := utils.GetCountBetweenDateRange(startTime, endTime, r.TimeInterval)
		var startDate time.Time
		var endDate time.Time
		tempMap := make(map[string][]response.GetLogListByCreateTimeAndProjectIdentifier, 0)
		for i := 0; i < countGap; i++ {
			if startDate.IsZero() {
				startDate = startTime
			} else {
				startDate = startDate.Add(time.Second * time.Duration(r.TimeInterval))
			}
			if endDate.IsZero() {
				endDate = startTime.Add(time.Second * time.Duration((i+1)*r.TimeInterval))
			} else {
				endDate = endDate.Add(time.Second * time.Duration(r.TimeInterval))
			}
			startDateStr := startDate.Format(utils.DefaultLayout)
			tempList := tempMap[startDateStr]
			if tempList == nil {
				tempList = make([]response.GetLogListByCreateTimeAndProjectIdentifier, 0)
			}
			for _, valueItem := range originResultList {
				if valueItem.CreateTime.After(startDate) && valueItem.CreateTime.Before(endDate) {
					tempList = append(tempList, valueItem)
				}
			}
			tempMap[startDateStr] = tempList
		}

		logTypeResultList := make([]map[string]interface{}, 0)
		for key, valueItem := range tempMap {
			dataMap := make(map[string]interface{})
			dataMap[key] = key

			// 计算count，日志数量
			if utils.IsStringArrContainsStr(IndicatorList, "count") {
				dataMap["count"] = len(valueItem)
			}

			// 计算uv，影响的用户数
			if utils.IsStringArrContainsStr(IndicatorList, "uv") {
				tempSet := make(map[string]bool)
				for _, value := range valueItem {
					tempSet[value.Cuuid] = true
				}
				dataMap["uv"] = len(tempSet)
			}

			logTypeResultList = append(logTypeResultList, dataMap)
		}
		resultMap[logType] = logTypeResultList
	}
	return nil, resultMap
}
