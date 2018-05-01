package org.buaa.ly.MyCar.http;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.buaa.ly.MyCar.exception.BaseError;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class HttpResponse {

    int status;

    String message;

    Object data;

    HttpResponse(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public HttpResponse(Object data) {
        this(0, "success", data);
    }

    public HttpResponse(BaseError error) {
        this(error.getStatus(), error.getMessage(), null);
    }

    public HttpResponse() { this(ResponseStatusMsg.SUCCESS.getStatus(), ResponseStatusMsg.SUCCESS.getMessage(),null); }

    public static HttpResponse buildErrorResponse() {
        return new HttpResponse(new BaseError());
    }
}
