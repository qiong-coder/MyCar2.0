package org.buaa.ly.MyCar.exception;

import org.buaa.ly.MyCar.http.ResponseStatusMsg;

public class SendCodeError extends BaseError {

    public SendCodeError() {
        super(ResponseStatusMsg.SEND_CODE_ERROR);
    }

}
