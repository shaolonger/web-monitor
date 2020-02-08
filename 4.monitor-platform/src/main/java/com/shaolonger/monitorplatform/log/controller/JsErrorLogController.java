package com.shaolonger.monitorplatform.log.controller;

import com.shaolonger.monitorplatform.log.service.JsErrorLogService;
import com.shaolonger.monitorplatform.utils.ResponseResultBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/jsErrorLog")
public class JsErrorLogController {
    @Autowired
    private JsErrorLogService jsErrorLogService;

    @RequestMapping(value = "/findByQueries", method = RequestMethod.GET)
    public Object findByQueries(HttpServletRequest request) throws Exception {
        return ResponseResultBase.getResponseResultBase(jsErrorLogService.findByQueries(request));
    }
}
