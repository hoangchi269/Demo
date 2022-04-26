package vn.vnpay.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.vnpay.bean.Message;
import vn.vnpay.bean.TransactionRequest;
import vn.vnpay.common.Common.ResponeCode;
import vn.vnpay.service.PaymentService;

import javax.validation.Valid;

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
        ResponeCode responeCode = paymentService.pay(transactionRequest);
        Message message  = new Message();
        message.setCode(responeCode.getCode());
        message.setMessage(responeCode.getMessage());
        log.info("-------------End pay-------------");
        return ResponseEntity.ok(message);
    }
}
