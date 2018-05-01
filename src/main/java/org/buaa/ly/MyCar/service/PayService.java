package org.buaa.ly.MyCar.service;

public interface PayService {

    String payUrl(String platform, int id, String ip);

    boolean checkPay(int id);

}
