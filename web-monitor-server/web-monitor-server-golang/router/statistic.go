package router

import (
	"github.com/gin-gonic/gin"
	v1 "web.monitor.com/api/v1"
)

// InitStatisticRouterPrivate 私有路由，需要权限校验
func InitStatisticRouterPrivate(Router *gin.RouterGroup) {
	BaseRouter := Router.Group("statistic")
	{
		BaseRouter.GET("getOverallByTimeRange", v1.GetStatisticOverallByTimeRange)                              // 获取总览页信息
		BaseRouter.GET("getLogCountByHours", v1.GetLogCountByHours)                                             // 按小时间隔获取各小时内的日志数量
		BaseRouter.GET("getLogCountByDays", v1.GetLogCountByDays)                                               // 按天间隔获取各日期内的日志数量
		BaseRouter.GET("getLogCountBetweenDiffDate", v1.GetLogCountBetweenDiffDate)                             // 按天间隔获取两个日期之间的对比数据
		BaseRouter.GET("getLogDistributionBetweenDiffDate", v1.GetLogDistributionBetweenDiffDate)               // 获取两个日期之间的设备、操作系统、浏览器、网络类型的统计数据
		BaseRouter.GET("getAllProjectOverviewListBetweenDiffDate", v1.GetAllProjectOverviewListBetweenDiffDate) // 获取用户关联的所有项目的统计情况列表
	}
}
