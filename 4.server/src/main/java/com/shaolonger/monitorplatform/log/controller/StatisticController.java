package com.shaolonger.monitorplatform.log.controller;

import com.shaolonger.monitorplatform.log.service.StatisticService;
import com.shaolonger.monitorplatform.utils.ResponseResultBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/statistic")
public class StatisticController {

    @Autowired
    StatisticService statisticService;

    /**
     * 获取总览统计信息
     *
     * @param request request
     * @return Object
     */
    @RequestMapping(value = "/getOverallByTimeRange", method = RequestMethod.GET)
    public Object getOverall(HttpServletRequest request) {
        try {
            return ResponseResultBase.getResponseResultBase(statisticService.getOverallByTimeRange(request));
        } catch (Exception e) {
            return ResponseResultBase.getErrorResponseResult(e);
        }
    }

    /**
     * 获取日志统计信息
     *
     * @param request request
     * @return Object
     */
    @RequestMapping(value = "/getLogCountByHours", method = RequestMethod.GET)
    public Object getLogCountByHours(HttpServletRequest request) {
        try {
            return ResponseResultBase.getResponseResultBase(statisticService.getLogCountByHours(request));
        } catch (Exception e) {
            return ResponseResultBase.getErrorResponseResult(e);
        }
    }
}
