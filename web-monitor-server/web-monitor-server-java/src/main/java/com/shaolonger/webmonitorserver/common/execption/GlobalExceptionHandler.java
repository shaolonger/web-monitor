package com.shaolonger.webmonitorserver.common.execption;

import com.shaolonger.webmonitorserver.common.api.ResponseResultBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理器
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ResponseBody
    @ExceptionHandler(value = ApiException.class)
    public Object handleApiException(ApiException e) {
        logger.error(e.getMessage(), e);
        return ResponseResultBase.getErrorResponseResult(e);
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Object handleException(Exception e) {
        logger.error(e.getMessage(), e);
        return ResponseResultBase.getErrorResponseResult(e);
    }
}
