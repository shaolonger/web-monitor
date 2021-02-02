package com.monitor.web.user.controller;

import com.monitor.web.auth.annotation.AuthIgnore;
import com.monitor.web.user.service.UserService;
import com.monitor.web.common.api.ResponseResultBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 多条件分页查询
     *
     * @param request request
     * @return Object
     */
    @RequestMapping(value = "/user/get", method = RequestMethod.GET)
    public Object get(HttpServletRequest request) {
        try {
            return ResponseResultBase.getResponseResultBase(userService.get(request));
        } catch (Exception e) {
            return ResponseResultBase.getErrorResponseResult(e);
        }
    }

    /**
     * 登录
     *
     * @param request request
     * @return Object
     */
    @AuthIgnore
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public Object login(HttpServletRequest request) {
        try {
            return ResponseResultBase.getResponseResultBase(userService.login(request));
        } catch (Exception e) {
            return ResponseResultBase.getErrorResponseResult(e);
        }
    }

    /**
     * 根据用户获取关联的项目
     *
     * @param request request
     * @return Object
     */
    @RequestMapping(value = "/user/getRelatedProjectList")
    public Object getRelatedProjectList(HttpServletRequest request) {
        return ResponseResultBase.getResponseResultBase(userService.getRelatedProjectListByRequest(request));
    }
}
