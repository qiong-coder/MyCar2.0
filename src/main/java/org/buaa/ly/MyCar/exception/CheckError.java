package org.buaa.ly.MyCar.exception;


import org.buaa.ly.MyCar.http.ResponseStatusMsg;

public class CheckError extends BaseError {

    public CheckError(String msg) {
        super(ResponseStatusMsg.CHECK_ERROR.getStatus(), msg);
    }
}
