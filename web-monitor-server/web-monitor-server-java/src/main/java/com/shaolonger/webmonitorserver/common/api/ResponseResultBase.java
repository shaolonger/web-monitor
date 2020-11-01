package com.shaolonger.webmonitorserver.common.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseResultBase {
    private Boolean success = true;

    private Object data;

    private String msg;

    public ResponseResultBase() {}

    public ResponseResultBase(Object data) {
        this.data = data;
    }

    public static Object getResponseResultBase(Object data) {
        return new ResponseResultBase(data);
    }

    public static Object getErrorResponseResult(Exception e) {
        ResponseResultBase responseResultBase = new ResponseResultBase();
        responseResultBase.setSuccess(false);
        responseResultBase.setMsg(e.getMessage());
        return responseResultBase;
    }
}
