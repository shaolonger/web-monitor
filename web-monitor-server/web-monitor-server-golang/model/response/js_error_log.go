package response

import "time"

type GetJsErrorLogByGroup struct {
	Count            uint64    `json:"count"`
	LatestRecordTime time.Time `json:"latestRecordTime"`
	AffectUserCount  uint64    `json:"affectUserCount"`
	ErrorMessage     string    `json:"errorMessage"`
}
