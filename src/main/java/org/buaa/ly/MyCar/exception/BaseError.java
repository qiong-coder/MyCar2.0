package org.buaa.ly.MyCar.exception;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.buaa.ly.MyCar.http.ResponseStatusMsg;

@Data
@EqualsAndHashCode(callSuper = false)
public class BaseError extends RuntimeException {

    private int status;

    public BaseError(int status, String message) {
        super(message);
        this.status = status;
    }

    public BaseError(ResponseStatusMsg responseStatusMsg) {
        super(responseStatusMsg.getMessage());
        this.status = responseStatusMsg.getStatus();
    }

    public BaseError() {
        this(ResponseStatusMsg.ERROR);
    }


}
