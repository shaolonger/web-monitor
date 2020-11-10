package com.monitor.web.log.controller;

import com.monitor.web.auth.annotation.AuthIgnore;
import com.monitor.web.log.service.LogService;
import com.monitor.web.common.api.ResponseResultBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LogController {

    @Autowired
    LogService logService;

    /**
     * 打点日志上传
     *
     * @param request request
     * @return Object
     */
    @AuthIgnore
    @RequestMapping(value = "/log/add", method = RequestMethod.GET)
    public Object add(HttpServletRequest request) {
        try {
            return ResponseResultBase.getResponseResultBase(logService.add(request));
        } catch (Exception e) {
            return ResponseResultBase.getErrorResponseResult(e);
        }
    }

    /**
     * 高级查询-多条件
     *
     * @param request request
     * @return Object
     */
    @RequestMapping(value = "/log/list", method = RequestMethod.POST)
    public Object list(HttpServletRequest request) {
        try {
            return ResponseResultBase.getResponseResultBase(logService.getLogListByConditions(request));
        } catch (Exception e) {
            return ResponseResultBase.getErrorResponseResult(e);
        }
    }
}
