package validation

type SchedulerRule struct {
	Op    string              `json:"op"`
	Rules []SchedulerRuleItem `json:"Rules"`
}

type SchedulerRuleItem struct {
	Agg      string  `json:"agg"`
	Ind      string  `json:"ind"`
	Interval int     `json:"interval"`
	Op       string  `json:"op"`
	TimeSpan int     `json:"timeSpan"`
	Val      float64 `json:"val"`
}
