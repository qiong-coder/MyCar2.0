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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.sql.Timestamp;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/vehicle/info")
public class VehicleInfoAction {

    @Autowired private AccountService accountService;

    @Autowired private VehicleInfoService vehicleInfoService;


    @RequestMapping(value = "/{id}/", method = RequestMethod.GET)
    public HttpResponse find(HttpServletRequest request,
                             @PathVariable int id) {
        accountService.check(request, RoleEnum.OPERATOR);

        return new HttpResponse(vehicleInfoService.find(id));
    }

    @RequestMapping(method = RequestMethod.GET)
    public HttpResponse find(HttpServletRequest request,
                             @RequestParam(required = false) List<Integer> status,
                             @RequestParam(required = false, defaultValue = "false") boolean exclude) {
        return new HttpResponse(vehicleInfoService.find(status, exclude));
    }

    @RequestMapping(value = "/{sid}/{begin}/{end}/", method = RequestMethod.GET)
    public HttpResponse find(HttpServletRequest request,
                             @PathVariable int sid,
                             @PathVariable Timestamp begin,
                             @PathVariable Timestamp end) {
        //TODO: 完成时间排查车型返回
        return null;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public HttpResponse insert(HttpServletRequest request,
                               @RequestPart(value = "vehicleInfoDTO") VehicleInfoDTO vehicleInfoDTO,
                               @RequestPart(value = "attachment", required = false) Part attachment) {
        accountService.check(request, RoleEnum.OPERATOR);

        return new HttpResponse(vehicleInfoService.insert(vehicleInfoDTO, attachment));
    }


    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public HttpResponse update(HttpServletRequest request,
                               VehicleInfoDTO vehicleInfoDTO,
                               @RequestPart(required = false) Part attachment) {
        accountService.check(request, RoleEnum.OPERATOR);

        vehicleInfoService.update(vehicleInfoDTO, attachment);

        return new HttpResponse();
    }

    @RequestMapping(value = "/{id}/", method = RequestMethod.DELETE)
    public HttpResponse delete(HttpServletRequest request,
                               @PathVariable int id) {
        accountService.check(request, RoleEnum.OPERATOR);

        vehicleInfoService.delete(id, false);

        return new HttpResponse();
    }

}
