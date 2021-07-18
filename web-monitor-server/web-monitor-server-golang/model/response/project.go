package response

import "time"

type ProjectListItem struct {
	Id                uint64    `json:"id"`
	ProjectName       string    `json:"projectName"`
	ProjectIdentifier string    `json:"projectIdentifier"`
	Description       string    `json:"description"`
	CreateTime        time.Time `json:"createTime"`
	UpdateTime        time.Time `json:"updateTime"`
	UserList          string    `json:"userList"`
}

type GetByProjectIdentifier struct {
	Id                uint64    `json:"id"`
	ProjectName       string    `json:"projectName"`
	ProjectIdentifier string    `json:"projectIdentifier"`
	Description       string    `json:"description"`
	AccessType        string    `json:"accessType"`
	ActiveFuncs       string    `json:"activeFuncs"`
	CreateTime        time.Time `json:"createTime"`
	UpdateTime        time.Time `json:"updateTime"`
}

type UpdateByProjectIdentifier struct {
	Id                uint64    `json:"id"`
	ProjectName       string    `json:"projectName"`
	ProjectIdentifier string    `json:"projectIdentifier"`
	Description       string    `json:"description"`
	AccessType        string    `json:"accessType"`
	ActiveFuncs       string    `json:"activeFuncs"`
	IsAutoUpload      uint8     `json:"isAutoUpload"`
	CreateTime        time.Time `json:"createTime"`
	UpdateTime        time.Time `json:"updateTime"`
}
