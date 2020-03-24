package com.shaolonger.monitorplatform.utils;

import java.util.UUID;

public class TokenUtils {

    /**
     * 获取token
     *
     * @return String
     */
    public static String getToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
