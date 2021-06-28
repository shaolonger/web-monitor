package service

import (
	"fmt"
	"go.uber.org/zap"
	"time"
	"web.monitor.com/global"
	"web.monitor.com/model"
	"web.monitor.com/model/validation"
)

func AddProject(r validation.AddProject) (err error, data interface{}) {
	var project model.PmsProject
	var userList []*model.UmsUser
	db := global.WM_DB.Model(&model.PmsProject{})

	// 保存关联关系
	err, userList = GetUserListByUserIdList(r.UserList)
	if err != nil {
		global.WM_LOG.Error("查询用户列表失败", zap.Any("err", err))
		return err, nil
	}

	// 保存实体
	project = model.PmsProject{
		ProjectName:       r.ProjectName,
		ProjectIdentifier: r.ProjectIdentifier,
		Description:       r.Description,
		AccessType:        r.AccessType,
		ActiveFuncs:       r.ActiveFuncs,
		IsAutoUpload:      r.IsAutoUpload,
		CreateTime:        time.Now(),
		UpdateTime:        time.Now(),
		NotifyDtToken:     r.NotifyDtToken,
		NotifyEmail:       r.NotifyEmail,
		UmsUsers:          userList,
	}
	err = db.Create(&project).Error
	if err != nil {
		global.WM_LOG.Error("保存项目实体失败", zap.Any("err", err))
		return err, nil
	}
	fmt.Println(userList)
	return nil, project
}
