package validation

type AddProject struct {
	ProjectName       string   `form:"projectName" binding:"required"`
	ProjectIdentifier string   `form:"projectIdentifier" binding:"required"`
	Description       string   `form:"description"`
	UserList          []uint64 `form:"userList"`
	AccessType        string   `form:"accessType" binding:"required"`
	ActiveFuncs       string   `form:"activeFuncs"`
	IsAutoUpload      uint8    `form:"isAutoUpload"`
	NotifyDtToken     string   `form:"notifyDtToken"`
	NotifyEmail       string   `form:"notifyEmail"`
}

type GetProject struct {
	PageInfo
	ProjectName string `form:"projectName"`
}
