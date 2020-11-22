package com.monitor.web.alarm.controller;

import com.monitor.web.alarm.service.SubscriberNotifyRecordService;
import com.monitor.web.common.api.ResponseResultBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class SubscriberNotifyRecordController {

    @Autowired
    SubscriberNotifyRecordService subscriberNotifyRecordService;

    /**
     * 查询
     *
     * @param request request
     * @return Object
     */
    @RequestMapping(value = "/subscriberNotifyRecord/get", method = RequestMethod.GET)
    public Object get(HttpServletRequest request) {
        return ResponseResultBase.getResponseResultBase(subscriberNotifyRecordService.get(request));
    }

    /**
     * 查询-带关联信息（预警名称）
     *
     * @param request request
     * @return Object
     */
    @RequestMapping(value = "/subscriberNotifyRecord/getWithRelatedInfo", method = RequestMethod.GET)
    public Object getWithRelatedInfo(HttpServletRequest request) {
        return ResponseResultBase.getResponseResultBase(subscriberNotifyRecordService.getWithRelatedInfo(request));
    }
}
