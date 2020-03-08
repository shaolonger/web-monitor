package com.shaolonger.monitorplatform.project.controller;

import com.shaolonger.monitorplatform.project.entity.ProjectEntity;
import com.shaolonger.monitorplatform.project.service.ProjectService;
import com.shaolonger.monitorplatform.utils.ResponseResultBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.ValidationException;

@RestController
@RequestMapping("project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @RequestMapping(value = "/add", method = RequestMethod.PUT)
    public Object add(HttpServletRequest request, @Valid ProjectEntity projectEntity, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ObjectError objectError : bindingResult.getAllErrors()) {
                stringBuilder.append(objectError.getDefaultMessage()).append(",");
            }
            throw new ValidationException(stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString());
        } else {
            try {
                return ResponseResultBase.getResponseResultBase(projectService.add(request, projectEntity));
            } catch (Exception e) {
                return ResponseResultBase.getErrorResponseResult(e);
            }
        }
    }

    /**
     * 查询
     *
     * @param request request
     * @return Object
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Object get(HttpServletRequest request) {
        return ResponseResultBase.getResponseResultBase(projectService.get(request));
    }

    /**
     * 删除
     *
     * @return Object
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public Object delete(@PathVariable("id") Long id) {
        try {
            return ResponseResultBase.getResponseResultBase(projectService.delete(id));
        } catch (Exception e) {
            return ResponseResultBase.getErrorResponseResult(e);
        }
    }
}
