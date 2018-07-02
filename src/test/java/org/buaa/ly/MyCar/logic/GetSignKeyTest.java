package org.buaa.ly.MyCar.logic;

import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.config.TestLoader;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


@Slf4j
public class GetSignKeyTest extends TestLoader {

    @Autowired WxPayService wxPayService;


    @Test
    public void getSandBoxKey() throws WxPayException {
        String key = wxPayService.getSandboxSignKey();
        log.debug("key:{}", key);
        assert(key != null );
    }



}
