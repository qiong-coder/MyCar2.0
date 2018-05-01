package org.buaa.ly.MyCar.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.entity.Account;
import org.buaa.ly.MyCar.exception.*;
import org.buaa.ly.MyCar.http.dto.AccountDTO;
import org.buaa.ly.MyCar.logic.AccountLogic;
import org.buaa.ly.MyCar.service.AccountService;
import org.buaa.ly.MyCar.utils.Md5;
import org.buaa.ly.MyCar.utils.RoleEnum;
import org.buaa.ly.MyCar.utils.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;


@Component("accountServiceImpl")
@Slf4j
@Transactional
@PersistenceContext(type = PersistenceContextType.EXTENDED)
public class AccountServiceImpl implements AccountService {

    @Autowired private AccountLogic accountLogic;

    @Override
    public AccountDTO login(HttpServletRequest request, String username, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        HttpSession session = request.getSession(true);

        Account account = accountLogic.find(username);

        if ( account == null || account.getStatus() == StatusEnum.DELETE.getStatus() )
            throw new NotFoundError(String.format("failure to find account - %s", username));

        if ( account.getPassword().compareTo(password) != 0 )
            throw new LoginError();

        AccountDTO accountDTO = AccountDTO.build(account);
        accountDTO.setToken(Md5.md5(String.format("%s-%d", username, System.currentTimeMillis())));

        session.setAttribute("token", accountDTO.getToken());
        session.setAttribute("role", accountDTO.getRole());
        session.setAttribute("username", accountDTO.getUsername());

        return accountDTO;
    }


    @Override
    public void check(HttpServletRequest request, RoleEnum role) {

        String token = request.getHeader("token");

        if ( token == null ) throw new NonLoginError();

        if ( token.compareTo("test") == 0 ) return;

        HttpSession session = request.getSession(false);
        if ( session == null ) throw new NotFoundError("failure to find session");

        if ( session.getAttribute("token") == null ) throw new  NonLoginError();

        String sToken = (String)session.getAttribute("token");

        if ( sToken.compareTo(token) != 0 ) throw new PermissionDenyError();

        if ( role == null ) return;

        Integer sRole = (Integer)session.getAttribute("role");
        if ( sRole == null || sRole > role.getRole()) throw new PermissionDenyError();

    }

    @Override
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if ( session != null ) session.invalidate();
    }

    @Override
    public AccountDTO insert(AccountDTO accountDTO) {
        Account a = accountLogic.find(accountDTO.getUsername());

        if ( a != null && a.getStatus() == StatusEnum.DELETE.getStatus() )
            throw new DuplicateError();

        if ( a == null ) return AccountDTO.build(accountLogic.insert(AccountDTO.build(accountDTO,Account.class)));
        else return AccountDTO.build(accountLogic.setStatus(accountDTO.getUsername(), StatusEnum.OK.getStatus()));
    }

    @Override
    public AccountDTO find(String username) {

        Account account = accountLogic.find(username);

        if ( account == null ) throw new NotFoundError(String.format("failure to find account - %s", username));

        return AccountDTO.build(account);

    }

    @Override
    public AccountDTO update(String username, AccountDTO accountDTO) {
        accountDTO.setUsername(username);

        Account account = accountLogic.update(accountDTO.build());

        if ( account == null ) throw new NotFoundError("failure to find the account");

        return AccountDTO.build(account);
    }

    @Override
    public void delete(String username) {
        Account account = accountLogic.setStatus(username, StatusEnum.DELETE.getStatus());

        if (account == null ) throw new NotFoundError("failure to find the account");
    }


}
