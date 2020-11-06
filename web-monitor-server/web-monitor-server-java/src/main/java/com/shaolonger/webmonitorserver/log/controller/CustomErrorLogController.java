package com.shaolonger.webmonitorserver.log.controller;

import com.shaolonger.webmonitorserver.auth.annotation.AuthIgnore;
import com.shaolonger.webmonitorserver.log.entity.CustomErrorLogEntity;
import com.shaolonger.webmonitorserver.log.service.CustomErrorLogService;
import com.shaolonger.webmonitorserver.common.api.ResponseResultBase;
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
public class CustomErrorLogController {
    @Autowired
    private CustomErrorLogService customErrorLogService;

    /**
     * 多条件查询
     *
     * @param request request
     * @return Object
     */
    @RequestMapping(value = "/customErrorLog/get", method = RequestMethod.GET)
    public Object get(HttpServletRequest request) {
        return ResponseResultBase.getResponseResultBase(customErrorLogService.get(request));
    }

    /**
     * 多条件聚合查询
     *
     * @param request request
     * @return Object
     */
    @RequestMapping(value = "/customErrorLog/getByGroup", method = RequestMethod.GET)
    public Object getByGroup(HttpServletRequest request) {
        return ResponseResultBase.getResponseResultBase(customErrorLogService.getByGroup(request));
    }

    /**
     * 新增
     *
     * @param customErrorLogEntity customErrorLog
     * @param bindingResult  bindingResult
     * @return Object
     */
    @AuthIgnore
    @RequestMapping(value = "/customErrorLog/add", method = RequestMethod.PUT)
    public Object add(@Valid CustomErrorLogEntity customErrorLogEntity, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ObjectError objectError : bindingResult.getAllErrors()) {
                stringBuilder.append(objectError.getDefaultMessage()).append(",");
            }
            throw new ValidationException(stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString());
        } else {
            return ResponseResultBase.getResponseResultBase(customErrorLogService.add(customErrorLogEntity));
        }
    }
}
