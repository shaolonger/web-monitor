package com.monitor.web.log.controller;

import com.monitor.web.log.service.StatisticService;
import com.monitor.web.common.api.ResponseResultBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class StatisticController {

    @Autowired
    StatisticService statisticService;

    /**
     * 获取总览统计信息
     *
     * @param request request
     * @return Object
     */
    @RequestMapping(value = "/statistic/getOverallByTimeRange", method = RequestMethod.GET)
    public Object getOverall(HttpServletRequest request) {
        try {
            return ResponseResultBase.getResponseResultBase(statisticService.getOverallByTimeRangeByRequest(request));
        } catch (Exception e) {
            return ResponseResultBase.getErrorResponseResult(e);
        }
    }

    /**
     * 按小时间隔，获取各小时内的日志数量
     *
     * @param request request
     * @return Object
     */
    @RequestMapping(value = "/statistic/getLogCountByHours", method = RequestMethod.GET)
    public Object getLogCountByHours(HttpServletRequest request) {
        try {
            return ResponseResultBase.getResponseResultBase(statisticService.getLogCountByHours(request));
        } catch (Exception e) {
            return ResponseResultBase.getErrorResponseResult(e);
        }
    }

    /**
     * 按天间隔，获取各日期内的日志数量
     *
     * @param request request
     * @return Object
     */
    @RequestMapping(value = "/statistic/getLogCountByDays", method = RequestMethod.GET)
    public Object getLogCountByDays(HttpServletRequest request) {
        try {
            return ResponseResultBase.getResponseResultBase(statisticService.getLogCountByDays(request));
        } catch (Exception e) {
            return ResponseResultBase.getErrorResponseResult(e);
        }
    }

    /**
     * 按天间隔，获取两个日期之间的对比数据
     *
     * @param request request
     * @return Object
     */
    @RequestMapping(value = "/statistic/getLogCountBetweenDiffDate", method = RequestMethod.GET)
    public Object getLogCountBetweenDiffDate(HttpServletRequest request) {
        try {
            return ResponseResultBase.getResponseResultBase(statisticService.getLogCountBetweenDiffDate(request));
        } catch (Exception e) {
            return ResponseResultBase.getErrorResponseResult(e);
        }
    }

    /**
     * 获取两个日期之间的设备、操作系统、浏览器、网络类型的统计数据
     *
     * @param request request
     * @return Object
     */
    @RequestMapping(value = "/statistic/getLogDistributionBetweenDiffDate", method = RequestMethod.GET)
    public Object getLogDistributionBetweenDiffDate(HttpServletRequest request) {
        try {
            return ResponseResultBase.getResponseResultBase(statisticService.getLogDistributionBetweenDiffDate(request));
        } catch (Exception e) {
            return ResponseResultBase.getErrorResponseResult(e);
        }
    }

    /**
     * 获取用户关联的所有项目的统计情况列表
     *
     * @param request request
     * @return Object
     */
    @RequestMapping(value = "/statistic/getAllProjectOverviewListBetweenDiffDate", method = RequestMethod.GET)
    public Object getAllProjectOverviewListBetweenDiffDate(HttpServletRequest request) {
        try {
            return ResponseResultBase.getResponseResultBase(statisticService.getAllProjectOverviewListBetweenDiffDate(request));
        } catch (Exception e) {
            return ResponseResultBase.getErrorResponseResult(e);
        }
    }
}
