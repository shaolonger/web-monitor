package validation

type AddHttpErrorLog struct {
	LogType           string `form:"logType"`
	ProjectIdentifier string `form:"projectIdentifier"`
	Cuuid             string `form:"cUuid"`
	Buid              uint64 `form:"bUid"`
	Buname            string `form:"bUname"`
	PageUrl           string `form:"pageUrl"`
	PageKey           string `form:"pageKey"`
	DeviceName        string `form:"deviceName"`
	Os                string `form:"os"`
	OsVersion         string `form:"osVersion"`
	BrowserName       string `form:"browserName"`
	BrowserVersion    string `form:"browserVersion"`
	IpAddress         string `form:"ipAddress"`
	Address           string `form:"address"`
	NetType           string `form:"netType"`
	HttpType          string `form:"httpType"`
	HttpUrlComplete   string `form:"httpUrlComplete"`
	HttpUrlShort      string `form:"httpUrlShort"`
	Status            string `form:"status"`
	StatusText        string `form:"statusText"`
	ResTime           string `form:"resTime"`
}

type GetHttpErrorLog struct {
	PageInfo
	StartTime         string `form:"startTime" binding:"omitempty,datetime=2006-01-02 15:04:05"`
	EndTime           string `form:"endTime" binding:"omitempty,datetime=2006-01-02 15:04:05"`
	LogType           string `form:"logType"`
	ProjectIdentifier string `form:"projectIdentifier"`
	Buname            string `form:"bUname"`
	PageUrl           string `form:"pageUrl"`
	HttpType          string `form:"httpType"`
	HttpUrlComplete   string `form:"httpUrlComplete"`
	HttpUrlShort      string `form:"httpUrlShort"`
	Status            string `form:"status"`
	StatusText        string `form:"statusText"`
}

type GetHttpErrorLogByGroup struct {
	PageInfo
	StartTime         string `form:"startTime" binding:"omitempty,datetime=2006-01-02 15:04:05"`
	EndTime           string `form:"endTime" binding:"omitempty,datetime=2006-01-02 15:04:05"`
	LogType           string `form:"logType"`
	ProjectIdentifier string `form:"projectIdentifier"`
	Buname            string `form:"bUname"`
	PageUrl           string `form:"pageUrl"`
	HttpType          string `form:"httpType"`
	HttpUrlComplete   string `form:"httpUrlComplete"`
	HttpUrlShort      string `form:"httpUrlShort"`
	Status            string `form:"status"`
	StatusText        string `form:"statusText"`
}

type GetLogCountByState struct {
	StartTime         string `form:"startTime" binding:"required,datetime=2006-01-02 15:04:05"`
	EndTime           string `form:"endTime" binding:"required,datetime=2006-01-02 15:04:05"`
	ProjectIdentifier string `form:"projectIdentifier" binding:"required"`
}
