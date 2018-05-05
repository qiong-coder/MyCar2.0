package org.buaa.ly.MyCar.action;


import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.http.HttpResponse;
import org.buaa.ly.MyCar.http.dto.AccountDTO;
import org.buaa.ly.MyCar.service.AccountService;
import org.buaa.ly.MyCar.utils.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping(value = "/account")
@Slf4j
public class AccountAction {

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/login/", method = RequestMethod.POST)
    public HttpResponse login(@RequestBody AccountDTO accountDTO) {
        return new HttpResponse(accountService.login(accountDTO.getUsername(), accountDTO.getPassword()));
    }


    @RequestMapping(value = "/logout/", method = RequestMethod.PUT)
    public HttpResponse logout(@RequestHeader String token) {
        accountService.logout(token);
        return new HttpResponse();
    }

    @RequestMapping(value = "/register/", method = RequestMethod.POST)
    public HttpResponse insert(@RequestHeader String token,
                               @RequestBody AccountDTO accountDTO)
    {
        accountService.check(token, RoleEnum.ADMINISTRATOR);

        accountService.insert(accountDTO);

        return new HttpResponse();
    }

    @RequestMapping(value = "/{username}/", method = RequestMethod.GET)
    public HttpResponse find(@RequestHeader String token,
                             @PathVariable String username) {
        accountService.check(token, RoleEnum.OPERATOR);

        return new HttpResponse(accountService.find(username));
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public HttpResponse update(@RequestHeader String token,
                               @RequestBody AccountDTO accountDTO)
    {
        accountService.check(token, RoleEnum.ADMINISTRATOR);

        return new HttpResponse(accountService.update(accountDTO));
    }

    @RequestMapping(value = "/{username}/", method = RequestMethod.DELETE)
    public HttpResponse delete(@RequestHeader String token,
                               @PathVariable String username)
    {
        accountService.check(token, RoleEnum.ADMINISTRATOR);

        accountService.delete(username);

        return new HttpResponse();
    }


}
