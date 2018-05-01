package org.buaa.ly.MyCar.exception.advice;

import org.buaa.ly.MyCar.exception.BaseError;
import org.buaa.ly.MyCar.http.HttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class DefaultAdvice {

    @ExceptionHandler(BaseError.class)
    public HttpResponse defaultHandler(BaseError error) {
        return new HttpResponse(error);
    }

    @ExceptionHandler(IOException.class)
    public HttpResponse ioHandler(IOException error) { return new HttpResponse(error); }
}
