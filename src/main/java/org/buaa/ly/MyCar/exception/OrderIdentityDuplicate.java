package org.buaa.ly.MyCar.exception;


import org.buaa.ly.MyCar.http.ResponseStatusMsg;

public class OrderIdentityDuplicate extends BaseError {

    public OrderIdentityDuplicate(String message) {
        super(ResponseStatusMsg.ERROR.getStatus(), message);
    }

}
