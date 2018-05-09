package org.buaa.ly.MyCar.action;


import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.http.HttpResponse;
import org.buaa.ly.MyCar.http.dto.VehicleInfoCostDTO;
import org.buaa.ly.MyCar.service.AccountService;
import org.buaa.ly.MyCar.service.VehicleInfoCostService;
import org.buaa.ly.MyCar.utils.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        VehicleInfoCostDTO vehicleInfoCostDTO = vehicleInfoCostService.find(id);
        vehicleInfoCostDTO.buildFinalDayCosts();
        return new HttpResponse(vehicleInfoCostDTO);

    }

    @RequestMapping(value = "/{id}/", method = RequestMethod.PUT)
    public HttpResponse update(@RequestHeader String token,
                               @PathVariable int id,
                               @RequestBody VehicleInfoCostDTO vehicleInfoCostDTO){
        accountService.check(token, RoleEnum.OPERATOR);
        vehicleInfoCostService.update(id, vehicleInfoCostDTO);
        return new HttpResponse();
    }
}
