package org.buaa.ly.MyCar.action;


import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.entity.Vehicle;
import org.buaa.ly.MyCar.http.HttpResponse;
import org.buaa.ly.MyCar.http.dto.VehicleDTO;
import org.buaa.ly.MyCar.service.AccountService;
import org.buaa.ly.MyCar.service.VehicleService;
import org.buaa.ly.MyCar.utils.RoleEnum;
import org.buaa.ly.MyCar.utils.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
public class VehicleAction {

    @Autowired private AccountService accountService;

    @Autowired private VehicleService vehicleService;

    @RequestMapping(value = "/vehicles/", method = RequestMethod.GET)
    public HttpResponse findByStatusNot()
    {
        return new HttpResponse(vehicleService.findByStatusNot(StatusEnum.DELETE.getStatus()));
    }


    @RequestMapping(value = "/vehicles/{viid}/", method = RequestMethod.GET)
    public HttpResponse findByViid(@PathVariable int viid) {
        return new HttpResponse(vehicleService.findByViidAndStatusNot(viid, 0));
    }

    @RequestMapping(value = "/vehicles/{viid}/{sid}/{status}/", method = RequestMethod.GET)
    public HttpResponse findByViidAndSidAndStatus(@PathVariable int viid,
                                                  @PathVariable int sid,
                                                  @PathVariable int status) {
        return new HttpResponse(vehicleService.findByViidAndSidAndStatus(viid, sid, status));
    }

    @RequestMapping(value = "/vehicle/{id}/", method = RequestMethod.GET)
    public HttpResponse findById(@PathVariable int id) {
        return new HttpResponse(vehicleService.find(id));
    }

    @RequestMapping(value = "/vehicle/{viid}/", method = RequestMethod.POST)
    public HttpResponse insert(HttpServletRequest request,
                               @PathVariable int viid,
                               @RequestBody VehicleDTO vehicleDTO) {
        accountService.check(request, RoleEnum.OPERATOR);
        return new HttpResponse(vehicleService.insert(viid, vehicleDTO));
    }

    @RequestMapping(value = "/vehicle/{id}/", method = RequestMethod.PUT)
    public HttpResponse update(HttpServletRequest request,
                               @PathVariable int id,
                               @RequestBody VehicleDTO vehicleDTO) {
        accountService.check(request, RoleEnum.OPERATOR);
        vehicleService.update(id, vehicleDTO);
        return new HttpResponse();
    }

    @RequestMapping(value = "/vehicle/{id}/{force}/", method = RequestMethod.DELETE)
    public HttpResponse delete(HttpServletRequest request,
                               @PathVariable int id,
                               @PathVariable int force) {
        accountService.check(request, RoleEnum.OPERATOR);
        if ( force == 0 ) {
            vehicleService.update(id, StatusEnum.DELETE.getStatus());
        } else {
            vehicleService.delete(id);
        }
        return new HttpResponse();
    }


}
