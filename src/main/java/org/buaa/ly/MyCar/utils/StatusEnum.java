package org.buaa.ly.MyCar.utils;

public enum StatusEnum {

    OK(0),
    UNPIAD(0),
    PENDING(1),
    RENTING(2),
    DRAWBACK(3),
    FIXING(3),
    FINISHED(4),
    VALIDATE(4),
    CANCELED(5),
    SPARE(5),
    DELETE(10);

    private final int status;

    StatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
