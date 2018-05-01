package org.buaa.ly.MyCar.action;

import org.buaa.ly.MyCar.http.HttpResponse;
import org.buaa.ly.MyCar.service.AccountService;
import org.buaa.ly.MyCar.service.UploadService;
import org.buaa.ly.MyCar.utils.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

@RestController
@RequestMapping("/files")
public class UploadAction {

    @Autowired private AccountService accountService;

    @Autowired private UploadService uploadService;


    @RequestMapping(value = "/{type}/{id}/", method = RequestMethod.GET)
    public HttpResponse find(HttpServletRequest request,
                             @PathVariable String type,
                             @PathVariable String id) {
        accountService.check(request, RoleEnum.USER);

        return new HttpResponse(uploadService.find(type, id));
    }

    @RequestMapping(value = "/{type}/{id}/", method = RequestMethod.POST)
    public HttpResponse save(@RequestPart Part attachment,
                               @PathVariable String id,
                               @PathVariable String type) {
        String dir = uploadService.save(type, id, attachment);
        return new HttpResponse(dir);
    }


}
