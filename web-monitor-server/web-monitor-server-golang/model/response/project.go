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
