package validation

type GetStatisticOverallByTimeRange struct {
	StartTime         string `form:"startTime" binding:"required,datetime=2006-01-02 15:04:05"`
	EndTime           string `form:"endTime" binding:"required,datetime=2006-01-02 15:04:05"`
	ProjectIdentifier string `form:"projectIdentifier" binding:"required"`
}

type GetLogCountByHours struct {
	StartTime         string `form:"startTime" binding:"required,datetime=2006-01-02 15:04:05"`
	EndTime           string `form:"endTime" binding:"required,datetime=2006-01-02 15:04:05"`
	LogType           string `form:"logType" binding:"required"`
	ProjectIdentifier string `form:"projectIdentifier" binding:"required"`
}
