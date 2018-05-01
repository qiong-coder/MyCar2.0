package org.buaa.ly.MyCar.logic.impl;

import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.logic.PayLogic;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("payLogic")
@Slf4j
@Transactional
public class PayLogicImpl implements PayLogic {

    @Override
    public boolean check(int id) {
        return true;
    }
}
