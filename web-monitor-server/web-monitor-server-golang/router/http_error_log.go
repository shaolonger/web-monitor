package router

import (
	"github.com/gin-gonic/gin"
	v1 "web.monitor.com/api/v1"
)

// InitHttpErrorLogRouterPublic 公开路由，不需要权限校验
func InitHttpErrorLogRouterPublic(Router *gin.RouterGroup) {
	BaseRouter := Router.Group("httpErrorLog")
	{
		BaseRouter.PUT("add", v1.AddHttpErrorLog) // 新增http异常日志
	}
}

// InitHttpErrorLogRouterPrivate 私有路由，需要权限校验
func InitHttpErrorLogRouterPrivate(Router *gin.RouterGroup) {
	BaseRouter := Router.Group("httpErrorLog")
	{
		BaseRouter.GET("get", v1.GetHttpErrorLog)                   // 条件查询http异常日志
		BaseRouter.GET("getByGroup", v1.GetHttpErrorLogByGroup)     // 聚合查询http异常日志
		BaseRouter.GET("getLogCountByState", v1.GetLogCountByState) // 按status分类获取http日志数量
	}
}
