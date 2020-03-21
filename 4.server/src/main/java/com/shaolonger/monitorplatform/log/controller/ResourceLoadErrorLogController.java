package com.shaolonger.monitorplatform.log.controller;

import com.shaolonger.monitorplatform.log.entity.ResourceLoadErrorLog;
import com.shaolonger.monitorplatform.log.service.ResourceLoadErrorLogService;
import com.shaolonger.monitorplatform.utils.ResponseResultBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sun.rmi.transport.ObjectTable;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.ValidationException;

@RestController
@RequestMapping("/resourceLoadErrorLog")
public class ResourceLoadErrorLogController {
    @Autowired
    private ResourceLoadErrorLogService resourceLoadErrorLogService;

    /**
     * 多条件查询
     *
     * @param request request
     * @return Object
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Object get(HttpServletRequest request) {
        return ResponseResultBase.getResponseResultBase(resourceLoadErrorLogService.get(request));
    }

    /**
     * 新增
     *
     * @param resourceLoadErrorLog  resourceLoadErrorLog
     * @param bindingResult bindingResult
     * @return Object
     */
    @RequestMapping(value = "/add", method = RequestMethod.PUT)
    public Object add(@Valid ResourceLoadErrorLog resourceLoadErrorLog, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ObjectError objectError : bindingResult.getAllErrors()) {
                stringBuilder.append(objectError.getDefaultMessage()).append(",");
            }
            throw new ValidationException(stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString());
        } else {
            return ResponseResultBase.getResponseResultBase(resourceLoadErrorLogService.add(resourceLoadErrorLog));
        }
    }

    /**
     * 获取总览统计信息
     *
     * @param request request
     * @return Object
     */
    @RequestMapping(value = "/getOverallByTimeRange", method = RequestMethod.GET)
    public Object getOverallByTimeRange(HttpServletRequest request) {
        try {
            return ResponseResultBase.getResponseResultBase(resourceLoadErrorLogService.getOverallByTimeRange(request));
        } catch (Exception e) {
            return ResponseResultBase.getErrorResponseResult(e);
        }
    }
}
