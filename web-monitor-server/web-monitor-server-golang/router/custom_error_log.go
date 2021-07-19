package router

import (
	"github.com/gin-gonic/gin"
	v1 "web.monitor.com/api/v1"
)

// InitCustomErrorLogRouterPublic 公开路由，不需要权限校验
func InitCustomErrorLogRouterPublic(Router *gin.RouterGroup) {
	BaseRouter := Router.Group("customErrorLog")
	{
		BaseRouter.PUT("add", v1.AddCustomErrorLog) // 新增custom异常日志
	}
}

// InitCustomErrorLogRouterPrivate 私有路由，需要权限校验
func InitCustomErrorLogRouterPrivate(Router *gin.RouterGroup) {
	BaseRouter := Router.Group("customErrorLog")
	{
		BaseRouter.GET("get", v1.GetCustomErrorLog)               // 条件查询custom异常日志
		BaseRouter.GET("getByGroup", v1.GetCustomErrorLogByGroup) // 聚合查询custom异常日志
	}
}
