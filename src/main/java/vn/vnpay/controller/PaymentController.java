package vn.vnpay.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.vnpay.bean.Message;
import vn.vnpay.bean.TransactionRequest;
import vn.vnpay.common.Common.ResponseCode;
import vn.vnpay.service.PaymentService;

import javax.validation.Valid;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class PaymentController {

    private final PaymentService paymentService;

    private final ObjectMapper objectMapper;
    public PaymentController(PaymentService infoDataService, ObjectMapper objectMapper) {
        this.paymentService = infoDataService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/pay")
    public ResponseEntity<?> pay(@RequestBody @Valid TransactionRequest transactionRequest) throws JsonProcessingException {
        log.info("Begin pay with request: {}", objectMapper.writeValueAsString(transactionRequest));
        String uuid = UUID.randomUUID().toString().toUpperCase().replace("-", "");
        MDC.put("id", uuid);
        ResponseCode responseCode;
        try {
            responseCode = paymentService.pay(transactionRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Message message  = new Message();
        message.setCode(responseCode.getCode());
        message.setMessage(responseCode.getMessage());
        log.info("Message : {}", message);
        log.info("End pay with BankCode: {}", transactionRequest.getBankCode());
        return ResponseEntity.ok(message);
    }
}
