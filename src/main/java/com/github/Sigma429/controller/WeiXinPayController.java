package com.github.Sigma429.controller;

import com.github.Sigma429.pojo.entity.Result;
import com.github.Sigma429.service.WxPayService;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyV3Result;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * ClassName:WeiXinPayController
 * Package:com.github.Sigma429.controller
 * Description:
 * 微信支付模块
 * @Author:14亿少女的梦-Sigma429
 * @Create:2023/08/23 - 14:47
 * @Version:v1.0
 */
@RestController
@RequestMapping("/weixin/pay")
@Slf4j
public class WeiXinPayController {
    @Autowired
    private WxPayService wxPayService;

    /**
     * Native下单（即创建支付二维码）
     * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_4_1.shtml
     * 当订单创建完成后，后端调用Native下单接口获取支付二维码地址
     * @param platFormNumber 商户订单号
     * @param totalPrice     总金额
     * @return 结果
     * @throws Exception 异常
     */
    @GetMapping("/create/{platformnumber}/{totalprice}")
    public Result createOrder(@PathVariable("platformnumber") String platFormNumber,
                              @PathVariable("totalprice") BigDecimal totalPrice) throws Exception {
        // TODO 设置传入的参数(以下三种为必传参数)
        // 构建请求对象，appId、mchid、notify_url会在执行时中自动添加，无需手动设置
        WxPayUnifiedOrderV3Request request = new WxPayUnifiedOrderV3Request();
        // 商品描述
        request.setDescription(platFormNumber);
        // 商户订单号
        request.setOutTradeNo(platFormNumber);
        // 总金额（单位为分，只能为整数，即元*100）
        WxPayUnifiedOrderV3Request.Amount amount = new WxPayUnifiedOrderV3Request.Amount();
        amount.setTotal(totalPrice.multiply(new BigDecimal("100")).intValue());
        request.setAmount(amount);

        // 调用接口，并接收返回的支付二维码地址
        String codeUrl = wxPayService.createOrderV3(request);

        // 测试把二维码输出到本地,正式环境通过流给前端
        // wxPayService.printQRCodeToLocal(codeUrl, new StringBuilder("C:\\Users\\10597\\Desktop\\demo\\demo.jpg"));

        return Result.success(codeUrl);
    }

    /**
     * 查询订单（即查询支付状态）
     * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_4_2.shtml
     * 当前端展示二维码之后，定时任务向后端查询订单支付状态，后端向微信端查询订单状态，如果支付完成，则更新数据库并返回前端支付成功
     * @param platFormNumber 商户订单号
     * @return 结果
     * @throws Exception 异常
     */
    @GetMapping("/query/{platformnumber}")
    public Result queryOrder(@PathVariable("platformnumber") String platFormNumber) throws Exception {
        // 传入 微信支付系统生成的订单号 或 商户订单号 ，此处使用商户订单号查询，mchid会在执行时中自动添加，无需手动设置
        WxPayOrderQueryV3Result result = wxPayService.queryOrderV3(null, platFormNumber);
        // 获取查询结果
        String tradeState = result.getTradeState();
        // TODO 根据查询结果处理逻辑
        if (tradeState.equals("SUCCESS")) {

        }
        return Result.success(tradeState);
    }

    /**
     * 支付通知（必须）（即用户支付完成后，微信端的回调接口）
     * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_4_5.shtml
     * 当收到支付时，修改本地订单为支付成功状态，并返回给微信处理成功的消息
     * @param request    统一下单请求参数
     * @param notifyData 通知数据
     * @return 结果
     * @throws Exception 异常
     */
    @PostMapping("/notify")
    public Result nativeNotify(HttpServletRequest request,
                               @RequestBody String notifyData) throws Exception {
        // 解析支付结果通知
        WxPayOrderNotifyV3Result result = wxPayService.parseOrderNotifyV3Result(request, notifyData);
        // TODO 业务逻辑

        return Result.success();
    }
}