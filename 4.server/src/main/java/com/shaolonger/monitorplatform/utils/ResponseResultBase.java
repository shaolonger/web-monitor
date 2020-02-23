package com.shaolonger.monitorplatform.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseResultBase {
    private Boolean success = true;

    private Object data;

    private String msg;

    public ResponseResultBase(Object data) {
        this.data = data;
    }

    public static Object getResponseResultBase(Object data) {
        return new ResponseResultBase(data);
    }
}
