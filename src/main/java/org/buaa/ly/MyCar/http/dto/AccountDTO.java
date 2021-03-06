package org.buaa.ly.MyCar.http.dto;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.buaa.ly.MyCar.entity.Account;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDTO extends DTOBase {

    Integer id;

    String username;

    @JSONField(deserialize = false)
    String password;

    String name;

    String phone;

    Integer role;

    Integer sid;

    Integer status;

    String token;

    @JsonProperty(value = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    Timestamp createTime;

    public Account build() {
        return build(this, Account.class);
    }

    static public AccountDTO build(Account account) {
        return build(account, AccountDTO.class);
    }

    static public List<AccountDTO> build(Collection<Account> accounts) {
        return build(accounts, AccountDTO.class);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
