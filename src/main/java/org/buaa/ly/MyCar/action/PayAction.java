package org.buaa.ly.MyCar.action;


import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.http.HttpResponse;
import org.buaa.ly.MyCar.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequestMapping(value = "/pay")
public class PayAction {

    @Autowired private PayService payService;

    @RequestMapping(value = "/weixin/get/{id}/", method = RequestMethod.GET)
    public HttpResponse findPayUrl(HttpServletRequest request,
                                   @PathVariable int id) {
        return new HttpResponse(payService.payUrl("weixin",id,request.getRemoteHost()));
    }

    @RequestMapping(value = "/weixin/check/{id}/", method = RequestMethod.GET)
    public HttpResponse checkPay(@PathVariable int id) {
        return new HttpResponse(payService.checkPay(id));
    }

}
