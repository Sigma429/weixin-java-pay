package com.github.Sigma429.annotation;

import com.github.Sigma429.WxPayAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * ClassName:EnableWxPay
 * Package:com.github.Sigma429.annotation
 * Description:
 * @Author:14亿少女的梦-Sigma429
 * @Create:2023/11/12 - 17:55
 * @Version:v1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({WxPayAutoConfiguration.class})
public @interface EnableWxPay {
    
}
