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
    @RequestMapping(value = "/getOverall", method = RequestMethod.GET)
    public Object getOverall(HttpServletRequest request) {
        try {
            return ResponseResultBase.getResponseResultBase(statisticService.getOverall(request));
        } catch (Exception e) {
            return ResponseResultBase.getErrorResponseResult(e);
        }
    }
}
