package com.monitor.web.alarm.controller;

import com.monitor.web.alarm.service.AlarmRecordService;
import com.monitor.web.common.api.ResponseResultBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AlarmRecordController {

    @Autowired
    AlarmRecordService alarmRecordService;

    /**
     * 查询
     *
     * @param request request
     * @return Object
     */
    @RequestMapping(value = "/alarmRecord/get", method = RequestMethod.GET)
    public Object get(HttpServletRequest request) {
        try {
            return ResponseResultBase.getResponseResultBase(alarmRecordService.get(request));
        } catch (Exception e) {
            return ResponseResultBase.getErrorResponseResult(e);
        }
    }
}
