package response

import "time"

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

type GetLogListByCreateTimeAndProjectIdentifier struct {
	Id         int64     `json:"id"`
	Cuuid      string    `json:"cUuid"`
	CreateTime time.Time `json:"createTime"`
}

type GetLogListByCreateTimeAndProjectIdentifierResult struct {
	Uv    int64  `json:"uv"`
	Count int64  `json:"count"`
	Key   string `json:"key"`
}
