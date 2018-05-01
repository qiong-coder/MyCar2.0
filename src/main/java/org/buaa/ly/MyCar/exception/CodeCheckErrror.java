package org.buaa.ly.MyCar.exception;

import org.buaa.ly.MyCar.http.ResponseStatusMsg;

public class CodeCheckErrror extends BaseError {

    public CodeCheckErrror() {
        super(ResponseStatusMsg.CODE_ERROR);
    }

}
