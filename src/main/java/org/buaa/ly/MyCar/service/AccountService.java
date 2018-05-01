package org.buaa.ly.MyCar.service;

import org.buaa.ly.MyCar.http.dto.AccountDTO;
import org.buaa.ly.MyCar.utils.RoleEnum;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public interface AccountService {

    void check(HttpServletRequest request, RoleEnum role);

    AccountDTO login(HttpServletRequest request, String username, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException;

    void logout(HttpServletRequest request);

    AccountDTO insert(AccountDTO accountDTO);

    AccountDTO find(String username);

    AccountDTO update(String username, AccountDTO accountDTO);

    void delete(String username);

}
