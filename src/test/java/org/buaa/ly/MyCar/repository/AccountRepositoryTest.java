package org.buaa.ly.MyCar.repository;

import com.google.common.collect.Lists;
import org.buaa.ly.MyCar.config.TestLoader;
import org.buaa.ly.MyCar.entity.Account;
import org.buaa.ly.MyCar.entity.QAccount;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

public class AccountRepositoryTest extends TestLoader {

    @Autowired private AccountRepository accountRepository;


    @Test
    public void testQueryDsl() {

        QAccount account = QAccount.account;

        List<Account> accountList = Lists.newArrayList(accountRepository.findAll(account.username.eq("admin")));
        assert(accountList.size() == 1);

        accountList = Lists.newArrayList(accountRepository.findAll(account.store.id.eq(1)));
        assert(accountList.size() == 1);

    }

    @Test
    public void testUpdate() {
        Account account = accountRepository.findByUsername("admin");

        assert(account.getSid() == 2);

    }

}
