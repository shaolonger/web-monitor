package router

import (
	"github.com/gin-gonic/gin"
	v1 "web.monitor.com/api/v1"
)

// InitStatisticRouterPrivate 私有路由，需要权限校验
func InitStatisticRouterPrivate(Router *gin.RouterGroup) {
	BaseRouter := Router.Group("statistic")
	{
		BaseRouter.GET("getOverallByTimeRange", v1.GetStatisticOverallByTimeRange) // 获取总览页信息
	}
}
