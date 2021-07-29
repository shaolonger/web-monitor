package utils

import (
	"encoding/json"
	"errors"
	"reflect"
)

// HasField 通过反射，检查struct是否拥有某个field
func HasField(s interface{}, field string) bool {
	t := reflect.TypeOf(s)
	if _, ok := t.FieldByName(field); ok {
		return true
	} else {
		return false
	}
}

// GetStringValueByField 通过反射，获取struct某个field的string值
func GetStringValueByField(s interface{}, field string) (error, string) {
	if !HasField(s, field) {
		return errors.New("field不存在"), ""
	} else {
		r := reflect.ValueOf(s)
		f := reflect.Indirect(r).FieldByName(field)
		return nil, string(f.String())
	}
}

// StructToJson Struct转Json
func StructToJson(s interface{}) (str string, err error) {
	jsonStr, err := json.Marshal(s)
	return string(jsonStr), err
}
