package org.buaa.ly.MyCar.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.service.PayService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("payService")
@Slf4j
@Transactional
public class PayServiceImpl implements PayService {

    @Override
    public String payUrl(String platform, int id, String ip) {
        return null;
    }

    @Override
    public boolean checkPay(int id) {
        return false;
    }
}
