package org.buaa.ly.MyCar.action;


import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.entity.VehicleInfo;
import org.buaa.ly.MyCar.exception.BaseError;
import org.buaa.ly.MyCar.http.HttpResponse;
import org.buaa.ly.MyCar.http.dto.VehicleInfoDTO;
import org.buaa.ly.MyCar.service.AccountService;
import org.buaa.ly.MyCar.service.UploadService;
import org.buaa.ly.MyCar.service.VehicleInfoService;
import org.buaa.ly.MyCar.service.VehicleService;
import org.buaa.ly.MyCar.utils.RoleEnum;
import org.buaa.ly.MyCar.utils.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.sql.Timestamp;

@RestController
@Slf4j
@RequestMapping("/vehicle/info")
public class VehicleInfoAction {

    @Autowired private AccountService accountService;

    @Autowired private VehicleInfoService vehicleInfoService;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public HttpResponse find(HttpServletRequest request) {
        accountService.check(request, RoleEnum.OPERATOR);

        return new HttpResponse(vehicleInfoService.findByStatusNot(StatusEnum.DELETE.getStatus()));
    }

    @RequestMapping(value = "/{begin}/{end}/", method = RequestMethod.GET)
    public HttpResponse find(HttpServletRequest request,
                             @PathVariable Timestamp begin,
                             @PathVariable Timestamp end) {
        //TODO: 完成时间排查车型返回
        return null;
    }

    @RequestMapping(value = "/{id}/", method = RequestMethod.GET)
    public HttpResponse find(HttpServletRequest request,
                             @PathVariable Integer id) {
        return new HttpResponse(vehicleInfoService.find(id));
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public HttpResponse insert(HttpServletRequest request,
                               @RequestBody VehicleInfoDTO vehicleInfoDTO,
                               @RequestPart Part attachment) {
        accountService.check(request, RoleEnum.OPERATOR);

        vehicleInfoService.insert(vehicleInfoDTO, attachment);

        return new HttpResponse();
    }


    @RequestMapping(value = "/{id}/", method = RequestMethod.POST)
    public HttpResponse update(HttpServletRequest request,
                               @PathVariable int id,
                               @RequestBody VehicleInfoDTO vehicleInfoDTO,
                               @RequestPart(required = false) Part attachment) {
        accountService.check(request, RoleEnum.OPERATOR);

        vehicleInfoService.update(id, vehicleInfoDTO, attachment);

        return new HttpResponse();
    }

    @RequestMapping(value = "/{id}/", method = RequestMethod.DELETE)
    public HttpResponse delete(HttpServletRequest request,
                               @PathVariable int id) {
        accountService.check(request, RoleEnum.OPERATOR);

        vehicleInfoService.delete(id, 0);

        return new HttpResponse();
    }





}
