package org.buaa.ly.MyCar.action;


import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.http.HttpResponse;
import org.buaa.ly.MyCar.http.dto.VehicleInfoDTO;
import org.buaa.ly.MyCar.service.AccountService;
import org.buaa.ly.MyCar.service.VehicleInfoService;
import org.buaa.ly.MyCar.utils.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
    public HttpResponse find(@RequestHeader String token,
                             @PathVariable int id) {
        accountService.check(token, RoleEnum.OPERATOR);

        return new HttpResponse(vehicleInfoService.find(id));
    }

    @RequestMapping(method = RequestMethod.GET)
    public HttpResponse find(@RequestHeader String token,
                             @RequestParam(required = false) List<Integer> status,
                             @RequestParam(required = false, defaultValue = "false") boolean exclude) {
        return new HttpResponse(vehicleInfoService.find(status, exclude));
    }

    // Timestamp参数格式为 yyyy-mm-dd HH:MM:SS 即可
    @RequestMapping(value = "/{sid}/{begin}/{end}/", method = RequestMethod.GET)
    public HttpResponse find(@RequestHeader String token,
                             @PathVariable int sid,
                             @PathVariable Timestamp begin,
                             @PathVariable Timestamp end) {
        //TODO: 完成时间排查车型返回
        return new HttpResponse(vehicleInfoService.find(sid, begin, end));
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public HttpResponse insert(@RequestHeader String token,
                               @RequestPart(value = "vehicleInfoDTO") VehicleInfoDTO vehicleInfoDTO,
                               @RequestPart(value = "attachment", required = false) Part attachment) {
        accountService.check(token, RoleEnum.OPERATOR);

        return new HttpResponse(vehicleInfoService.insert(vehicleInfoDTO, attachment));
    }


    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public HttpResponse update(@RequestHeader String token,
                               @RequestPart(value = "vehicleInfoDTO") VehicleInfoDTO vehicleInfoDTO,
                               @RequestPart(required = false) Part attachment) {
        accountService.check(token, RoleEnum.OPERATOR);

        vehicleInfoService.update(vehicleInfoDTO, attachment);

        return new HttpResponse();
    }

    @RequestMapping(value = "/{id}/", method = RequestMethod.DELETE)
    public HttpResponse delete(@RequestHeader String token,
                               @PathVariable int id) {
        accountService.check(token, RoleEnum.OPERATOR);

        vehicleInfoService.delete(id, false);

        return new HttpResponse();
    }

}
