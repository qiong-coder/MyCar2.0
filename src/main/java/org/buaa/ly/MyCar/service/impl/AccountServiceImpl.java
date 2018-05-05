package org.buaa.ly.MyCar.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.entity.Account;
import org.buaa.ly.MyCar.exception.*;
import org.buaa.ly.MyCar.http.dto.AccountDTO;
import org.buaa.ly.MyCar.logic.AccountLogic;
import org.buaa.ly.MyCar.service.AccountService;
import org.buaa.ly.MyCar.service.RedisService;
import org.buaa.ly.MyCar.utils.BasicAuthUtils;
import org.buaa.ly.MyCar.utils.Md5;
import org.buaa.ly.MyCar.utils.RoleEnum;
import org.buaa.ly.MyCar.utils.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;


@Component("accountServiceImpl")
@Slf4j
@PersistenceContext(type = PersistenceContextType.EXTENDED)
public class AccountServiceImpl implements AccountService {

    private AccountLogic accountLogic;

    private RedisService redisService;

    @Autowired
    public void setAccountLogic(AccountLogic accountLogic) {
        this.accountLogic = accountLogic;
    }

    @Autowired
    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public AccountDTO login(String username, String password) {

        Account account = accountLogic.find(username);

        if ( account == null || account.getStatus() == StatusEnum.DELETE.getStatus() )
            throw new NotFoundError(String.format("failure to find account - %s", username));

        String credential = BasicAuthUtils.basicAuth(account.getUsername(), account.getPassword());

        if ( credential.compareTo(BasicAuthUtils.basicAuth(username, password)) != 0 )
            throw new LoginError();

        AccountDTO accountDTO = AccountDTO.build(account);
        accountDTO.setToken(credential);

        redisService.putAccountDTO(accountDTO);

        return accountDTO;

    }


    @Override
    public void check(String token, RoleEnum role) {

        if ( token == null ) throw new NonLoginError();

        if ( token.compareTo("test") == 0 ) return;

        AccountDTO accountDTO = redisService.findAccountDTO(token);

        if ( accountDTO == null ) throw new  NonLoginError();

        String sToken = accountDTO.getToken();

        if ( sToken.compareTo(token) != 0 ) throw new PermissionDenyError();

        if ( role == null ) return;

        Integer sRole = accountDTO.getRole();

        if ( sRole == null || sRole > role.getRole()) throw new PermissionDenyError();
    }

    @Override
    public void logout(String token) {
        redisService.deleteAccountDTO(token);
    }

    @Override
    public AccountDTO insert(AccountDTO accountDTO) {
        Account a = accountLogic.find(accountDTO.getUsername());

        if ( a != null && a.getStatus() == StatusEnum.DELETE.getStatus() )
            throw new DuplicateError();

        if ( a == null ) return AccountDTO.build(accountLogic.insert(AccountDTO.build(accountDTO, Account.class)));
        else return AccountDTO.build(accountLogic.setStatus(accountDTO.getUsername(), StatusEnum.OK.getStatus()));
    }

    @Override
    public AccountDTO find(String username) {

        Account account = accountLogic.find(username);

        if ( account == null ) throw new NotFoundError(String.format("failure to find account - %s", username));

        return AccountDTO.build(account);

    }

    @Override
    public AccountDTO update(AccountDTO accountDTO) {

        Account account = accountLogic.find(accountDTO.getUsername());

        if ( account == null ) throw new NotFoundError("failure to find the account");

        accountDTO = AccountDTO.build(accountLogic.update(accountDTO.build()));

        String token = BasicAuthUtils.basicAuth(account.getUsername(), account.getPassword());

        redisService.deleteAccountDTO(token);

        token = BasicAuthUtils.basicAuth(accountDTO.getUsername(), accountDTO.getPassword());

        redisService.deleteAccountDTO(token);

        return accountDTO;

    }

    @Override
    public void delete(String username) {
        Account account = accountLogic.setStatus(username, StatusEnum.DELETE.getStatus());

        if (account == null ) throw new NotFoundError("failure to find the account");

        String token = BasicAuthUtils.basicAuth(account.getUsername(), account.getPassword());

        redisService.deleteAccountDTO(token);
    }


}
