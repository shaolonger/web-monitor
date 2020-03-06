package com.shaolonger.monitorplatform.user.controller;

import com.shaolonger.monitorplatform.user.service.UserService;
import com.shaolonger.monitorplatform.utils.ResponseResultBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Object login(HttpServletRequest request) {
        try {
            return ResponseResultBase.getResponseResultBase(userService.login(request));
        } catch (Exception e) {
            return ResponseResultBase.getErrorResponseResult(e);
        }
    }
}
