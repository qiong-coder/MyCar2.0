package org.buaa.ly.MyCar.config;


import com.alibaba.fastjson.JSON;
import org.buaa.ly.MyCar.exception.LoginError;
import org.buaa.ly.MyCar.exception.MustLoginError;
import org.buaa.ly.MyCar.http.HttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@Component("restAuthenticationEntryPoint")
public class RestAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    @Override
    public void commence(final HttpServletRequest request,
                         final HttpServletResponse response,
                         final AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName() + "");

        PrintWriter writer = response.getWriter();
        if ( request.getRequestURI().contains("login") ) writer.println(JSON.toJSONString(new HttpResponse(new LoginError())));
        else writer.println(JSON.toJSONString(new HttpResponse(new MustLoginError())));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName(SecurityConfig.REALM);
        super.afterPropertiesSet();
    }

}
