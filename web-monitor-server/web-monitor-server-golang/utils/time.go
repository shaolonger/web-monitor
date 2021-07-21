package utils

import (
	"math"
	"time"
)

const (
	Layout = "2006-01-02 15:04:05"
)

// ParseTimeStrInLocationByDefault 将时间字符串转为time.Time
func ParseTimeStrInLocationByDefault(timeStr string) (time.Time, error) {
	result, err := time.ParseInLocation(Layout, timeStr, time.Local)
	return result, err
}

// GetHoursBetweenDateRange 获取两个日期之间相差的小时数
func GetHoursBetweenDateRange(startTime time.Time, endTime time.Time) int {
	return int(math.Abs(endTime.Sub(startTime).Seconds() / 3600))
}

// GetDateBeforeOrAfterByDays 获取在参考日期相隔days天的日期
func GetDateBeforeOrAfterByDays(date time.Time, days int) time.Time {
	return date.Add(time.Hour * time.Duration(24*days))
}

// CheckIsTimeAfterByTimeStrAndLayout 判断两个时间字符串的先后
func CheckIsTimeAfterByTimeStrAndLayout(str1 string, str2 string, layout string) (error, bool) {
	t1, err := time.ParseInLocation(layout, str1, time.Local)
	if err != nil {
		return err, false
	}
	t2, err := time.ParseInLocation(layout, str2, time.Local)
	if err != nil {
		return err, false
	}
	return nil, t1.After(t2)
}
