package org.buaa.ly.MyCar.http.dto;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.buaa.ly.MyCar.entity.Account;

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

    public Account build() {
        return build(this, Account.class);
    }

    static public AccountDTO build(Account account) {
        return build(account, AccountDTO.class);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
