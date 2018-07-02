package org.buaa.ly.MyCar.service.impl;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayNativeOrderResult;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.entity.Order;
import org.buaa.ly.MyCar.exception.NotFoundError;
import org.buaa.ly.MyCar.exception.PayError;
import org.buaa.ly.MyCar.exception.StatusError;
import org.buaa.ly.MyCar.logic.OrderLogic;
import org.buaa.ly.MyCar.logic.PayLogic;
import org.buaa.ly.MyCar.service.PayService;
import org.buaa.ly.MyCar.utils.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("payService")
@Slf4j
@Transactional
public class PayServiceImpl implements PayService {

    PayLogic payLogic;

    OrderLogic orderLogic;

    @Autowired
    public void setWeiXinPayLogic(@Qualifier("payLogic") PayLogic payLogic) {
        this.payLogic = payLogic;
    }

    @Autowired
    public void setOrderLogic(OrderLogic orderLogic) {
        this.orderLogic = orderLogic;
    }


    @Override
    public String payUrl(String platform, int id, String ip) {

        Order order = orderLogic.find(id);

        if (order == null) throw new NotFoundError(String.format("failure to find the order - %d", id));
        else if ( order.getStatus().compareTo(StatusEnum.UNPIAD.getStatus()) != 0 ) throw new StatusError("order's status is not unpaid");

        try {
            WxPayNativeOrderResult result = payLogic.getPayUrl(order, ip);
            return result.getCodeUrl();
        } catch ( WxPayException exception) {
            log.warn("failure to get pay url - order:{}\tip:{}\texception:{}", order, ip, exception);
            throw new PayError("failure to get pay url");
        }

    }

    @Override
    public boolean check(int id) {

        Order order = orderLogic.find(id);

        if ( order == null ) throw new NotFoundError(String.format("failure to find the order - %d", id));
        else if ( order.getStatus().compareTo(StatusEnum.PENDING.getStatus()) >= 0 ) return true;

        try {
            WxPayOrderQueryResult result = payLogic.queryOrder(order.getOid());
            if ( result.getTradeState() == null || result.getTradeState().compareTo(WxPayConstants.WxpayTradeStatus.SUCCESS) != 0 ) {
                throw new PayError(String.format("failure to check the pay: %s", result.getTradeStateDesc()));
            } else if ( orderLogic.update(id, StatusEnum.PENDING.getStatus()) == null) {
                throw new PayError(String.format("failure to update the order status: %s", order));
            }
            return true;
        } catch ( WxPayException exception ) {
            log.warn("failure to query the order - order:{}\texception:{}", order,exception);
            throw new PayError(String.format("failure to check the order: %s", exception));
        }

    }


    @Override
    public boolean refund(int id) {

        Order order = orderLogic.find(id);

        if ( order == null ) throw new NotFoundError(String.format("failure to find the order - %d", id));
        else if ( order.getStatus() <= StatusEnum.UNPIAD.getStatus() ) throw new StatusError(String.format("order'status is not paid - %d", id));

        try {
            WxPayRefundResult result = payLogic.refundOrder(order);

            if ( result.getResultCode().compareTo(WxPayConstants.ResultCode.SUCCESS) != 0 ) throw new PayError(String.format("failure to refund the order: %s", result));

            // 设置退款状态
            if ( orderLogic.update(id, StatusEnum.DRAWBACK.getStatus()) == null ) throw new PayError(String.format("failure to update order status: %s", order));

            return true;
        } catch (WxPayException exception) {
            log.warn("failure to refund the order - order:{}\texception:{}", order, exception);
            return false;
        }

    }

    @Override
    public String notify(String type, String xmlData) {

        if ( type.compareTo("pay") == 0 ) {
            try {
                WxPayOrderNotifyResult result = payLogic.payNotify(xmlData);
                if ( result.getReturnCode().compareTo("SUCCESS") != 0 || result.getResultCode().compareTo("SUCCESS") != 0 ) {
                    log.warn("failure to process pay notify - {}", result);
                    return WxPayNotifyResponse.fail(result.getReturnMsg());
                } else {
                    String oid = result.getOutTradeNo();
                    Order order = orderLogic.find(oid);
                    if ( order.getStatus() == StatusEnum.UNPIAD.getStatus() ) {
                        orderLogic.update(order.getId(), StatusEnum.PENDING.getStatus());
                    }
                    return WxPayNotifyResponse.success("OK");
                }
            } catch ( WxPayException exception ) {
                log.warn("failure to process pay notify - {}", exception);
                return WxPayNotifyResponse.fail(exception.getReturnMsg());
            }

        } else if ( type.compareTo("refund") == 0 ) {
            try {
                WxPayRefundNotifyResult result = payLogic.refundNotify(xmlData);
                if ( result.getReturnCode().compareTo("SUCCESS") != 0 || result.getResultCode().compareTo("SUCCESS") != 0 ) {
                    log.warn("failure to process refund notify - {}", result);
                    return WxPayNotifyResponse.fail(result.getReturnMsg());
                } else {
                    String oid = result.getReqInfo().getOutTradeNo();
                    Order order = orderLogic.find(oid);
                    if ( order.getStatus() == StatusEnum.DRAWBACK.getStatus() )
                        orderLogic.update(order.getId(), StatusEnum.CANCELED.getStatus());
                    return WxPayNotifyResponse.success("OK");
                }
            } catch ( WxPayException exception ) {
                log.warn("failure to process refund notify - {}", exception);
                return WxPayNotifyResponse.fail(exception.getReturnMsg());
            }
        } else {
            log.warn("unsupported pay's type - {}", type);
            return WxPayNotifyResponse.fail("");
        }

    }
}
