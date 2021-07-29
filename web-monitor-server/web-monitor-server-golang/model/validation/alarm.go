package validation

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
