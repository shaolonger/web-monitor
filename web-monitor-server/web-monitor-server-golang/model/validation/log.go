package validation

type AddLog struct {
	LogType string `form:"logType" binding:"required"`
}
