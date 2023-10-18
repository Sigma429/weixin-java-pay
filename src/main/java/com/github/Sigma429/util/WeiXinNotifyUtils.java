package com.github.Sigma429.util;

import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import jakarta.servlet.http.HttpServletRequest;

/**
 * ClassName:WeixinNotifyUtils
 * Package:tyut.selab.a2.biomassmall.util
 * Description:
 * @Author:14亿少女的梦-Sigma429
 * @Create:2023/08/23 - 14:49
 * @Version:v1.0
 */
public class WeiXinNotifyUtils {
    /**
     * 获取请求头签名
     * @param request 统一下单请求参数
     * @return 请求头签名
     */
    public static SignatureHeader getSignatureHeader(HttpServletRequest request) {
        SignatureHeader signatureHeader = new SignatureHeader();
        signatureHeader.setSignature(request.getHeader("Wechatpay-Signature"));
        signatureHeader.setNonce(request.getHeader("Wechatpay-Nonce"));
        signatureHeader.setSerial(request.getHeader("Wechatpay-Serial"));
        signatureHeader.setTimeStamp(request.getHeader("Wechatpay-TimeStamp"));
        return signatureHeader;
    }
}
