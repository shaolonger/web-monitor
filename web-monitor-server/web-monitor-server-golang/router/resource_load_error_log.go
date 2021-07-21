package router

import (
	"github.com/gin-gonic/gin"
	v1 "web.monitor.com/api/v1"
)

// InitResourceLoadErrorLogRouterPublic 公开路由，不需要权限校验
func InitResourceLoadErrorLogRouterPublic(Router *gin.RouterGroup) {
	BaseRouter := Router.Group("resourceLoadErrorLog")
	{
		BaseRouter.PUT("add", v1.AddResourceLoadErrorLog) // 新增resourceLoad异常日志
	}
}

// InitResourceLoadErrorLogRouterPrivate 私有路由，需要权限校验
func InitResourceLoadErrorLogRouterPrivate(Router *gin.RouterGroup) {
	BaseRouter := Router.Group("resourceLoadErrorLog")
	{
		BaseRouter.GET("get", v1.GetResourceLoadErrorLog)                 // 条件查询resourceLoad异常日志
		BaseRouter.GET("getByGroup", v1.GetResourceLoadErrorLogByGroup)   // 聚合查询resourceLoad异常日志
		BaseRouter.GET("getOverallByTimeRange", v1.GetOverallByTimeRange) // 获取总览统计信息
	}
}
