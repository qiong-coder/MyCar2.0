package org.buaa.ly.MyCar.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.buaa.ly.MyCar.http.ResponseStatusMsg;

@Data
@EqualsAndHashCode(callSuper = false)
public class AccountNonFoundError extends BaseError {

    private static int status = -12;

    public AccountNonFoundError(String username) {
        super(status, String.format("failure to find the account - %s", username));
    }

    public AccountNonFoundError() {
        super(ResponseStatusMsg.ACCOUNT_LOGIN_ERROR);
    }

}
