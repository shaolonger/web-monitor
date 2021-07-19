package validation

type AddJsErrorLog struct {
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
