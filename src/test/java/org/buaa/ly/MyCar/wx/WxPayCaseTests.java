package org.buaa.ly.MyCar.wx;

import com.github.binarywang.wxpay.bean.order.WxPayNativeOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.config.TestLoader;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class WxPayCaseTests extends TestLoader {


    @Autowired WxPayService wxPayService;


    @Test
    public void test1() throws WxPayException {

        WxPayUnifiedOrderRequest request = WxPayUnifiedOrderRequest.newBuilder()
                .body(String.format("用例1【扫码-正常】订单金额3.01元，用户支付成功"))
                .outTradeNo("1")
                .totalFee(301)
                .spbillCreateIp("172.168.1.1")
                .notifyUrl("http://mycarzc.com/pay/weixin/nofity/pay/")
                .productId("test")
                .tradeType(WxPayConstants.TradeType.NATIVE).build();


        WxPayNativeOrderResult result = wxPayService.createOrder(request);

        assert(result.getCodeUrl() != null);

    }

}
