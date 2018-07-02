package org.buaa.ly.MyCar.config;

import com.github.binarywang.wxpay.config.WxPayConfig;
import org.springframework.context.annotation.Bean;

public class TestPayConfig extends PayConfig {



    @Bean
    public WxPayConfig wxPayConfig() {
        mch_key = "7ef22f0075490aa37318cd8f4e29bb03";
        WxPayConfig config = new WxPayConfig();
        config.setAppId(app_id);
        config.setMchId(mch_id);
        config.setMchKey(mch_key);
        config.setUseSandboxEnv(true);
        return config;
    }

}
