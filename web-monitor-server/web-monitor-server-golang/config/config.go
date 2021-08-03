package config

type Server struct {
	// zap
	Zap    Zap    `mapstructure:"zap" json:"zap" yaml:"zap"`
	System System `mapstructure:"system" json:"system" yaml:"system"`
	// gorm
	Mysql Mysql `mapstructure:"mysql" json:"mysql" yaml:"mysql"`
	// redis
	Redis Redis `mapstructure:"redis" json:"redis" yaml:"redis"`
	// dingtalk
	Dingtalk Dingtalk `mapstructure:"dingtalk" json:"dingtalk" yaml:"dingtalk"`
}
