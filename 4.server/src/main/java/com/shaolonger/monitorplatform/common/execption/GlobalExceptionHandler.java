package com.shaolonger.monitorplatform.common.execption;

import com.shaolonger.monitorplatform.common.api.ResponseResultBase;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理器
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = ApiException.class)
    public Object handle(ApiException e) {
        return ResponseResultBase.getErrorResponseResult(e);
    }
}
