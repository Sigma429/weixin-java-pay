package com.github.Sigma429.service.impl;

import com.github.Sigma429.service.WXPayService;
import com.github.Sigma429.util.WeiXinNotifyUtils;
import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyV3Result;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import jakarta.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;


/**
 * ClassName:WXPayServiceImpl
 * Package:com.github.Sigma429.service.impl
 * Description:
 * 微信支付接口实现类
 * @Author:14亿少女的梦-Sigma429
 * @Create:2023/10/17 - 14:47
 * @Version:v1.0
 */
public class WXPayServiceImpl extends com.github.binarywang.wxpay.service.impl.WxPayServiceImpl implements WXPayService {
    @Override
    public <T> T createOrderV3(WxPayUnifiedOrderV3Request request) throws WxPayException {
        return super.createOrderV3(TradeTypeEnum.NATIVE, request);
    }

    @Override
    public WxPayOrderQueryV3Result queryOrderV3(String transactionId, String outTradeNo) throws WxPayException {
        return super.queryOrderV3(transactionId, outTradeNo);
    }

    @Override
    public WxPayOrderNotifyV3Result parseOrderNotifyV3Result(HttpServletRequest request, String notifyData) throws WxPayException {
        // 获取请求头信息
        SignatureHeader signatureHeader = WeiXinNotifyUtils.getSignatureHeader(request);
        return super.parseOrderNotifyV3Result(notifyData, signatureHeader);
    }

    @Override
    public void setConfig(WxPayConfig config) {
        super.setConfig(config);
    }

    @Override
    public void printQRCodeToLocal(String codeUrl, StringBuilder path) throws IOException, WriterException {
        // 测试把二维码输出到本地,正式环境通过流给前端
        HashMap<EncodeHintType, String> map = new HashMap<>();
        map.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix bitMatrix;
        bitMatrix = new MultiFormatWriter().encode(codeUrl, BarcodeFormat.QR_CODE, 250, 250, map);
        Path file = new File(path.toString()).toPath();
        MatrixToImageWriter.writeToPath(bitMatrix, "jpg", file);
    }

}
