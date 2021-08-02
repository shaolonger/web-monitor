package utils

import (
	"math"
	"time"
)

const (
	DefaultLayout = "2006-01-02 15:04:05"
)

// ParseTimeStrInLocationByDefault 将时间字符串转为time.Time
func ParseTimeStrInLocationByDefault(timeStr string) (time.Time, error) {
	result, err := time.ParseInLocation(DefaultLayout, timeStr, time.Local)
	return result, err
}

// ParseTimeStrInLocationByLayout 将时间字符串转为time.Time，按照指定的日期格式
func ParseTimeStrInLocationByLayout(timeStr string, layout string) (time.Time, error) {
	result, err := time.ParseInLocation(layout, timeStr, time.Local)
	return result, err
}

// GetHoursBetweenDateRange 获取两个日期之间相差的小时数
func GetHoursBetweenDateRange(startTime time.Time, endTime time.Time) int {
	return int(math.Abs(endTime.Sub(startTime).Seconds() / 3600))
}

// GetDaysBetweenDateRange 获取两个日期之间相差的天数
func GetDaysBetweenDateRange(startTime time.Time, endTime time.Time) int {
	return int(math.Abs(endTime.Sub(startTime).Hours() / 24))
}

// GetCountBetweenDateRange 根据传入的单位(秒)，获取两个日期之间相差的单位数
func GetCountBetweenDateRange(startTime time.Time, endTime time.Time, timeInterval int) int {
	return int(math.Abs(endTime.Sub(startTime).Seconds() / float64(timeInterval)))
}

// GetDateBeforeOrAfterByDays 获取在参考日期相隔days天的日期
func GetDateBeforeOrAfterByDays(date time.Time, days int) time.Time {
	return date.Add(time.Hour * time.Duration(24*days))
}

// GetDateBeforeOrAfterByMinutes 获取在参考日期相隔minutes分钟的日期
func GetDateBeforeOrAfterByMinutes(date time.Time, minutes int) time.Time {
	return date.Add(time.Minute * time.Duration(minutes))
}

// PassTimeToStrByDefaultLayout 获取当前时刻的字符串表示
func PassTimeToStrByDefaultLayout(t time.Time) string {
	return t.Format(DefaultLayout)
}

// GetCurrentTimeByDefaultLayout 获取当前时刻的字符串表示
func GetCurrentTimeByDefaultLayout() string {
	return PassTimeToStrByDefaultLayout(time.Now())
}

// PassTimeToStrByLayout 获取当前时刻的字符串表示
func PassTimeToStrByLayout(t time.Time, layout string) string {
	return t.Format(layout)
}

// CheckIsTimeAfterByTimeStrAndLayout 判断两个时间字符串的先后
//func CheckIsTimeAfterByTimeStrAndLayout(str1 string, str2 string, layout string) (error, bool) {
//	t1, err := time.ParseInLocation(layout, str1, time.Local)
//	if err != nil {
//		return err, false
//	}
//	t2, err := time.ParseInLocation(layout, str2, time.Local)
//	if err != nil {
//		return err, false
//	}
//	return nil, t1.After(t2)
//}
