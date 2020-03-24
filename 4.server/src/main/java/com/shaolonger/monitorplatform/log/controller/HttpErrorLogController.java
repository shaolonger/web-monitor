package com.shaolonger.monitorplatform.log.controller;

import com.shaolonger.monitorplatform.log.entity.HttpErrorLog;
import com.shaolonger.monitorplatform.log.service.HttpErrorLogService;
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
@RequestMapping("/httpErrorLog")
public class HttpErrorLogController {
    @Autowired
    private HttpErrorLogService httpErrorLogService;

    /**
     * 多条件查询
     *
     * @param request request
     * @return Object
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Object get(HttpServletRequest request) {
        return ResponseResultBase.getResponseResultBase(httpErrorLogService.get(request));
    }

    /**
     * 新增
     *
     * @param httpErrorLog  httpErrorLog
     * @param bindingResult bindingResult
     * @return Object
     */
    @RequestMapping(value = "/add", method = RequestMethod.PUT)
    public Object add(@Valid HttpErrorLog httpErrorLog, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ObjectError objectError : bindingResult.getAllErrors()) {
                stringBuilder.append(objectError.getDefaultMessage()).append(",");
            }
            throw new ValidationException(stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString());
        } else {
            return ResponseResultBase.getResponseResultBase(httpErrorLogService.add(httpErrorLog));
        }
    }

    /**
     * 按status分类获取日志数量
     *
     * @param request request
     * @return Object
     */
    @RequestMapping(value = "/getLogCountByState", method = RequestMethod.GET)
    public Object getLogCountByState(HttpServletRequest request) {
        try {
            return ResponseResultBase.getResponseResultBase(httpErrorLogService.getLogCountByState(request));
        } catch (Exception e) {
            return ResponseResultBase.getErrorResponseResult(e);
        }
    }
}
