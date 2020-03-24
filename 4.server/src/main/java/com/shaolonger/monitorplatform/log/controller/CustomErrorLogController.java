package com.shaolonger.monitorplatform.log.controller;

import com.shaolonger.monitorplatform.log.entity.CustomErrorLog;
import com.shaolonger.monitorplatform.log.service.CustomErrorLogService;
import com.shaolonger.monitorplatform.common.api.ResponseResultBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.ValidationException;

@RestController
@RequestMapping("/customErrorLog")
public class CustomErrorLogController {
    @Autowired
    private CustomErrorLogService customErrorLogService;

    /**
     * 多条件查询
     *
     * @param request request
     * @return Object
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Object get(HttpServletRequest request) {
        return ResponseResultBase.getResponseResultBase(customErrorLogService.get(request));
    }

    /**
     * 新增
     *
     * @param customErrorLog customErrorLog
     * @param bindingResult  bindingResult
     * @return Object
     */
    @RequestMapping(value = "/add", method = RequestMethod.PUT)
    public Object add(@Valid CustomErrorLog customErrorLog, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ObjectError objectError : bindingResult.getAllErrors()) {
                stringBuilder.append(objectError.getDefaultMessage()).append(",");
            }
            throw new ValidationException(stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString());
        } else {
            return ResponseResultBase.getResponseResultBase(customErrorLogService.add(customErrorLog));
        }
    }
}
