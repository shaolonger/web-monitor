package com.monitor.web.log.controller;

import com.monitor.web.auth.annotation.AuthIgnore;
import com.monitor.web.log.entity.HttpErrorLogEntity;
import com.monitor.web.log.service.HttpErrorLogService;
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
public class HttpErrorLogController {
    @Autowired
    private HttpErrorLogService httpErrorLogService;

    /**
     * 多条件查询
     *
     * @param request request
     * @return Object
     */
    @RequestMapping(value = "/httpErrorLog/get", method = RequestMethod.GET)
    public Object get(HttpServletRequest request) {
        return ResponseResultBase.getResponseResultBase(httpErrorLogService.get(request));
    }

    /**
     * 多条件聚合查询
     *
     * @param request request
     * @return Object
     */
    @RequestMapping(value = "/httpErrorLog/getByGroup", method = RequestMethod.GET)
    public Object getByGroup(HttpServletRequest request) {
        return ResponseResultBase.getResponseResultBase(httpErrorLogService.getByGroup(request));
    }

    /**
     * 新增
     *
     * @param httpErrorLogEntity  httpErrorLog
     * @param bindingResult bindingResult
     * @return Object
     */
    @AuthIgnore
    @RequestMapping(value = "/httpErrorLog/add", method = RequestMethod.PUT)
    public Object add(@Valid HttpErrorLogEntity httpErrorLogEntity, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ObjectError objectError : bindingResult.getAllErrors()) {
                stringBuilder.append(objectError.getDefaultMessage()).append(",");
            }
            throw new ValidationException(stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString());
        } else {
            return ResponseResultBase.getResponseResultBase(httpErrorLogService.add(httpErrorLogEntity));
        }
    }

    /**
     * 按status分类获取日志数量
     *
     * @param request request
     * @return Object
     */
    @RequestMapping(value = "/httpErrorLog/getLogCountByState", method = RequestMethod.GET)
    public Object getLogCountByState(HttpServletRequest request) {
        try {
            return ResponseResultBase.getResponseResultBase(httpErrorLogService.getLogCountByState(request));
        } catch (Exception e) {
            return ResponseResultBase.getErrorResponseResult(e);
        }
    }
}
