package vn.vnpay.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import vn.vnpay.common.Common;
import vn.vnpay.bean.Message;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.UUID;


@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String uuid = UUID.randomUUID().toString().toUpperCase().replace("-", "");
        MDC.put("id", uuid);
        Message messages = new Message();
        messages.setMessage(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        messages.setCode(Common.ResponseCode.INVALID_REQUEST.getCode());
        log.error("Method Argument Not Valid : {}", messages);
        return ResponseEntity.ok(messages);
    }
}
