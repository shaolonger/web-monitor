package router

import (
	"github.com/gin-gonic/gin"
	"web.monitor.com/api/v1"
)

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

func InitUserRouterPrivate(Router *gin.RouterGroup) {
	BaseRouter1 := Router.Group("userRegisterRecord")
	{
		BaseRouter1.GET("get", v1.GetUserRegisterRecord)      // 查询用户注册记录
		BaseRouter1.POST("audit", v1.AuditUserRegisterRecord) // 用户注册记录审批
	}
}
