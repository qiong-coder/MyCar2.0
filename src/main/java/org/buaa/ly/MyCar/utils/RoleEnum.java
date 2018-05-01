package org.buaa.ly.MyCar.utils;

public enum RoleEnum {

    ADMINISTRATOR(0),
    OPERATOR(1),
    USER(2);

    private int role;

    RoleEnum(int role) {
        this.role = role;
    }

    public int getRole() {
        return role;
    }

}
