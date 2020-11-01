package com.shaolonger.webmonitorserver.log.controller;

import com.shaolonger.webmonitorserver.common.api.ResponseResultBase;
import com.shaolonger.webmonitorserver.log.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    LogService logService;

    /**
     * 高级查询-多条件
     * @param request request
     * @return Object
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Object list(HttpServletRequest request) {
        try {
            return ResponseResultBase.getResponseResultBase(logService.getLogListByConditions(request));
        } catch (Exception e) {
            return ResponseResultBase.getErrorResponseResult(e);
        }
    }
}
