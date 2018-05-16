package org.buaa.ly.MyCar.action;


import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.http.HttpResponse;
import org.buaa.ly.MyCar.http.dto.StoreDTO;
import org.buaa.ly.MyCar.service.AccountService;
import org.buaa.ly.MyCar.service.StoreService;
import org.buaa.ly.MyCar.utils.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@Slf4j
@RequestMapping("/store")
public class StoreAction {

    @Autowired StoreService storeService;

    @Autowired AccountService accountService;

    @RequestMapping(value = "/{id}/", method = GET)
    public HttpResponse findById(@PathVariable int id) {

        return new HttpResponse(storeService.find(id));
    }

    @RequestMapping(value = "/", method = GET)
    public HttpResponse find() {
        return new HttpResponse(storeService.find());
    }

    @RequestMapping(value = "/", method = POST)
    public HttpResponse insert(@RequestHeader String token,
                               @RequestBody StoreDTO storeDTO) {
        accountService.check(token, RoleEnum.OPERATOR);
        return new HttpResponse(storeService.insert(storeDTO));
    }

    @RequestMapping(value = "/{id}/", method = PUT)
    public HttpResponse update(@RequestHeader String token,
                               @PathVariable int id,
                               @RequestBody StoreDTO storeDTO) {
        accountService.check(token, RoleEnum.OPERATOR);
        return new HttpResponse(storeService.update(id, storeDTO));
    }

    @RequestMapping(value = "/{id}/", method = DELETE)
    public HttpResponse delete(@RequestHeader String token,
                               @PathVariable int id) {
        accountService.check(token, RoleEnum.OPERATOR);
        return new HttpResponse(storeService.delete(id));
    }

}
