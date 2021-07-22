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

type GetLogCountByDays struct {
	StartTime         string `form:"startTime" binding:"required,datetime=2006-01-02"`
	EndTime           string `form:"endTime" binding:"required,datetime=2006-01-02"`
	LogType           string `form:"logType" binding:"required"`
	ProjectIdentifier string `form:"projectIdentifier" binding:"required"`
}

type GetLogCountBetweenDiffDate struct {
	StartTime         string `form:"startTime" binding:"required,datetime=2006-01-02 15:04:05"`
	EndTime           string `form:"endTime" binding:"required,datetime=2006-01-02 15:04:05"`
	ProjectIdentifier string `form:"projectIdentifier" binding:"required"`
	LogTypeList       string `form:"logTypeList" binding:"required"`
	IndicatorList     string `form:"indicatorList" binding:"required"`
	TimeInterval      int    `form:"timeInterval" binding:"required,number,gt=60"`
}

type GetLogDistributionBetweenDiffDate struct {
	StartTime         string `form:"startTime" binding:"required,datetime=2006-01-02 15:04:05"`
	EndTime           string `form:"endTime" binding:"required,datetime=2006-01-02 15:04:05"`
	ProjectIdentifier string `form:"projectIdentifier" binding:"required"`
	LogType           string `form:"logType" binding:"required"`
	Indicator         string `form:"indicator" binding:"required"`
}

type GetAllProjectOverviewListBetweenDiffDate struct {
	StartTime string `form:"startTime" binding:"required,datetime=2006-01-02 15:04:05"`
	EndTime   string `form:"endTime" binding:"required,datetime=2006-01-02 15:04:05"`
}
