package utils

import "strings"

// IsStringArrContainsStr 判断[]string中是否包含某个字符串
func IsStringArrContainsStr(stringArr []string, str string) bool {
	flag := false
	for _, strVal := range stringArr {
		flag = strings.Contains(strVal, str)
		if flag {
			break
		}
	}
	return flag
}
