package com.monitor.web.common.execption;

import com.monitor.web.common.api.ResponseResultBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理器
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = ApiException.class)
    public Object handleApiException(ApiException e) {
        log.error(e.getMessage(), e);
        return ResponseResultBase.getErrorResponseResult(e);
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Object handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseResultBase.getErrorResponseResult(e);
    }
}
