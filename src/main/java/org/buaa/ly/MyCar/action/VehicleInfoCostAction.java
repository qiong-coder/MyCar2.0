package org.buaa.ly.MyCar.action;


import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.http.HttpResponse;
import org.buaa.ly.MyCar.http.request.CostInfoRequest;
import org.buaa.ly.MyCar.service.AccountService;
import org.buaa.ly.MyCar.service.VehicleInfoCostService;
import org.buaa.ly.MyCar.utils.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

@RestController
@Slf4j
@RequestMapping(value = "/vehicle/info/cost")
public class VehicleInfoCostAction {

    @Autowired
    private AccountService accountService;

    @Autowired
    private VehicleInfoCostService vehicleInfoCostService;


    @RequestMapping(value = "/{id}/{begin}/{end}/", method = RequestMethod.GET)
    public HttpResponse find(@PathVariable int id,
                             @PathVariable Timestamp begin,
                             @PathVariable Timestamp end) {
        return new HttpResponse(vehicleInfoCostService.find(id, begin, end));
    }


    @RequestMapping(value = "/{id}/", method = RequestMethod.GET)
    public HttpResponse find(@PathVariable int id) {
        return new HttpResponse(vehicleInfoCostService.find(id));

    }

    @RequestMapping(value = "/{id}/", method = RequestMethod.PUT)
    public HttpResponse update(HttpServletRequest request,
                               @PathVariable int id,
                               @RequestBody CostInfoRequest costInfoRequest){
        accountService.check(request, RoleEnum.OPERATOR);
        vehicleInfoCostService.update(id, costInfoRequest);
        return new HttpResponse();
    }
}
