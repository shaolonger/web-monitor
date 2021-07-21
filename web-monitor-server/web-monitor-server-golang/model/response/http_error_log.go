package response

import "time"

type GetHttpErrorLogByGroup struct {
	Count            uint64    `json:"count"`
	LatestRecordTime time.Time `json:"latestRecordTime"`
	AffectUserCount  uint64    `json:"affectUserCount"`
	ErrorMessage     string    `json:"errorMessage"`
}

type GetLogCountByState struct {
	Status string `json:"status"`
	Count  uint64 `json:"count"`
}
