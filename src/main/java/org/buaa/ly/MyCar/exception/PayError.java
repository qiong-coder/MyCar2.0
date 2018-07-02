package org.buaa.ly.MyCar.exception;

import org.buaa.ly.MyCar.http.ResponseStatusMsg;

public class PayError extends BaseError {

    public PayError() {
        super(ResponseStatusMsg.PAY_ERROR);
    }

    public PayError(String msg) {
        super(ResponseStatusMsg.PAY_ERROR.getStatus(), msg);
    }

}
