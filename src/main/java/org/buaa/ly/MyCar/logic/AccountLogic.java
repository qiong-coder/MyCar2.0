package org.buaa.ly.MyCar.logic;

import org.buaa.ly.MyCar.entity.Account;

import java.util.List;

public interface AccountLogic {

    Account find(String username);

    List<Account> findAll();

    Account insert(Account account);

    Account update(Account account);

    Account delete(String username);

    Account setStatus(String username, int status);
}
