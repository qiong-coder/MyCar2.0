package org.buaa.ly.MyCar.exception;

import org.buaa.ly.MyCar.http.ResponseStatusMsg;

public class OrderPaid extends BaseError {

    public OrderPaid() { super(ResponseStatusMsg.ORDER_PAID); }

    public OrderPaid(String msg) {
        super(ResponseStatusMsg.ORDER_PAID.getStatus(), msg);
    }

}
