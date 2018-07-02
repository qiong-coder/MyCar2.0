package org.buaa.ly.MyCar.config;


import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:weixin-pay.properties")
public class PayConfig {

    @Value("${weixin.app.id}") protected String app_id;
    @Value("${weixin.mch.id}") protected String mch_id;
    @Value("${weixin.mch.key}") protected String mch_key;

    @Bean
    public WxPayConfig wxPayConfig() {
        WxPayConfig config = new WxPayConfig();
        config.setAppId(app_id);
        config.setMchId(mch_id);
        config.setMchKey(mch_key);
        config.setKeyPath("classpath:weixin-cert.p12");
        return config;
    }


    @Bean
    public WxPayService wxPayService(WxPayConfig wxPayConfig) {
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(wxPayConfig);
        return wxPayService;
    }
}
