package org.buaa.ly.MyCar.action;


import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.http.HttpResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value = "/user")
public class UserAction {


    //@Autowired private UserService userService;


    @RequestMapping(value = "/order/{identity}/{phone}/", method = RequestMethod.GET)
    public HttpResponse find(@PathVariable String identity,
                             @PathVariable String phone) {
        //List<Order>
        return null;
    }

}
