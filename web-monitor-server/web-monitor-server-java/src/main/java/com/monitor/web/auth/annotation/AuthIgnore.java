package com.monitor.web.auth.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解，用于免token权限校验的处理
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthIgnore {
}
