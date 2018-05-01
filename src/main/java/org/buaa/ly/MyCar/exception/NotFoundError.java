package org.buaa.ly.MyCar.exception;

import org.buaa.ly.MyCar.http.ResponseStatusMsg;

public class NotFoundError extends BaseError {

    public NotFoundError(String msg) {
        super(ResponseStatusMsg.NOT_FOUND.getStatus(), msg);
    }

}
