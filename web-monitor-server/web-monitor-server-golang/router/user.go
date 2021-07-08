package router

import (
	"github.com/gin-gonic/gin"
	"web.monitor.com/api/v1"
)

// InitUserRouterPublic 公开路由，不需要权限校验
func InitUserRouterPublic(Router *gin.RouterGroup) {
	BaseRouter1 := Router.Group("userRegisterRecord")
	{
		BaseRouter1.PUT("add", v1.AddUserRegisterRecord) // 注册
	}
	BaseRouter2 := Router.Group("user")
	{
		BaseRouter2.POST("login", v1.Login) // 登录
	}
}

// InitUserRouterPrivate 私有路由，需要权限校验
func InitUserRouterPrivate(Router *gin.RouterGroup) {
	BaseRouter1 := Router.Group("userRegisterRecord")
	{
		BaseRouter1.GET("get", v1.GetUserRegisterRecord)      // 查询用户注册记录
		BaseRouter1.POST("audit", v1.AuditUserRegisterRecord) // 用户注册记录审批
	}
	BaseRouter2 := Router.Group("user")
	{
		BaseRouter2.GET("get", v1.GetUser)                                 // 多条件分页查询用户列表
		BaseRouter2.GET("getDetail", v1.GetUserDetail)                     // 查询详情
		BaseRouter2.GET("getRelatedProjectList", v1.GetRelatedProjectList) // 根据用户获取关联的项目
	}
}
