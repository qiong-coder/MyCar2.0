package org.buaa.ly.MyCar.exception;

import org.buaa.ly.MyCar.http.ResponseStatusMsg;

public class CodeRequestTimeoutError extends BaseError {

    public CodeRequestTimeoutError() {
        super(ResponseStatusMsg.CODE_REQUEST_TIMEOUT);
    }

}
