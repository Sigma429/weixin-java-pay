package com.github.Sigma429.config;

import java.lang.annotation.*;

/**
 * ClassName:NativeNotify
 * Package:com.github.Sigma429.config
 * Description:
 * 微信Native支付回调自定义注解
 * @Author:14亿少女的梦-Sigma429
 * @Create:2023/10/19 - 16:11
 * @Version:v1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
@Documented
public @interface NativeNotify {
}
