package com.github.Sigma429.config;


import com.github.Sigma429.properties.WxPayProperties;
import com.github.Sigma429.service.WxPayService;
import com.github.Sigma429.service.impl.WxPayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName:WxPayConfig
 * Package:com.github.Sigma429.config
 * Description:
 * 微信支付自动配置
 * @Author:14亿少女的梦-Sigma429
 * @Create:2023/10/17 - 18:07
 * @Version:v1.0
 */
@Configuration
@ConditionalOnClass(WxPayService.class)
@EnableConfigurationProperties(WxPayProperties.class)
public class WxPayConfig {
    @Autowired
    private WxPayProperties properties;

    /**
     * 构造微信支付服务对象
     * @return 微信支付接口
     */
    @Bean
    @ConditionalOnMissingBean(WxPayService.class)
    public WxPayService wxPayService() {
        com.github.binarywang.wxpay.config.WxPayConfig payConfig = new com.github.binarywang.wxpay.config.WxPayConfig();
        payConfig.setMchId(properties.getMchId());
        payConfig.setAppId(properties.getAppId());
        payConfig.setCertSerialNo(properties.getCertSerialNo());
        payConfig.setPrivateKeyPath(properties.getPrivateKeyPath());
        payConfig.setPrivateCertPath(properties.getPrivateCertPath());
        payConfig.setApiV3Key(properties.getApiV3Key());
        payConfig.setNotifyUrl(properties.getNotifyUrl());
        WxPayServiceImpl wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }
}