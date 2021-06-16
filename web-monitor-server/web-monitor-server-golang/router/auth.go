package router

import (
	"github.com/gin-gonic/gin"
	"web.monitor.com/api/v1"
)

func InitAuthRouterPublic(Router *gin.RouterGroup) {
	BaseRouter := Router.Group("userRegisterRecord")
	{
		BaseRouter.PUT("add", v1.AddUserRegisterRecord) // 注册
	}
}

func InitAuthRouterPrivate(Router *gin.RouterGroup) {
	BaseRouter := Router.Group("userRegisterRecord")
	{
		BaseRouter.GET("get", v1.GetUserRegisterRecord)      // 查询用户注册记录
		BaseRouter.POST("audit", v1.AuditUserRegisterRecord) // 用户注册记录审批
	}
}
