package com.monitor.web.alarm.controller;

import com.monitor.web.alarm.entity.AlarmEntity;
import com.monitor.web.alarm.service.AlarmService;
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
public class AlarmController {

    @Autowired
    AlarmService alarmService;

    /**
     * 新增
     *
     * @param alarmEntity   alarmEntity
     * @param bindingResult bindingResult
     * @return Object
     */
    @RequestMapping(value = "/alarm/add", method = RequestMethod.PUT)
    public Object add(@Valid AlarmEntity alarmEntity, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ObjectError objectError : bindingResult.getAllErrors()) {
                stringBuilder.append(objectError.getDefaultMessage()).append(",");
            }
            throw new ValidationException(stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString());
        } else {
            return ResponseResultBase.getResponseResultBase(alarmService.add(alarmEntity));
        }
    }

    /**
     * 编辑
     *
     * @param request request
     * @return Object
     */
    @RequestMapping(value = "/alarm/update", method = RequestMethod.POST)
    public Object update(HttpServletRequest request) {
        try {
            return alarmService.update(request);
        } catch (Exception e) {
            return ResponseResultBase.getErrorResponseResult(e);
        }
    }

    /**
     * 查询
     *
     * @param request request
     * @return Object
     */
    @RequestMapping(value = "/alarm/get", method = RequestMethod.GET)
    public Object get(HttpServletRequest request) {
        return ResponseResultBase.getResponseResultBase(alarmService.get(request));
    }
}
