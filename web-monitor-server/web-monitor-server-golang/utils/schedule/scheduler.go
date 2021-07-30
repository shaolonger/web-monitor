package schedule

import (
	"fmt"
	"github.com/robfig/cron/v3"
	"go.uber.org/zap"
	"web.monitor.com/global"
)

var scheduleTasks = make(map[*Scheduler]*cron.Cron)

// Scheduler
// 此处的BeanName、MethodName，是为了保持与Java后台写法一致所以保留的
// 在Java后台里由于使用了Spring框架的Bean进行实例化注入，但在Go里没必要这么做
type Scheduler struct {
	BeanName       string
	MethodName     string
	Params         string
	SchedulerId    uint64
	CronExpression string
}

// Start
// 开始定时任务
func (s Scheduler) Start(f func(s string) error) error {
	global.WM_LOG.Info("定时任务开始执行", zap.Any("info", fmt.Sprintf("bean：%v，方法：%v，参数：%v", s.BeanName, s.MethodName, s.Params)))
	err := f(s.Params)
	return err
}
