package org.buaa.ly.MyCar.repository;

import org.buaa.ly.MyCar.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;


public interface AccountRepository extends JpaRepository<Account, Integer>,
        JpaSpecificationExecutor<Account>,
        QuerydslPredicateExecutor<Account> {

    Account findByUsername(String username);

}
