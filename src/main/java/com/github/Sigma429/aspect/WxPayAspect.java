package com.github.Sigma429.aspect;


import com.github.Sigma429.pojo.entity.Result;
import com.github.binarywang.wxpay.bean.notify.OriginNotifyResponse;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.ReentrantLock;

/**
 * ClassName:WxPayAspect
 * Package:com.github.Sigma429.aspect
 * Description:
 * 切面类
 * @Author:14亿少女的梦-Sigma429
 * @Create:2023/10/17 - 18:07
 * @Version:v1.0
 */
@Aspect
@Slf4j
@Component
public class WxPayAspect {
    private final ReentrantLock orderLock = new ReentrantLock();

    @Pointcut("@annotation(com.github.Sigma429.config.NativeNotify)")
    public void nativeNotify() {

    }

    /**
     * 设置回调接口的AOP
     * @param joinPoint 切点
     * @return 结果
     */
    @Around(value = "nativeNotify()")
    public Object aroundMethod(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String notifyData = (String) args[1];

        Object result = null;
        try {
            log.info("======= 接收到通知 =========");
            // 将请求体json字符串转换为实体
            OriginNotifyResponse notifyResponse = new GsonBuilder().create().fromJson(notifyData,
                    OriginNotifyResponse.class);
            // 支付成功通知
            if ("TRANSACTION.SUCCESS".equals(notifyResponse.getEventType())) {
                // 获取锁
                if (orderLock.tryLock()) {
                    try {
                        result = joinPoint.proceed();
                        log.info("支付成功");
                    } catch (Exception e) {
                        // 支付结果解析异常或者订单处理异常
                        log.error("支付通知处理异常：", e);
                        // 支付成功通知处理失败时需要将状态码修改为5xx/4xx，微信才会重新发送回调
                        return Result.error("系统异常");
                    } finally {
                        // 释放锁
                        orderLock.unlock();
                    }
                } else {
                    // 锁获取失败，返回异常，等待下次消息
                    return Result.error(503, "系统繁忙");
                }
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return result;
    }
}
