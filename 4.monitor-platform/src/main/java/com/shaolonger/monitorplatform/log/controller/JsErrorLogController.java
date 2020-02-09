package com.shaolonger.monitorplatform.log.controller;

import com.shaolonger.monitorplatform.log.entity.JsErrorLog;
import com.shaolonger.monitorplatform.log.service.JsErrorLogService;
import com.shaolonger.monitorplatform.utils.ResponseResultBase;
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
     * @param request
     * @return
     */
    @RequestMapping(value = "/findByQueries", method = RequestMethod.GET)
    public Object findByQueries(HttpServletRequest request) {
        return ResponseResultBase.getResponseResultBase(jsErrorLogService.findByQueries(request));
    }

    /**
     * 新增
     * @param jsErrorLog
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object add(@Valid JsErrorLog jsErrorLog, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuffer stringBuffer = new StringBuffer();
            for (ObjectError objectError : bindingResult.getAllErrors()) {
                stringBuffer.append(objectError.getDefaultMessage() + ",");
            }
            throw new ValidationException(stringBuffer.deleteCharAt(stringBuffer.length() - 1).toString());
        } else {
            return ResponseResultBase.getResponseResultBase(jsErrorLogService.add(jsErrorLog));
        }
    }
}
