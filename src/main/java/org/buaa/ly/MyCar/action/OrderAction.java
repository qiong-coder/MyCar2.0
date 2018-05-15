package org.buaa.ly.MyCar.action;


import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.entity.Vehicle;
import org.buaa.ly.MyCar.http.HttpResponse;
import org.buaa.ly.MyCar.http.dto.OrderDTO;
import org.buaa.ly.MyCar.service.AccountService;
import org.buaa.ly.MyCar.service.OrderService;
import org.buaa.ly.MyCar.utils.RoleEnum;
import org.buaa.ly.MyCar.utils.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

@RestController
@Slf4j
public class OrderAction {

    @Autowired private OrderService orderService;

    @Autowired private AccountService accountService;

    @RequestMapping(value = "/orders/{status}/", method = RequestMethod.GET)
    public HttpResponse findByStatus(@RequestHeader String token,
                                     @PathVariable int status) {
        accountService.check(token, RoleEnum.OPERATOR);
        return new HttpResponse(orderService.findByStatusAndVehiclesAndVehicleInfos(status));
    }

    @RequestMapping(value = "/orders/number/", method = RequestMethod.GET)
    public HttpResponse countByStatus(@RequestHeader String token) {
        accountService.check(token, RoleEnum.OPERATOR);
        return new HttpResponse(orderService.findByOrdersCountByStatus());
    }


    @RequestMapping(value = "/order/{id}/", method = RequestMethod.GET)
    public HttpResponse findById(@RequestHeader String token,
                                 @PathVariable int id) {
        accountService.check(token, RoleEnum.OPERATOR);
        return new HttpResponse(orderService.findByIdAndVehicleAndVehicleInfo(id));
    }

    @RequestMapping(value = "/order/{viid}/", method = RequestMethod.POST)
    public HttpResponse insert(@RequestHeader String token,
                               @PathVariable int viid,
                               @RequestBody OrderDTO orderDTO) {
        accountService.check(token, RoleEnum.USER);
        orderDTO = orderService.insert(viid, orderDTO);
        if ( orderDTO != null ) return new HttpResponse(orderDTO);
        else return HttpResponse.buildErrorResponse();
    }

    @RequestMapping(value = "/order/{id}/", method = RequestMethod.PUT)
    public HttpResponse update(@RequestHeader String token,
                               @PathVariable int id,
                               @RequestBody OrderDTO orderDTO) {
        accountService.check(token, RoleEnum.USER);
        orderDTO = orderService.update(id, orderDTO);
        if ( orderDTO != null ) return new HttpResponse(orderDTO);
        else return HttpResponse.buildErrorResponse();
    }

    @RequestMapping(value = "/order/{id}/", method = RequestMethod.DELETE)
    public HttpResponse delete(HttpServletRequest request,
                               @PathVariable int id) {
        if ( orderService.delete(id, 0) != null ) return new HttpResponse();
        else return HttpResponse.buildErrorResponse();
    }

    @RequestMapping(value = "/order/check/{id}/", method = RequestMethod.PUT)
    public HttpResponse check(HttpServletRequest request,
                              @PathVariable int id)
    {
        if ( orderService.check(id) != null ) return new HttpResponse();
        else return HttpResponse.buildErrorResponse();
    }

    @RequestMapping(value = "/order/rent/{id}/{number}/", method = RequestMethod.PUT)
    public HttpResponse rent(HttpServletRequest request,
                             @PathVariable int id,
                             @PathVariable String number,
                             @RequestBody OrderDTO orderDTO) {
        if ( orderService.rent(id, number, orderDTO) != null ) return new HttpResponse();
        else return HttpResponse.buildErrorResponse();
    }

    @RequestMapping(value = "/order/drawback/{id}/", method = RequestMethod.PUT)
    public HttpResponse drawback(HttpServletRequest request,
                                 @PathVariable int id,
                                 @RequestBody OrderDTO orderDTO)
    {
        if ( orderService.drawback(id, orderDTO) != null ) return new HttpResponse();
        else return HttpResponse.buildErrorResponse();
    }

    @RequestMapping(value = "/order/finished/{id}/", method = RequestMethod.PUT)
    public HttpResponse finished(HttpServletRequest request,
                                 @PathVariable int id,
                                 @RequestBody OrderDTO orderDTO)
    {
        if ( orderService.finished(id, orderDTO) != null ) return new HttpResponse();
        else return HttpResponse.buildErrorResponse();
    }

    @RequestMapping(value = "/order/cancel/{id}/", method = RequestMethod.PUT)
    public HttpResponse cancel(HttpServletRequest request,
                               @PathVariable int id,
                               @RequestBody OrderDTO orderDTO)
    {
        if ( orderService.cancel(id, orderDTO) != null ) return new HttpResponse();
        else return HttpResponse.buildErrorResponse();
    }

    @RequestMapping(value = "/order/history", method = RequestMethod.GET)
    public HttpResponse history(@RequestHeader String token,
                                @RequestParam(required = false) Integer viid,
                                @RequestParam(required = false) String number,
                                @RequestParam Timestamp begin,
                                @RequestParam Timestamp end)
    {
        accountService.check(token, RoleEnum.OPERATOR);
        return new HttpResponse(orderService.history(viid, number, begin, end));
    }

    @RequestMapping(value = "/order/schedule", method = RequestMethod.GET)
    public HttpResponse schedule(@RequestHeader String token,
                                 @RequestParam(required = false) Integer sid,
                                 @RequestParam(required = false) Integer viid,
                                 @RequestParam Timestamp begin,
                                 @RequestParam Timestamp end)
    {
        accountService.check(token, RoleEnum.OPERATOR);
        return new HttpResponse(orderService.schedule(sid, viid, begin, end));
    }

    @RequestMapping(value = "/order/conflict", method = RequestMethod.GET)
    public HttpResponse conflict(HttpServletRequest request,
                                 @RequestParam(required = false) Integer sid,
                                 @RequestParam(required = false) Integer viid,
                                 @RequestParam Timestamp begin,
                                 @RequestParam Timestamp end) {
        return new HttpResponse(orderService.conflict(sid, viid, begin, end));
    }

}
