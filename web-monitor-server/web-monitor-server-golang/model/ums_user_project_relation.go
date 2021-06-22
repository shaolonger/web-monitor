package model

type UmsUserProjectRelation struct {
	Id        uint64 `json:"id" gorm:"primaryKey;autoIncrement;comment:ID"`
	UserId    uint64 `json:"userId" gorm:"not null;comment:关联的用户ID"`
	ProjectId uint64 `json:"projectId" gorm:"not null;comment:关联的项目ID"`
}

func (u UmsUserProjectRelation) TableName() string {
	return "ums_user_project_relation"
}
