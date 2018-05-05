package org.buaa.ly.MyCar.exception;

import org.buaa.ly.MyCar.http.ResponseStatusMsg;

public class MustLoginError extends BaseError {

    public MustLoginError() {
        super(ResponseStatusMsg.MUST_LOGIN);
    }

}
