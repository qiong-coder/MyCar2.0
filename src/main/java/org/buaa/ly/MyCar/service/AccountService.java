package org.buaa.ly.MyCar.service;

import org.buaa.ly.MyCar.http.dto.AccountDTO;
import org.buaa.ly.MyCar.utils.RoleEnum;
import org.springframework.security.access.annotation.Secured;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public interface AccountService {

    void check(String token, RoleEnum role);

    AccountDTO login(String username, String password);

    void logout(String token);

    AccountDTO insert(AccountDTO accountDTO);

    AccountDTO find(String username);

    AccountDTO update(AccountDTO accountDTO);

    void delete(String username);

}
