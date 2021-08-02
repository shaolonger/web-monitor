package validation

import "time"

type AddAlarm struct {
	Name              string       `form:"name" json:"name" binding:"required"`
	ProjectIdentifier string       `form:"projectIdentifier" json:"projectIdentifier" binding:"required"`
	Level             *int8        `form:"level" json:"level" binding:"required,number,oneof=-1 0 1 2"`
	Category          *int8        `form:"category" json:"category" binding:"required,number,oneof=0 1 2 3 4"`
	Rule              string       `form:"rule" json:"rule" binding:"required"`
	StartTime         string       `form:"startTime" json:"startTime" binding:"omitempty,datetime=15:04:05"`
	EndTime           string       `form:"endTime" json:"endTime" binding:"omitempty,datetime=15:04:05"`
	SilentPeriod      *int8        `form:"silentPeriod" json:"silentPeriod" binding:"required,number,oneof=0 1 2 3 4 5 6 7 8 9"`
	IsActive          *int8        `form:"isActive" json:"isActive" binding:"required,number,oneof=0 1"`
	CreateBy          *uint64      `form:"createBy" json:"createBy"`
	SubscriberList    []Subscriber `form:"subscriberList" json:"subscriberList" binding:"required"`
}

type UpdateAlarm struct {
	Id                *uint64      `form:"id" json:"id" binding:"required"`
	Name              string       `form:"name" json:"name" binding:"omitempty"`
	ProjectIdentifier string       `form:"projectIdentifier" json:"projectIdentifier" binding:"omitempty"`
	Level             *int8        `form:"level" json:"level" binding:"omitempty,number,oneof=-1 0 1 2"`
	Category          *int8        `form:"category" json:"category" binding:"omitempty,number,oneof=0 1 2 3 4"`
	Rule              string       `form:"rule" json:"rule" binding:"omitempty"`
	StartTime         string       `form:"startTime" json:"startTime" binding:"omitempty,datetime=15:04:05"`
	EndTime           string       `form:"endTime" json:"endTime" binding:"omitempty,datetime=15:04:05"`
	SilentPeriod      *int8        `form:"silentPeriod" json:"silentPeriod" binding:"omitempty,number,oneof=0 1 2 3 4 5 6 7 8 9"`
	IsActive          *int8        `form:"isActive" json:"isActive" binding:"omitempty,number,oneof=0 1"`
	SubscriberList    []Subscriber `form:"subscriberList" json:"subscriberList"`
	IsDeleted         *int8        `form:"isDeleted" json:"isDeleted" binding:"omitempty,number,oneof=0 1"`
}

type GetAlarm struct {
	PageInfo
	Name              string  `form:"name" json:"name" binding:"omitempty"`
	ProjectIdentifier string  `form:"projectIdentifier" json:"projectIdentifier" binding:"omitempty"`
	Level             *int8   `form:"level" json:"level" binding:"omitempty,number,oneof=-1 0 1 2"`
	Category          *int8   `form:"category" json:"category" binding:"omitempty,number,oneof=0 1 2 3 4"`
	Rule              string  `form:"rule" json:"rule" binding:"omitempty"`
	StartTime         string  `form:"startTime" json:"startTime" binding:"omitempty,datetime=15:04:05"`
	EndTime           string  `form:"endTime" json:"endTime" binding:"omitempty,datetime=15:04:05"`
	SilentPeriod      *int8   `form:"silentPeriod" json:"silentPeriod" binding:"omitempty,number,oneof=0 1 2 3 4 5 6 7 8 9"`
	IsActive          *int8   `form:"isActive" json:"isActive" binding:"omitempty,number,oneof=0 1"`
	CreateBy          *uint64 `form:"createBy" json:"createBy"`
	IsDeleted         *int8   `form:"isDeleted" json:"isDeleted" binding:"omitempty,number,oneof=0 1"`
}

type AlarmScheduleResult struct {
	IsExceedAlarmThreshold bool      `json:"isExceedAlarmThreshold"`
	TargetInd              string    `json:"targetInd"`
	ActualValue            float64   `json:"actualValue"`
	ThresholdValue         float64   `json:"thresholdValue"`
	StartTime              time.Time `json:"StartTime"`
	EndTime                time.Time `json:"endTime"`
}
