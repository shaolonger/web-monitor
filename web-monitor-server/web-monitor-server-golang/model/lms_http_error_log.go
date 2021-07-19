package model

import "time"

type LmsHttpErrorLog struct {
	Id                uint64    `json:"id" gorm:"primaryKey;autoIncrement;comment:ID"`
	LogType           string    `json:"logType" gorm:"not null;type:varchar(50);comment:日志类型"`
	ProjectIdentifier string    `json:"projectIdentifier" gorm:"not null;type:varchar(200);comment:关联的项目标识"`
	CreateTime        time.Time `json:"createTime" gorm:"not null;comment:创建时间，格式为yyyy-MM-dd HH:mm:ss"`
	Cuuid             string    `json:"cUuid" gorm:"column:c_uuid;not null;type:char(36);comment:客户端唯一识别码，全称client uuid"`
	Buid              uint64    `json:"bUid" gorm:"column:b_uid;comment:业务用户ID"`
	Buname            string    `json:"bUname" gorm:"column:b_uname;type:varchar(64);comment:业务用户名"`
	PageUrl           string    `json:"pageUrl" gorm:"type:text;comment:页面URL"`
	PageKey           string    `json:"pageKey" gorm:"type:varchar(50);comment:页面关键字"`
	DeviceName        string    `json:"deviceName" gorm:"type:varchar(100);comment:设备名"`
	Os                string    `json:"os" gorm:"type:varchar(20);comment:操作系统"`
	OsVersion         string    `json:"osVersion" gorm:"type:varchar(20);comment:操作系统版本"`
	BrowserName       string    `json:"browserName" gorm:"type:varchar(20);comment:浏览器名"`
	BrowserVersion    string    `json:"browserVersion" gorm:"type:varchar(20);comment:浏览器版本"`
	IpAddress         string    `json:"ipAddress" gorm:"type:varchar(50);comment:IP地址"`
	Address           string    `json:"address" gorm:"type:varchar(100);comment:地址"`
	NetType           string    `json:"netType" gorm:"type:varchar(10);comment:网络类型"`
	HttpType          string    `json:"httpType" gorm:"type:varchar(20);comment:http请求类型，如request、response"`
	HttpUrlComplete   string    `json:"httpUrlComplete" gorm:"not null;type:text;comment:完整的http请求地址"`
	HttpUrlShort      string    `json:"httpUrlShort" gorm:"type:text;comment:缩写的http请求地址"`
	Status            string    `json:"status" gorm:"type:varchar(20);comment:请求状态"`
	StatusText        string    `json:"statusText" gorm:"type:varchar(50);comment:请求状态文字描述"`
	ResTime           string    `json:"resTime" gorm:"type:varchar(13);comment:响应时间"`
}

func (l LmsHttpErrorLog) TableName() string {
	return "lms_http_error_log"
}
