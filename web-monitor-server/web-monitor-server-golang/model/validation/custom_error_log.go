package validation

type AddCustomErrorLog struct {
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
	ErrorType         string `form:"errorType"`
	ErrorMessage      string `form:"errorMessage"`
	ErrorStack        string `form:"errorStack"`
}

type GetCustomErrorLog struct {
	PageInfo
	StartTime         string `form:"startTime" binding:"omitempty,datetime=2006-01-02 15:04:05"`
	EndTime           string `form:"endTime" binding:"omitempty,datetime=2006-01-02 15:04:05"`
	LogType           string `form:"logType"`
	ProjectIdentifier string `form:"projectIdentifier"`
	Buname            string `form:"bUname"`
	PageUrl           string `form:"pageUrl"`
	ErrorType         string `form:"errorType"`
	ErrorMessage      string `form:"errorMessage"`
}

type GetCustomErrorLogByGroup struct {
	PageInfo
	StartTime         string `form:"startTime" binding:"omitempty,datetime=2006-01-02 15:04:05"`
	EndTime           string `form:"endTime" binding:"omitempty,datetime=2006-01-02 15:04:05"`
	LogType           string `form:"logType"`
	ProjectIdentifier string `form:"projectIdentifier"`
	Buname            string `form:"bUname"`
	PageUrl           string `form:"pageUrl"`
	ErrorType         string `form:"errorType"`
	ErrorMessage      string `form:"errorMessage"`
}
