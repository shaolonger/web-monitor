package initialize

import (
	"go.uber.org/zap"
	"gorm.io/driver/mysql"
	"gorm.io/gorm"
	"gorm.io/gorm/logger"
	"os"
	"web.monitor.com/global"
	"web.monitor.com/initialize/internal"
	"web.monitor.com/model"
)

func Gorm() *gorm.DB {
	switch global.WM_CONFIG.System.DbType {
	case "mysql":
		return GormMysql()
	default:
		return GormMysql()
	}
}

func GormMysql() *gorm.DB {
	m := global.WM_CONFIG.Mysql
	if m.Dbname == "" {
		global.WM_LOG.Error("MySQL数据库配置有问题")
		os.Exit(0)
		return nil
	}
	dsn := m.Username + ":" + m.Password + "@tcp(" + m.Path + ")/" + m.Dbname + "?" + m.Config
	mysqlConfig := mysql.Config{
		DSN:                       dsn,   // DSN data source name
		DefaultStringSize:         191,   // string 类型字段的默认长度
		DisableDatetimePrecision:  true,  // 禁用 datetime 精度，MySQL 5.6 之前的数据库不支持
		DontSupportRenameIndex:    true,  // 重命名索引时采用删除并新建的方式，MySQL 5.7 之前的数据库和 MariaDB 不支持重命名索引
		DontSupportRenameColumn:   true,  // 用 `change` 重命名列，MySQL 8 之前的数据库和 MariaDB 不支持重命名列
		SkipInitializeWithVersion: false, // 根据版本自动配置
	}
	if db, err := gorm.Open(mysql.New(mysqlConfig), gormConfig(m.LogMode)); err != nil {
		global.WM_LOG.Error("MySQL启动异常", zap.Any("err", err))
		os.Exit(0)
		return nil
	} else {
		sqlDB, _ := db.DB()
		sqlDB.SetMaxIdleConns(m.MaxIdleConns)
		sqlDB.SetMaxOpenConns(m.MaxOpenConns)
		return db
	}
}

func gormConfig(mod bool) *gorm.Config {
	var config = &gorm.Config{DisableForeignKeyConstraintWhenMigrating: true}
	switch global.WM_CONFIG.Mysql.LogZap {
	case "silent", "Silent":
		config.Logger = internal.Default.LogMode(logger.Silent)
	case "error", "Error":
		config.Logger = internal.Default.LogMode(logger.Error)
	case "warn", "Warn":
		config.Logger = internal.Default.LogMode(logger.Warn)
	case "info", "Info":
		config.Logger = internal.Default.LogMode(logger.Info)
	case "zap", "Zap":
		config.Logger = internal.Default.LogMode(logger.Info)
	default:
		if mod {
			config.Logger = internal.Default.LogMode(logger.Info)
			break
		}
		config.Logger = internal.Default.LogMode(logger.Silent)
	}
	return config
}

// AutoMigrateMysqlTables
// @description: 自动迁移数据库表结构
func AutoMigrateMysqlTables(db *gorm.DB) {
	var err error

	// 自定义连接表(也就是gorm里所谓的"JoinTable"，实际上就是associative table或junction table)
	err = db.SetupJoinTable(&model.UmsUser{}, "PmsProjects", &model.UmsUserProjectRelation{})
	if err != nil {
		global.WM_LOG.Error("自定义连接表失败:UmsUserProjectRelation", zap.Any("err", err))
		os.Exit(0)
	}
	err = db.SetupJoinTable(&model.PmsProject{}, "UmsUsers", &model.UmsUserProjectRelation{})
	if err != nil {
		global.WM_LOG.Error("自定义连接表失败:UmsUserProjectRelation", zap.Any("err", err))
		os.Exit(0)
	}

	// 自动迁移数据库表结构
	err = db.AutoMigrate(
		&model.AmsAlarm{},
		&model.AmsAlarmRecord{},
		&model.AmsAlarmSchedulerRelation{},
		&model.AmsSubscriber{},
		&model.AmsSubscriberNotifyRecord{},
		&model.LmsClientUser{},
		&model.LmsCustomErrorLog{},
		&model.LmsHttpErrorLog{},
		&model.LmsJsErrorLog{},
		&model.LmsResourceLoadErrorLog{},
		&model.PmsProject{},
		&model.TmsScheduler{},
		&model.TmsSchedulerRecord{},
		&model.UmsUser{},
		&model.UmsUserRegisterRecord{},
	)
	if err != nil {
		global.WM_LOG.Error("自动迁移数据库表结构失败", zap.Any("err", err))
		os.Exit(0)
	}
	global.WM_LOG.Info("自动迁移数据库表结构成功")
}
