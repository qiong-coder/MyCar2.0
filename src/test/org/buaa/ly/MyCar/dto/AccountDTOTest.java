package org.buaa.ly.MyCar.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.buaa.ly.MyCar.config.TestLoader;
import org.buaa.ly.MyCar.entity.Account;
import org.buaa.ly.MyCar.entity.Store;
import org.buaa.ly.MyCar.http.dto.AccountDTO;
import org.junit.Test;

@Slf4j
public class AccountDTOTest extends TestLoader {

    @Test
    public void testDTO() {

        Account account = new Account();

        account.setName("test");

        Store store = new Store();

        store.setId(1);

        account.setStore(store);

        log.info("account:{}", JSONObject.toJSONString(account));

        AccountDTO accountDTO = AccountDTO.build(account, AccountDTO.class);

        log.info("accountDTO:{}", JSONObject.toJSONString(accountDTO));

        account = JSONObject.parseObject(JSONObject.toJSONString(accountDTO), Account.class);

        assert(account.getStore().getId()==1);
    }



}
