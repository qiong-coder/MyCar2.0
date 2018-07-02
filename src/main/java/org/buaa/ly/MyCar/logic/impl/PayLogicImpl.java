package org.buaa.ly.MyCar.logic.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayNativeOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayOrderQueryRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.entity.Order;
import org.buaa.ly.MyCar.exception.BaseError;
import org.buaa.ly.MyCar.http.ResponseStatusMsg;
import org.buaa.ly.MyCar.http.dto.OrderDTO;
import org.buaa.ly.MyCar.internal.PreCost;
import org.buaa.ly.MyCar.logic.PayLogic;
import org.buaa.ly.MyCar.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("payLogic")
@Slf4j
@Transactional
public class PayLogicImpl implements PayLogic {

    private WxPayService payService;

    @Autowired
    public void setPayService(@Qualifier("wxPayService") WxPayService payService) {
        this.payService = payService;
    }

    @Override
    public WxPayNativeOrderResult getPayUrl(Order order, String ip) throws WxPayException {

        PreCost preCost = JSONObject.parseObject(order.getPreCost(), PreCost.class);

        WxPayUnifiedOrderRequest request = WxPayUnifiedOrderRequest.newBuilder()
                .body(String.format("MyCar-%s", order.getVehicleInfo().getName()))
                .outTradeNo(order.getOid())
                .totalFee(preCost.getTotal_cost())
                .spbillCreateIp(ip)
                .timeStart(WxPayConstants.QUERY_COMMENT_DATE_FORMAT.format(order.getCreateTime()))
                .notifyUrl("http://mycarzc.com/pay/weixin/nofity/pay/")
                .productId(order.getVehicleInfo().getName())
                .tradeType(WxPayConstants.TradeType.NATIVE).build();

        return payService.createOrder(request);
    }

    @Override
    public WxPayOrderQueryResult queryOrder(String oid) throws WxPayException {
        return payService.queryOrder(null, oid);
    }

    @Override
    public WxPayRefundResult refundOrder(Order order) throws WxPayException {

        PreCost preCost = JSONObject.parseObject(order.getPreCost(), PreCost.class);

        WxPayRefundRequest request = WxPayRefundRequest.newBuilder()
                .outTradeNo(order.getOid())
                .outRefundNo(order.getOid()+"-refund")
                .totalFee(preCost.getTotal_cost())
                .refundFee(preCost.getTotal_cost())
                .notifyUrl("http://mycarzc/com/pay/weixin/nofity/refund/")
                .build();

        return payService.refund(request);
    }

    @Override
    public WxPayRefundQueryResult refundQuery(String oid) throws WxPayException {
        return payService.refundQuery(null, oid, null, null);
    }

    @Override
    public WxPayOrderNotifyResult payNotify(String xmlData) throws WxPayException {
        return payService.parseOrderNotifyResult(xmlData);
    }

    @Override
    public WxPayRefundNotifyResult refundNotify(String xmlData) throws WxPayException {
        return payService.parseRefundNotifyResult(xmlData);
    }
}
