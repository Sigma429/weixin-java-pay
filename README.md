## 微信Native支付

只简单实现了Native下单接口、查询订单接口、回调接口以及自定义的输出二维码到本地接口

直接导入依赖配置好文件即可使用

### 配置文件

```java
// 商户公钥和私钥文件放在resources下并配置路径
// 回调地址加上回调接口的URL
```

```yaml
# 微信支付相关参数（以下数据是参考尚硅谷的）
wx:
  pay:
    # 商户号
    mch-id: 1558950191
    # 微信公众号或者小程序等的appid.
    app-id: wx74862e0dfcf69954
    # 证书序列号(选填)
#    cert-serial-no:
    # 商户私钥文件
    private-key-path: classpath:/file/apiclient_key.pem
    # 商户公钥文件
    private-cert-path: classpath:/file/apiclient_cert.pem
    # APIv3密钥
    api-v3-key: UDuLFDcmy5Eb6o0nTNZdu6ek4DDh4K8B
    # 接收结果通知地址
    notify-url: https://de06-211-93-248-135.ngrok-free.app/weixin/pay/notify
```

### DemoController

```java
package com.github.Sigma429.controller;

import com.github.Sigma429.pojo.entity.Result;
import com.github.Sigma429.service.WXPayService;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyV3Result;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

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
    private WXPayService wxPayService;

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
```

## 作者留言

本API为一大二学生所写，技术比较菜，只是简单运用AOP封装了下大佬（[binarywang (Binary Wang) (github.com)](https://github.com/binarywang)）的API

如有问题，欢迎各位友友指出意见

