package org.buaa.ly.MyCar.logic;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayNativeOrderResult;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import org.buaa.ly.MyCar.entity.Order;

public interface PayLogic {

    WxPayNativeOrderResult getPayUrl(Order order, String ip) throws WxPayException;

    WxPayOrderQueryResult queryOrder(String oid) throws WxPayException;

    WxPayRefundResult refundOrder(Order order) throws WxPayException;

    WxPayRefundQueryResult refundQuery(String oid) throws WxPayException;

    WxPayOrderNotifyResult payNotify(String xmlData) throws WxPayException;

    WxPayRefundNotifyResult refundNotify(String xmlData) throws WxPayException;

}
