package com.monitor.web.alarm.bean;

import lombok.Data;

import java.util.List;

/**
 * 预警规则数据结构
 */
@Data
public class AlarmRuleBean {

    /**
     * &&-满足下列所有规则，||-满足下列任一规则
     */
    private String op;

    /**
     * 预警规则条目
     */
    private List<AlarmRuleItemBean> rules;
}
