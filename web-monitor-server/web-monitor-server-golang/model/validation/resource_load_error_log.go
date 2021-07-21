package validation

type AddResourceLoadErrorLog struct {
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
	ResourceUrl       string `form:"resourceUrl"`
	ResourceType      string `form:"resourceType"`
	Status            string `form:"status"`
}

type GetResourceLoadErrorLog struct {
	PageInfo
	StartTime         string `form:"startTime" binding:"omitempty,datetime=2006-01-02 15:04:05"`
	EndTime           string `form:"endTime" binding:"omitempty,datetime=2006-01-02 15:04:05"`
	LogType           string `form:"logType"`
	ProjectIdentifier string `form:"projectIdentifier"`
	Buname            string `form:"bUname"`
	PageUrl           string `form:"pageUrl"`
	ResourceUrl       string `form:"resourceUrl"`
	ResourceType      string `form:"resourceType"`
	Status            string `form:"status"`
}

type GetResourceLoadErrorLogByGroup struct {
	PageInfo
	StartTime         string `form:"startTime" binding:"omitempty,datetime=2006-01-02 15:04:05"`
	EndTime           string `form:"endTime" binding:"omitempty,datetime=2006-01-02 15:04:05"`
	LogType           string `form:"logType"`
	ProjectIdentifier string `form:"projectIdentifier"`
	Buname            string `form:"bUname"`
	PageUrl           string `form:"pageUrl"`
	ResourceUrl       string `form:"resourceUrl"`
	ResourceType      string `form:"resourceType"`
	Status            string `form:"status"`
}

type GetOverallByTimeRange struct {
	StartTime         string `form:"startTime" binding:"required,datetime=2006-01-02 15:04:05"`
	EndTime           string `form:"endTime" binding:"required,datetime=2006-01-02 15:04:05"`
	ProjectIdentifier string `form:"projectIdentifier" binding:"required"`
}
