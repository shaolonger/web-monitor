package router

import (
	"github.com/gin-gonic/gin"
	v1 "web.monitor.com/api/v1"
)

// InitJsErrorLogRouterPublic 公开路由，不需要权限校验
func InitJsErrorLogRouterPublic(Router *gin.RouterGroup) {
	BaseRouter := Router.Group("jsErrorLog")
	{
		BaseRouter.PUT("add", v1.AddJsErrorLog) // 新增js异常日志
	}
}

// InitJsErrorLogRouterPrivate 私有路由，需要权限校验
func InitJsErrorLogRouterPrivate(Router *gin.RouterGroup) {
	BaseRouter := Router.Group("jsErrorLog")
	{
		BaseRouter.GET("get", v1.GetJsErrorLog)               // 条件查询js异常日志
		BaseRouter.GET("getByGroup", v1.GetJsErrorLogByGroup) // 聚合查询js异常日志
	}
}
