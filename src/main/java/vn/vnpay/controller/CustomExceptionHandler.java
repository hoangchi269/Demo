package vn.vnpay.controller;

import org.aspectj.apache.bcel.classfile.Code;
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

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Message messages = new Message();
        messages.setMessage(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        messages.setCode(Common.Code.CODE_01.getCode());
        return ResponseEntity.ok(messages);
    }
}
