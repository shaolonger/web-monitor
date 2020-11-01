package com.shaolonger.webmonitorserver.common.execption;

import lombok.Getter;
import lombok.Setter;

/**
 * 自定义API异常
 */
@Getter
@Setter
public class ApiException extends RuntimeException {

    private String message;

    public ApiException(String message) {
        this.message = message;
    }
}
