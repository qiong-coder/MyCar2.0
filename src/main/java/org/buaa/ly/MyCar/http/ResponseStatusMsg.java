package org.buaa.ly.MyCar.http;

public enum ResponseStatusMsg {

    SUCCESS(0, "success"),
    ERROR(-1, "error"),
    ACCOUNT_LOGIN_ERROR(-12, "failure to login"),
    CODE_ERROR(-14, "code check error"),
    CODE_NOT_FOUND(-15, "failrue to find the code"),
    PERMISSION_DENY(-16, "permission deny"),
    NO_ACCOUNT(-17, "no account"),
    DUPLICATE_ACCOUNT(-18, "duplicate account"),
    MUST_LOGIN(-20, "must login"),
    NO_FILE(-21, "file not founded"),
    SEND_CODE_ERROR(-22, "send code error"),
    NOT_FOUND(-23, "not found"),
    DUPLICATE_ERROR(-24, "duplicate error"),
    STATUS_ERROR(-25, "status error"),
    CHECK_ERROR(-26, "check error"),
    CODE_REQUEST_TIMEOUT(-27, "code request not timeout");


    private int status;
    private String message;

    ResponseStatusMsg(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

}
