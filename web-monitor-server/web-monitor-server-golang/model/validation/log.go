package validation

type AddLog struct {
	LogType string `form:"logType" binding:"required"`
}

type AddClient struct {
	Cuuid  string `form:"cUuid" binding:"required"`
	Buid   uint64 `form:"bUid"`
	Buname string `form:"bUname"`
}

type conditionListItem struct {
	Key   string `form:"key"`
	Value string `form:"value"`
	Op    string `form:"op"`
}

type ListLog struct {
	PageInfo
	StartTime         string              `form:"startTime" json:"startTime" binding:"omitempty,datetime=2006-01-02 15:04:05"`
	EndTime           string              `form:"endTime" json:"endTime" binding:"omitempty,datetime=2006-01-02 15:04:05"`
	LogType           string              `form:"logType" json:"logType"`
	ProjectIdentifier string              `form:"projectIdentifier" json:"projectIdentifier"`
	ConditionList     []conditionListItem `form:"conditionList" json:"conditionList" binding:"required"`
}
