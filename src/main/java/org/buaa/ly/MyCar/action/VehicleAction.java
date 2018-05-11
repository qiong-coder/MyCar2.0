package org.buaa.ly.MyCar.action;


import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.http.HttpResponse;
import org.buaa.ly.MyCar.http.dto.VehicleDTO;
import org.buaa.ly.MyCar.service.AccountService;
import org.buaa.ly.MyCar.service.VehicleService;
import org.buaa.ly.MyCar.utils.RoleEnum;
import org.buaa.ly.MyCar.utils.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.List;

@RestController
@Slf4j

public class VehicleAction {

    @Autowired private AccountService accountService;

    @Autowired private VehicleService vehicleService;

    @RequestMapping(value = "/vehicle/{id}/", method = RequestMethod.GET)
    public HttpResponse find(@PathVariable int id) {
        return new HttpResponse(vehicleService.find(id));
    }

    @RequestMapping(value = {"/vehicles"}, method = RequestMethod.GET)
    public HttpResponse find(@RequestParam(required = false) Integer sid,
                             @RequestParam(required = false) Integer viid,
                             @RequestParam(required = false) List<Integer> status,
                             @RequestParam(required = false, defaultValue = "false") boolean exclude)
    {
        return new HttpResponse(vehicleService.findBySidAndViidAndStatus(sid, viid, status, exclude));
    }

    @RequestMapping(value = "/vehicles/{viid}/{sid}/{begin}/{end}/", method = RequestMethod.GET)
    public HttpResponse findByViidAndTimestamp(@PathVariable int viid,
                                               @PathVariable int sid,
                                               @PathVariable Timestamp begin,
                                               @PathVariable Timestamp end) {
        return new HttpResponse(vehicleService.findByViidAndSidAndTimestamp(viid, sid, begin, end));
    }


    @RequestMapping(value = "/vehicle/", method = RequestMethod.POST)
    public HttpResponse insert(@RequestHeader String token,
                               @RequestBody VehicleDTO vehicleDTO) {
        accountService.check(token, RoleEnum.OPERATOR);
        return new HttpResponse(vehicleService.insert(vehicleDTO));
    }

    @RequestMapping(value = "/vehicle/", method = RequestMethod.PUT)
    public HttpResponse update(@RequestHeader String token,
                               @RequestBody VehicleDTO vehicleDTO) {
        accountService.check(token, RoleEnum.OPERATOR);
        vehicleService.update(vehicleDTO);
        return new HttpResponse();
    }

    @RequestMapping(value = "/vehicle/{id}/", method = RequestMethod.DELETE)
    public HttpResponse delete(@RequestHeader String token,
                               @PathVariable int id) {
        accountService.check(token, RoleEnum.OPERATOR);
        if ( true ) {
            vehicleService.update(id, StatusEnum.DELETE.getStatus());
        } else {
            vehicleService.delete(id);
        }
        return new HttpResponse();
    }


}
