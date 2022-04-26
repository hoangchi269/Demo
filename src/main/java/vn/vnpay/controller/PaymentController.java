package vn.vnpay.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.vnpay.bean.Message;
import vn.vnpay.bean.TransactionRequest;
import vn.vnpay.common.Common.ResponseCode;
import vn.vnpay.config.Snowflake;
import vn.vnpay.service.PaymentService;

import javax.validation.Valid;

import static vn.vnpay.common.Common.SNOWFLAKE;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class PaymentController {

    private final PaymentService paymentService;
    public PaymentController(PaymentService infoDataService) {
        this.paymentService = infoDataService;
    }

    @PostMapping("/pay")
    public ResponseEntity<?> pay(@RequestBody @Valid TransactionRequest transactionRequest) {
        ResponseCode responseCode;
        try {
            responseCode = paymentService.pay(transactionRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Message message  = new Message();
        message.setCode(responseCode.getCode());
        message.setMessage(responseCode.getMessage());
        log.info("Message : {}  [{}]", message , SNOWFLAKE);
        log.info("End pay with BankCode: {}  [{}]", transactionRequest.getBankCode(), SNOWFLAKE);
        return ResponseEntity.ok(message);
    }
}
