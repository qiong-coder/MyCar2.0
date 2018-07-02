package org.buaa.ly.MyCar.action;


import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayNativeOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping(value = "/pay/cases")
@Slf4j
public class WxPayCaseTest {


    @Autowired WxPayConfig config;

    @Autowired WxPayService service;

    @PostConstruct
    public void init() throws WxPayException {
        String sandboxKey = service.getSandboxSignKey();
        config.setMchKey(sandboxKey);
        config.setUseSandboxEnv(true);
        //service = new WxPayServiceImpl();
        service.setConfig(config);
    }

    @RequestMapping(value = "/notify/{type}/", method = RequestMethod.POST)
    public String notify(@PathVariable String type,
                         @RequestBody String xmlData) throws WxPayException {
        if ( type.compareTo("pay") == 0 ) {
            WxPayOrderNotifyResult result = service.parseOrderNotifyResult(xmlData);
            log.info("pay:{}",result.toString());
            return WxPayNotifyResponse.success("OK");
        } else if ( type.compareTo("refund") == 0 ) {
            WxPayRefundNotifyResult result = service.parseRefundNotifyResult(xmlData);
            log.info("refund:{}", result.toString());
            return WxPayNotifyResponse.success("OK");
        }
        return WxPayNotifyResponse.fail("");
    }

    @RequestMapping(value = "/1", method = RequestMethod.GET)
    public String case1() throws WxPayException {
        WxPayUnifiedOrderRequest request = WxPayUnifiedOrderRequest.newBuilder()
                .body("【扫码-正常】订单金额3.01元")
                .outTradeNo("1409811653")
                .totalFee(301)
                .spbillCreateIp("182.92.157.225")
                .notifyUrl("http://182.92.157.225:13080/MyCar/pay/cases/notify/pay/")
                .productId("case1")
                .tradeType(WxPayConstants.TradeType.NATIVE)
                .build();

        WxPayNativeOrderResult result = service.createOrder(request);

        return result.getCodeUrl();
    }

    @RequestMapping(value = "/2", method = RequestMethod.GET)
    public String case2() throws WxPayException {
        WxPayUnifiedOrderRequest request = WxPayUnifiedOrderRequest.newBuilder()
                .body("【扫码-正常】订单金额3.02元")
                .outTradeNo("1409811653")
                .totalFee(302)
                .spbillCreateIp("182.92.157.225")
                .notifyUrl("http://182.92.157.225:13080/MyCar/pay/cases/notify/pay/")
                .productId("case2")
                .tradeType(WxPayConstants.TradeType.NATIVE)
                .build();

        WxPayNativeOrderResult result = service.createOrder(request);

        return result.getCodeUrl();
    }

}
