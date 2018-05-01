package org.buaa.ly.MyCar.exception;

import org.buaa.ly.MyCar.http.ResponseStatusMsg;

public class LoginError extends BaseError {

    public LoginError() {
        super(ResponseStatusMsg.ACCOUNT_LOGIN_ERROR);
    }

}
