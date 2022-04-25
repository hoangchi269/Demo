package vn.vnpay.controller;

import lombok.extern.slf4j.Slf4j;
import vn.vnpay.bean.InfoData;
import vn.vnpay.bean.Message;
import vn.vnpay.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class InfoDataController {

    private final PaymentService infoDataService;
    public InfoDataController(PaymentService infoDataService) {
        this.infoDataService = infoDataService;
    }

    @PostMapping("/pay")
    public ResponseEntity<?> pay(@RequestBody @Valid InfoData infoData) {
        Message message = infoDataService.pay(infoData);
        log.info("-------------End pay-------------");
        return ResponseEntity.ok(message);
    }
}
