package response

import "time"

type GetResourceLoadErrorLogByGroup struct {
	Count            uint64    `json:"count"`
	LatestRecordTime time.Time `json:"latestRecordTime"`
	AffectUserCount  uint64    `json:"affectUserCount"`
	ErrorMessage     string    `json:"errorMessage"`
}

type GetOverallByTimeRange struct {
	AffectUsers  uint64 `json:"affectUsers"`
	AffectCounts uint64 `json:"affectCounts"`
	AffectPages  uint64 `json:"affectPages"`
}
