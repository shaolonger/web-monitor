package com.monitor.web.alarm.bean;

import lombok.Data;

/**
 * 预警规则条目结构体
 */
@Data
public class AlarmRuleItemBean {

    /**
     * agg
     * 聚合维度，count-总和，avg-平均
     */
    private String agg;

    /**
     * ind
     * uvCount-影响用户数，uvRate-影响用户率，perPV-人均异常次数，newPV-新增异常数
     */
    private String ind;

    /**
     * op
     * >-大于，<-小于，d_up-环比昨天上涨，d_down-环比昨天下跌，w_up-环比上周上涨，w_down-环比上周下跌
     */
    private String op;

    /**
     * timeSpan
     * 时间幅度（分钟）
     */
    private int timeSpan;

    /**
     * interval
     * 时间间隔（用于agg为avg时，求均值）
     */
    private int interval;

    /**
     * val
     * 报警阈值
     */
    private float val;
}
