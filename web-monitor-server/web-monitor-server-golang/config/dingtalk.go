package config

type Dingtalk struct {
	Enable  bool   `mapstructure:"enable" json:"enable" yaml:"enable"`
	Keyword string `mapstructure:"keyword" json:"keyword" yaml:"keyword"`
}
