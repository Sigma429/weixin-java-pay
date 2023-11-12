package com.github.Sigma429;

import com.github.Sigma429.aspect.WxPayAspect;
import com.github.Sigma429.config.NativeNotify;
import com.github.Sigma429.config.WxPayConfig;
import com.github.Sigma429.config.WxPayProperties;
import com.github.Sigma429.pojo.entity.Result;
import com.github.Sigma429.service.WXPayService;
import com.github.Sigma429.service.impl.WXPayServiceImpl;
import com.github.Sigma429.util.WeiXinNotifyUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * ClassName:WxPayAutoConfiguration
 * Package:com.github.Sigma429
 * Description:
 * @Author:14亿少女的梦-Sigma429
 * @Create:2023/11/12 - 15:56
 * @Version:v1.0
 */
@Import({WxPayAspect.class, NativeNotify.class, WxPayConfig.class, WxPayProperties.class, Result.class,
        WeiXinNotifyUtils.class, WXPayService.class, WXPayServiceImpl.class})
@Configuration
public class WxPayAutoConfiguration {
}
