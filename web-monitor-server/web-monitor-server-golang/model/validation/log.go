package validation

type AddLog struct {
	LogType string `form:"logType" binding:"required"`
}

type AddClient struct {
	Cuuid  string `form:"cUuid" binding:"required"`
	Buid   uint64 `form:"bUid"`
	Buname string `form:"bUname"`
}
