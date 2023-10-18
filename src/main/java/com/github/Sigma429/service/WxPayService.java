package com.github.Sigma429.service;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyV3Result;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.google.zxing.WriterException;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

/**
 * ClassName:WxPayService
 * Package:com.github.Sigma429.service
 * Description:
 * 微信支付接口
 * @Author:14亿少女的梦-苏信玮
 * @Create:2023/10/17 - 14:49
 * @Version:v1.0
 */
public interface WxPayService {
    /**
     * 调用统一下单接口，并组装生成支付所需参数对象.
     * @param <T>     请使用{@link com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result}里的内部类或字段
     * @param request 统一下单请求参数
     * @return 返回 {@link com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result}里的内部类或字段
     * @throws WxPayException the wx pay exception
     */
    <T> T createOrderV3(WxPayUnifiedOrderV3Request request) throws WxPayException;

    /**
     * <pre>
     * 查询订单
     * 详见 <a href="https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_1_2.shtml">https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_1_2.shtml</a>
     * 商户可以通过查询订单接口主动查询订单状态，完成下一步的业务逻辑。查询订单状态可通过微信支付订单号或商户订单号两种方式查询
     * 注意：
     *  查询订单可通过微信支付订单号和商户订单号两种方式查询，两种查询方式返回结果相同
     * 需要调用查询接口的情况：
     * ◆ 当商户后台、网络、服务器等出现异常，商户系统最终未接收到支付通知。
     * ◆ 调用支付接口后，返回系统错误或未知交易状态情况。
     * ◆ 调用付款码支付API，返回USERPAYING的状态。
     * ◆ 调用关单或撤销接口API之前，需确认支付状态。
     * 接口地址：
     *  https://api.mch.weixin.qq.com/v3/pay/transactions/id/{transaction_id}
     *  https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/{out_trade_no}
     * </pre>
     * @param transactionId 微信订单号
     * @param outTradeNo    商户系统内部的订单号，当没提供transactionId时需要传这个。
     * @return the wx pay order query result
     * @throws WxPayException the wx pay exception
     */
    WxPayOrderQueryV3Result queryOrderV3(String transactionId, String outTradeNo) throws WxPayException;

    /**
     * 解析支付结果v3通知.
     * 详见https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_1_5.shtml
     * @param request    统一下单请求参数
     * @param notifyData 通知数据
     * @return the wx pay order notify result
     * @throws WxPayException the wx pay exception
     */
    WxPayOrderNotifyV3Result parseOrderNotifyV3Result(HttpServletRequest request, String notifyData) throws WxPayException;

    /**
     * 设置配置对象.
     * @param config the config
     */
    void setConfig(WxPayConfig config);

    /**
     * 测试把二维码输出到本地,正式环境通过流给前端
     * @param codeUrl 二维码地址
     * @param path    路径
     * @throws IOException io异常
     */
    void printQRCodeToLocal(String codeUrl, StringBuilder path) throws IOException, WriterException;

}
