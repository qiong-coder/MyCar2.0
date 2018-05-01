package org.buaa.ly.MyCar.action;


import org.buaa.ly.MyCar.http.HttpResponse;
import org.buaa.ly.MyCar.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/message")
public class MessageAction {

    @Autowired private MessageService messageService;

    @RequestMapping(value = "/phone/{phone}/", method = GET)
    public HttpResponse getCode(@PathVariable String phone) {
        return new HttpResponse(messageService.getCode(phone));
    }

    @RequestMapping(value = "/phone/{phone}/{code}/", method = GET)
    public HttpResponse checkCode(@PathVariable String phone,
                                  @PathVariable String code) {
        messageService.checkCode(phone, code);
        return new HttpResponse();
    }

    @RequestMapping(value = "/picture/", method = GET)
    public HttpResponse getPicture() {
        return new HttpResponse(messageService.getPicture());
    }

    @RequestMapping(value = "/picture/{picture}/{code}/", method = GET)
    public HttpResponse checkPicture(@PathVariable String picture,
                                     @PathVariable String code) {
        messageService.checkPicture(picture, code);
        return new HttpResponse();
    }
}
