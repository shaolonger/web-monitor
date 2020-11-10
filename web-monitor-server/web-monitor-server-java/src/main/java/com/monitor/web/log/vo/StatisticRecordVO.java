package com.monitor.web.log.vo;

import lombok.Data;

import java.util.Date;

@Data
public class StatisticRecordVO {

    // 数量
    private int count;

    // 最后一条记录的时间
    private Date latestRecordTime;

    // 影响用户数量
    private int affectUserCount;

    // 异常信息
    private String errorMessage;
}
