package org.buaa.ly.MyCar.action;


import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.http.HttpResponse;
import org.buaa.ly.MyCar.http.dto.StoreDTO;
import org.buaa.ly.MyCar.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@Slf4j
@RequestMapping("/store")
public class StoreAction {

    @Autowired StoreService storeService;

    @RequestMapping(value = "/{id}/", method = GET)
    public HttpResponse findById(@PathVariable int id) {

        return new HttpResponse(storeService.find(id));
    }

    @RequestMapping(value = "/", method = GET)
    public HttpResponse find() {
        return new HttpResponse(storeService.find());
    }

    @RequestMapping(value = "/", method = POST)
    public HttpResponse insert(HttpServletRequest request,
                               @RequestBody StoreDTO storeDTO) {
        return new HttpResponse(storeService.insert(storeDTO));
    }

    @RequestMapping(value = "/{id}/", method = PUT)
    public HttpResponse update(HttpServletRequest request,
                               @PathVariable int id,
                               @RequestBody StoreDTO storeDTO) {
        return new HttpResponse(storeService.update(id, storeDTO));
    }

    @RequestMapping(value = "/{id}/", method = DELETE)
    public HttpResponse delete(HttpServletRequest request,
                               @PathVariable int id) {
        return new HttpResponse(storeService.delete(id));
    }

}
