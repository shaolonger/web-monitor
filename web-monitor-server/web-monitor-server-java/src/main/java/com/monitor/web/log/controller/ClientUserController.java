package com.monitor.web.log.controller;

import com.monitor.web.auth.annotation.AuthIgnore;
import com.monitor.web.common.api.ResponseResultBase;
import com.monitor.web.log.dto.ClientUserDTO;
import com.monitor.web.log.service.ClientUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.ValidationException;

@RestController
public class ClientUserController {

    @Autowired
    ClientUserService clientUserService;

    /**
     * 新增
     *
     * @param clientUserDTO clientUserDTO
     * @param bindingResult bindingResult
     * @return Object
     */
    @AuthIgnore
    @RequestMapping(value = "/log/client/add", method = RequestMethod.GET)
    public Object add(@Valid ClientUserDTO clientUserDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ObjectError objectError : bindingResult.getAllErrors()) {
                stringBuilder.append(objectError.getDefaultMessage()).append(",");
            }
            throw new ValidationException(stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString());
        } else {
            return ResponseResultBase.getResponseResultBase(clientUserService.add(clientUserDTO));
        }
    }
}
