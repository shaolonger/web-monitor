package router

import (
	"github.com/gin-gonic/gin"
	v1 "web.monitor.com/api/v1"
)

// 私有路由，需要权限校验
func InitProjectRouterPrivate(Router *gin.RouterGroup) {
	BaseRouter := Router.Group("project")
	{
		BaseRouter.PUT("add", v1.AddProject) // 新增项目
	}
}
