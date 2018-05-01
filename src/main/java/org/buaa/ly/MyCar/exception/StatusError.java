package org.buaa.ly.MyCar.exception;

import org.buaa.ly.MyCar.http.ResponseStatusMsg;

public class StatusError extends BaseError {

    public StatusError(String error) {
        super(ResponseStatusMsg.STATUS_ERROR.getStatus(), error);
    }

}
