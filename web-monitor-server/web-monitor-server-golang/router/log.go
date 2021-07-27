package router

import (
	"github.com/gin-gonic/gin"
	v1 "web.monitor.com/api/v1"
)

// InitLogRouterPublic 公开路由，不需要权限校验
func InitLogRouterPublic(Router *gin.RouterGroup) {
	BaseRouter := Router.Group("log")
	{
		BaseRouter.GET("add", v1.AddLog)           // 通用日志打点上传
		BaseRouter.GET("client/add", v1.AddClient) // 新增日志客户端用户
	}
}

// InitLogRouterPrivate 私有路由，需要权限校验
func InitLogRouterPrivate(Router *gin.RouterGroup) {
	BaseRouter := Router.Group("log")
	{
		BaseRouter.POST("list", v1.ListLog) // 多条件高级查询
	}
}
