package com.shaolonger.monitorplatform.log.controller;

import com.shaolonger.monitorplatform.auth.annotation.AuthIgnore;
import com.shaolonger.monitorplatform.log.entity.JsErrorLog;
import com.shaolonger.monitorplatform.log.service.JsErrorLogService;
import com.shaolonger.monitorplatform.common.api.ResponseResultBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.ValidationException;

@RestController
@RequestMapping("/jsErrorLog")
public class JsErrorLogController {
    @Autowired
    private JsErrorLogService jsErrorLogService;

    /**
     * 多条件查询
     *
     * @param request request
     * @return Object
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Object get(HttpServletRequest request) {
        return ResponseResultBase.getResponseResultBase(jsErrorLogService.get(request));
    }

    /**
     * 多条件聚合查询
     *
     * @param request request
     * @return Object
     */
    @RequestMapping(value = "/getByGroup", method = RequestMethod.GET)
    public Object getByGroup(HttpServletRequest request) {
        return ResponseResultBase.getResponseResultBase(jsErrorLogService.getByGroup(request));
    }

    /**
     * 新增
     *
     * @param jsErrorLog    jsErrorLog
     * @param bindingResult bindingResult
     * @return Object
     */
    @AuthIgnore
    @RequestMapping(value = "/add", method = RequestMethod.PUT)
    public Object add(@Valid JsErrorLog jsErrorLog, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ObjectError objectError : bindingResult.getAllErrors()) {
                stringBuilder.append(objectError.getDefaultMessage()).append(",");
            }
            throw new ValidationException(stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString());
        } else {
            return ResponseResultBase.getResponseResultBase(jsErrorLogService.add(jsErrorLog));
        }
    }
}
