package se.casparsylwan.SBAB.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<Object> handlerApiRequestException(ApiRequestException e){

        HttpStatus httpStatus = HttpStatus.BAD_GATEWAY;

        ApiException apiException = new ApiException(
                e.getMessage(),
                httpStatus);

        return new ResponseEntity<>(apiException, httpStatus);
    }
}
