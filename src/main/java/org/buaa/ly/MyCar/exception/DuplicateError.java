package org.buaa.ly.MyCar.exception;

import org.buaa.ly.MyCar.http.ResponseStatusMsg;

public class DuplicateError extends BaseError {

    public DuplicateError(String msg) {
        super(ResponseStatusMsg.DUPLICATE_ERROR.getStatus(), msg);
    }

    public DuplicateError() {
        super(ResponseStatusMsg.DUPLICATE_ERROR);
    }
}
