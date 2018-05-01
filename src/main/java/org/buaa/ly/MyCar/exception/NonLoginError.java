package org.buaa.ly.MyCar.exception;

import org.buaa.ly.MyCar.http.ResponseStatusMsg;

public class NonLoginError extends BaseError {

    public NonLoginError() {
        super(ResponseStatusMsg.MUST_LOGIN);
    }

}
