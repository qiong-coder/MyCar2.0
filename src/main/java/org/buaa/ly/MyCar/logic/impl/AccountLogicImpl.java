package org.buaa.ly.MyCar.logic.impl;

import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.entity.Account;
import org.buaa.ly.MyCar.logic.AccountLogic;
import org.buaa.ly.MyCar.repository.AccountRepository;
import org.buaa.ly.MyCar.utils.BeanCopyUtils;
import org.hibernate.JDBCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Component("accountLogic")
@Slf4j
@Transactional
public class AccountLogicImpl implements AccountLogic {

    private AccountRepository accountRepository;

    @Autowired
    void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account find(String username) {
        return accountRepository.findByUsername(username);
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account insert(Account account) {
        return accountRepository.save(account);
    }

    @Modifying
    @Override
    public Account update(Account account) {
        Account a = find(account.getUsername());

        if ( a == null ) return null;
        else BeanCopyUtils.copyPropertiesIgnoreNull(account,a);

        return a;
    }

    @Override
    public Account delete(String username) {
        Account a = find(username);

        if ( a == null ) return null;
        else accountRepository.deleteById(a.getId());

        return a;
    }

    @Modifying
    @Override
    public Account setStatus(String username, int status) {
        Account account = find(username);

        if ( account == null ) return null;
        else account.setStatus(status);

        return account;
    }
}
