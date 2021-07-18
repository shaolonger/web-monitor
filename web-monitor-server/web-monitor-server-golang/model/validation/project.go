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

type GetProjectByProjectIdentifier struct {
	ProjectIdentifier string `form:"projectIdentifier" binding:"required"`
}

type UpdateProject struct {
	Id                uint64   `json:"id"`
	ProjectName       string   `form:"projectName"`
	ProjectIdentifier string   `form:"projectIdentifier"`
	Description       string   `form:"description"`
	UserList          []uint64 `form:"userList"`
	AccessType        string   `form:"accessType"`
	ActiveFuncs       string   `form:"activeFuncs"`
	IsAutoUpload      uint8    `form:"isAutoUpload"`
}
