package org.buaa.ly.MyCar.exception;

import org.buaa.ly.MyCar.http.ResponseStatusMsg;

public class CodeNotFoundError extends BaseError {

    public CodeNotFoundError() {
        super(ResponseStatusMsg.CODE_NOT_FOUND);
    }
}
