package service

import (
	"errors"
	"go.uber.org/zap"
	"gorm.io/gorm"
	"math"
	"strconv"
	"time"
	"web.monitor.com/global"
	"web.monitor.com/model"
	"web.monitor.com/model/response"
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
	return nil, project
}

func GetProject(r validation.GetProject) (err error, data interface{}) {
	var projects []model.PmsProject
	var records []response.ProjectListItem
	db := global.WM_DB.Model(&model.PmsProject{})
	var totalNum int64
	limit := r.PageSize
	offset := limit * (r.PageNum - 1)
	// 项目名称
	if r.ProjectName != "" {
		db = db.Where("`project_name` like ?", "%"+r.ProjectName+"%")
	}
	err = db.Count(&totalNum).Error
	err = db.Limit(limit).Offset(offset).Preload("UmsUsers").Find(&projects).Error
	if err != nil {
		return err, nil
	}
	for _, project := range projects {
		var userList string
		if len(project.UmsUsers) > 0 {
			for _, umsUser := range project.UmsUsers {
				userList = userList + strconv.FormatUint(umsUser.Id, 10) + ","
			}
			userList = userList[:len(userList)-1] // 移除最后一个逗号
		}
		records = append(records, response.ProjectListItem{
			Id:                project.Id,
			ProjectName:       project.ProjectName,
			ProjectIdentifier: project.ProjectIdentifier,
			Description:       project.Description,
			CreateTime:        project.CreateTime,
			UpdateTime:        project.UpdateTime,
			UserList:          userList,
		})
	}
	data = map[string]interface{}{
		"totalNum":  totalNum,
		"totalPage": math.Ceil(float64(totalNum) / float64(r.PageSize)),
		"pageNum":   r.PageNum,
		"pageSize":  r.PageSize,
		"records":   records,
	}
	return err, data
}

func GetProjectByProjectIdentifier(r validation.GetProjectByProjectIdentifier) (err error, data interface{}) {
	var project model.PmsProject
	db := global.WM_DB.Model(&model.PmsProject{})
	// 项目标识
	if r.ProjectIdentifier != "" {
		db = db.Where("`project_identifier` = ?", r.ProjectIdentifier)
	}
	err = db.First(&project).Error
	if err != nil {
		return err, nil
	}
	projectRes := response.GetByProjectIdentifier{
		Id:                project.Id,
		ProjectName:       project.ProjectName,
		ProjectIdentifier: project.ProjectIdentifier,
		Description:       project.Description,
		AccessType:        project.AccessType,
		ActiveFuncs:       project.ActiveFuncs,
		CreateTime:        project.CreateTime,
		UpdateTime:        project.UpdateTime,
	}
	return err, projectRes
}

func UpdateProject(r validation.UpdateProject) (err error, data interface{}) {
	var project model.PmsProject
	var userList []*model.UmsUser
	db := global.WM_DB.Model(&model.PmsProject{})
	db = db.Where("`id` = ?", r.Id)
	// 获取要更新的实体
	err = db.Preload("UmsUsers").First(&project).Error
	if err != nil {
		if errors.Is(err, gorm.ErrRecordNotFound) {
			err = errors.New("项目不存在")
		}
		return err, nil
	}
	// 保存关联关系
	err, userList = GetUserListByUserIdList(r.UserList)
	if err != nil {
		global.WM_LOG.Error("查询用户列表失败", zap.Any("err", err))
		return err, nil
	}
	// 保存实体
	project.ProjectName = r.ProjectName
	project.ProjectIdentifier = r.ProjectIdentifier
	project.Description = r.Description
	project.AccessType = r.AccessType
	project.ActiveFuncs = r.ActiveFuncs
	project.IsAutoUpload = r.IsAutoUpload
	project.UpdateTime = time.Now()
	project.UmsUsers = userList
	err = db.Session(&gorm.Session{FullSaveAssociations: true}).Updates(&project).Error
	if err != nil {
		global.WM_LOG.Error("更新项目实体失败", zap.Any("err", err))
		return err, nil
	}
	projectRes := response.UpdateByProjectIdentifier{
		Id:                project.Id,
		ProjectName:       project.ProjectName,
		ProjectIdentifier: project.ProjectIdentifier,
		Description:       project.Description,
		AccessType:        project.AccessType,
		ActiveFuncs:       project.ActiveFuncs,
		IsAutoUpload:      project.IsAutoUpload,
		CreateTime:        project.CreateTime,
		UpdateTime:        project.UpdateTime,
	}
	return nil, projectRes
}
