package org.buaa.ly.MyCar.service;


public interface MessageService {

    String getCode(String phone);

    void checkCode(String phone, String code);

    String getPicture();

    void checkPicture(String picture, String code);

}
