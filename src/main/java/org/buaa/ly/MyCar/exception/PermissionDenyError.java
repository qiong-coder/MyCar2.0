package org.buaa.ly.MyCar.exception;

import org.buaa.ly.MyCar.http.ResponseStatusMsg;

public class PermissionDenyError extends BaseError {

    public PermissionDenyError() {
        super(ResponseStatusMsg.PERMISSION_DENY);
    }

}
