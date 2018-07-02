package org.buaa.ly.MyCar.service;

public interface PayService {

    String payUrl(String platform, int id, String ip);

    boolean check(int id);

    boolean refund(int id);

    String notify(String type, String xmlData);

}
