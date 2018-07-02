package org.buaa.ly.MyCar.action;


import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.http.HttpResponse;
import org.buaa.ly.MyCar.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


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
        if ( payService.check(id) ) return new HttpResponse();
        else return HttpResponse.buildErrorResponse();
    }


    @RequestMapping(value = "/weixin/refund/{id}/", method = RequestMethod.DELETE)
    public HttpResponse refund(@PathVariable int id) {
        if ( payService.refund(id) ) return new HttpResponse();
        else return HttpResponse.buildErrorResponse();
    }

    @RequestMapping(value = "/weixin/notify/{type}/", method = RequestMethod.POST)
    public String notify(@PathVariable String type,
                         @RequestBody String xmlData) {
        return payService.notify(type, xmlData);
    }
}
