package com.monitor.web.log.controller;

import com.monitor.web.auth.annotation.AuthIgnore;
import com.monitor.web.log.entity.ResourceLoadErrorLogEntity;
import com.monitor.web.log.service.ResourceLoadErrorLogService;
import com.monitor.web.common.api.ResponseResultBase;
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
public class ResourceLoadErrorLogController {
    @Autowired
    private ResourceLoadErrorLogService resourceLoadErrorLogService;

    /**
     * 多条件查询
     *
     * @param request request
     * @return Object
     */
    @RequestMapping(value = "/resourceLoadErrorLog/get", method = RequestMethod.GET)
    public Object get(HttpServletRequest request) {
        return ResponseResultBase.getResponseResultBase(resourceLoadErrorLogService.get(request));
    }

    /**
     * 多条件聚合查询
     *
     * @param request request
     * @return Object
     */
    @RequestMapping(value = "/resourceLoadErrorLog/getByGroup", method = RequestMethod.GET)
    public Object getByGroup(HttpServletRequest request) {
        return ResponseResultBase.getResponseResultBase(resourceLoadErrorLogService.getByGroup(request));
    }

    /**
     * 新增
     *
     * @param resourceLoadErrorLogEntity resourceLoadErrorLog
     * @param bindingResult        bindingResult
     * @return Object
     */
    @AuthIgnore
    @RequestMapping(value = "/resourceLoadErrorLog/add", method = RequestMethod.PUT)
    public Object add(@Valid ResourceLoadErrorLogEntity resourceLoadErrorLogEntity, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ObjectError objectError : bindingResult.getAllErrors()) {
                stringBuilder.append(objectError.getDefaultMessage()).append(",");
            }
            throw new ValidationException(stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString());
        } else {
            return ResponseResultBase.getResponseResultBase(resourceLoadErrorLogService.add(resourceLoadErrorLogEntity));
        }
    }

    /**
     * 获取总览统计信息
     *
     * @param request request
     * @return Object
     */
    @RequestMapping(value = "/resourceLoadErrorLog/getOverallByTimeRange", method = RequestMethod.GET)
    public Object getOverallByTimeRange(HttpServletRequest request) {
        try {
            return ResponseResultBase.getResponseResultBase(resourceLoadErrorLogService.getOverallByTimeRange(request));
        } catch (Exception e) {
            return ResponseResultBase.getErrorResponseResult(e);
        }
    }
}
