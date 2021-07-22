package response

type GetStatisticOverallByTimeRange struct {
	CustomErrorLogCount       int64 `json:"customErrorLogCount"`
	HttpErrorLogCount         int64 `json:"httpErrorLogCount"`
	ResourceLoadErrorLogCount int64 `json:"resourceLoadErrorLogCount"`
	JsErrorLogCount           int64 `json:"jsErrorLogCount"`
}

type GetLogCountByHours struct {
	Hour  string `json:"hour"`
	Count int64  `json:"count"`
}

type GetLogCountByDays struct {
	Day   string `json:"day"`
	Count int64  `json:"count"`
}
