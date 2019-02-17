package org.buaa.ly.MyCar.action;


import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayNativeOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayOrderQueryRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
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

//    @PostConstruct
//    public void init() throws WxPayException {
//        String sandboxKey = service.getSandboxSignKey();
//        config.setMchKey(sandboxKey);
//        config.setUseSandboxEnv(true);
//        //service = new WxPayServiceImpl();
//        service.setConfig(config);
//    }

    private void check(boolean b, String msg) {
        if (!b) log.info("failure {}", msg);
    }

    @RequestMapping(value = "/notify/{type}/{caseid}/", method = RequestMethod.POST)
    public String notify(@PathVariable String type,
                         @PathVariable int caseid,
                         @RequestBody String xmlData) throws WxPayException {

        if ( type.compareTo("pay") == 0 && caseid == -1 ) {
            WxPayOrderNotifyResult result = service.parseOrderNotifyResult(xmlData);
            log.info("pay:{}", result.toString());
            return WxPayNotifyResponse.success("OK");
        }

        if ( type.compareTo("pay") == 0 ) {
            WxPayOrderNotifyResult result;
            try {
                result = service.parseOrderNotifyResult(xmlData);
            } catch (WxPayException e) {
                log.info("catch the exception - {}:{}", caseid, e);
                if ( caseid == 6 ) return WxPayNotifyResponse.success("OK");
                else throw e;
            }
            log.info("pay:{}",result.toString());
            check(result.getResultCode().compareTo("SUCCESS") == 0,"result code is not [SUCCESS]");
            check(result.getReturnCode().compareTo("SUCCESS") == 0 , "return code is not [SUCCESS]");
            check(result.getFeeType().compareTo("CNY") == 0 , "fee type is not compare to [CNY]");
            if ( caseid == 1 ) {
                check(result.getTotalFee() == 301 , "total fee is not [301]");
            } else if ( caseid == 2 ) {
                check(result.getTotalFee() == 302, "total fee is not [302]");
                check(result.getCashFee() == 299 , "cash fee is not [299]");
                check(result.getCouponCount() == 2 , "coupon count is not [2]");
                check(result.getCouponFee() == 3 , "coupon fee is not [3]");
            } else if ( caseid == 5 ) {
                check(result.getTotalFee() == 332 , "total fee is not [332]");
            } else if ( caseid == 6 ) {
                check( false , "never go to there");
            } else if ( caseid == 7 ) {
                check(result.getTotalFee() == 20000 , "total fee is not [20000]");
            }
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
                .outTradeNo("case1")
                .totalFee(301)
                .spbillCreateIp("182.92.157.225")
                .notifyUrl("http://182.92.157.225:13080/MyCar/pay/cases/notify/pay/1/")
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
                .outTradeNo("case2")
                .totalFee(302)
                .spbillCreateIp("182.92.157.225")
                .notifyUrl("http://182.92.157.225:13080/MyCar/pay/cases/notify/pay/2/")
                .productId("case2")
                .tradeType(WxPayConstants.TradeType.NATIVE)
                .build();

        WxPayNativeOrderResult result = service.createOrder(request);

        try {
            Thread.sleep(5 * 1000L);
        } catch (Exception e) {
            log.info("sleep error - {}", e);
        }

        WxPayOrderQueryRequest orderQueryRequest = WxPayOrderQueryRequest.newBuilder()
                .outTradeNo("case2")
                .build();
        WxPayOrderQueryResult orderQueryResult = service.queryOrder(orderQueryRequest);

        check(orderQueryResult.getTradeState().compareTo(WxPayConstants.WxpayTradeStatus.SUCCESS) == 0, "trade status is not [SUCCESS]");
        check( orderQueryResult.getTotalFee() == 302, "total fee is not [302]");
        return result.getCodeUrl();
    }

    @RequestMapping(value = "/3", method = RequestMethod.GET)
    public String case3() throws WxPayException {
        WxPayUnifiedOrderRequest request = WxPayUnifiedOrderRequest.newBuilder()
                .body("【扫码-异常】订单金额3.3元")
                .outTradeNo("case3")
                .totalFee(330)
                .spbillCreateIp("182.92.157.225")
                .notifyUrl("http://182.92.157.225:13080/MyCar/pay/cases/notify/pay/3/")
                .productId("case3")
                .tradeType(WxPayConstants.TradeType.NATIVE)
                .build();

        WxPayNativeOrderResult result = service.createOrder(request);

        try {
            Thread.sleep(5 * 1000L);
        } catch (Exception e) {
            log.info("sleep error - {}", e);
        }

        WxPayOrderQueryRequest orderQueryRequest = WxPayOrderQueryRequest.newBuilder()
                .outTradeNo("case3")
                .build();

        WxPayOrderQueryResult orderQueryResult = service.queryOrder(orderQueryRequest);

        check( orderQueryResult.getTradeState().compareTo("SUCCESS") == 0 , "trade status is not [SUCCESS]");
        check( orderQueryResult.getTotalFee() == 330 , "total fee is not [330]");

        return "OK";
    }

    @RequestMapping(value = "/4", method = RequestMethod.GET)
    public String case4() throws WxPayException {
        WxPayUnifiedOrderRequest request = WxPayUnifiedOrderRequest.newBuilder()
                .body("【扫码-异常】订单金额3.31元")
                .outTradeNo("case4")
                .totalFee(331)
                .spbillCreateIp("182.92.157.225")
                .notifyUrl("http://182.92.157.225:13080/MyCar/pay/cases/notify/pay/4/")
                .productId("case4")
                .tradeType(WxPayConstants.TradeType.NATIVE)
                .build();

        WxPayNativeOrderResult result = service.createOrder(request);

        try {
            Thread.sleep(5 * 1000L);
        } catch (Exception e) {
            log.info("sleep error - {}", e);
        }

        WxPayOrderQueryRequest orderQueryRequest = WxPayOrderQueryRequest.newBuilder()
                .outTradeNo("case4")
                .build();

        WxPayOrderQueryResult orderQueryResult = service.queryOrder(orderQueryRequest);

        check( orderQueryResult.getTradeState().compareTo(WxPayConstants.WxpayTradeStatus.PAY_ERROR) == 0 , "trade status is not [PAYERROR]");
        check( orderQueryResult.getTotalFee() == 331 , "total fee is not [331]");

        return result.getCodeUrl();
    }

    @RequestMapping(value = "/5", method = RequestMethod.GET)
    public String case5() throws WxPayException {
        WxPayUnifiedOrderRequest request = WxPayUnifiedOrderRequest.newBuilder()
                .body("【扫码-异常】订单金额3.32元")
                .outTradeNo("case5")
                .totalFee(332)
                .spbillCreateIp("182.92.157.225")
                .notifyUrl("http://182.92.157.225:13080/MyCar/pay/cases/notify/pay/5/")
                .productId("case5")
                .tradeType(WxPayConstants.TradeType.NATIVE)
                .build();

        WxPayNativeOrderResult result = service.createOrder(request);

        try {
            Thread.sleep(5 * 1000L);
        } catch (Exception e) {
            log.info("sleep error - {}", e);
        }

        WxPayOrderQueryRequest orderQueryRequest = WxPayOrderQueryRequest.newBuilder()
                .outTradeNo("case5")
                .build();

        WxPayOrderQueryResult orderQueryResult = service.queryOrder(orderQueryRequest);

        check( orderQueryResult.getTradeState().compareTo(WxPayConstants.WxpayTradeStatus.SUCCESS) == 0 , "trade status is not [SUCCESS]");
        check( orderQueryResult.getTotalFee() == 332 , "total fee is not [332]");


        return result.getCodeUrl();

    }

    @RequestMapping(value = "/6", method = RequestMethod.GET)
    public String case6() throws WxPayException {
        WxPayUnifiedOrderRequest request = WxPayUnifiedOrderRequest.newBuilder()
                .body("【扫码-异常】订单金额3.33元")
                .outTradeNo("case6")
                .totalFee(333)
                .spbillCreateIp("182.92.157.225")
                .notifyUrl("http://182.92.157.225:13080/MyCar/pay/cases/notify/pay/6/")
                .productId("case6")
                .tradeType(WxPayConstants.TradeType.NATIVE)
                .build();

        WxPayNativeOrderResult result = service.createOrder(request);

        try {
            Thread.sleep(5 * 1000L);
        } catch (Exception e) {
            log.info("sleep error - {}", e);
        }

        WxPayOrderQueryRequest orderQueryRequest = WxPayOrderQueryRequest.newBuilder()
                .outTradeNo("case6")
                .build();

        WxPayOrderQueryResult orderQueryResult = service.queryOrder(orderQueryRequest);

        check(orderQueryResult.getTradeState().compareTo(WxPayConstants.WxpayTradeStatus.SUCCESS) == 0 , "trade status is not [SUCCESS]");
        check( orderQueryResult.getTotalFee() == 333 , "total fee is not [333]");


        return result.getCodeUrl();
    }

    @RequestMapping(value = "/7", method = RequestMethod.GET)
    public String case7() throws WxPayException {
        WxPayUnifiedOrderRequest request = WxPayUnifiedOrderRequest.newBuilder()
                .body("【扫码-异常】订单金额3.34元")
                .outTradeNo("case7")
                .totalFee(334)
                .spbillCreateIp("182.92.157.225")
                .notifyUrl("http://182.92.157.225:13080/MyCar/pay/cases/notify/pay/7/")
                .productId("case7")
                .tradeType(WxPayConstants.TradeType.NATIVE)
                .build();

        WxPayNativeOrderResult result = service.createOrder(request);

        try {
            Thread.sleep(5 * 1000L);
        } catch (Exception e) {
            log.info("sleep error - {}", e);
        }

        WxPayOrderQueryRequest orderQueryRequest = WxPayOrderQueryRequest.newBuilder()
                .outTradeNo("case7")
                .build();

        WxPayOrderQueryResult orderQueryResult = service.queryOrder(orderQueryRequest);

        check(orderQueryResult.getTradeState().compareTo(WxPayConstants.WxpayTradeStatus.SUCCESS) == 0 , "trade status is not [SUCCESS]");
        check( orderQueryResult.getTotalFee() == 20000 , "total fee is not [20000]");


        return result.getCodeUrl();
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String orderCreate() throws WxPayException {
        WxPayUnifiedOrderRequest request = WxPayUnifiedOrderRequest.newBuilder()
                .body("【测试】订单金额0.01元")
                .outTradeNo("test2")
                .totalFee(1)
                .spbillCreateIp("182.92.157.225")
                .notifyUrl("http://182.92.157.225:13080/MyCar/pay/cases/notify/pay/-1/")
                .productId("test")
                .tradeType(WxPayConstants.TradeType.NATIVE)
                .build();

        WxPayNativeOrderResult result = service.createOrder(request);

        return result.getCodeUrl();
    }

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public String orderQuery() throws WxPayException {
        WxPayOrderQueryRequest request = WxPayOrderQueryRequest.newBuilder()
                .outTradeNo("test2")
                .build();

        WxPayOrderQueryResult result = service.queryOrder(request);

        return result.toString();
    }

    @RequestMapping(value = "/refund", method = RequestMethod.GET)
    public String orderRefund() throws WxPayException {
        WxPayRefundRequest request = WxPayRefundRequest.newBuilder()
                .outTradeNo("test2")
                .outRefundNo("test2-refund")
                .totalFee(1)
                .refundFee(1)
                .notifyUrl("http://182.92.157.225:13080/MyCar/pay/cases/notify/refund/-1/")
                .build();
        return service.refund(request).toString();
    }

}
