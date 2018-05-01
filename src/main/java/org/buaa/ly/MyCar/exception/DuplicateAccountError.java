package org.buaa.ly.MyCar.exception;

import org.buaa.ly.MyCar.http.ResponseStatusMsg;

public class DuplicateAccountError extends BaseError {

    public DuplicateAccountError() {
        super(ResponseStatusMsg.DUPLICATE_ACCOUNT);
    }

}
