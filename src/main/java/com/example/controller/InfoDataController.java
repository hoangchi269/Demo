package com.example.controller;

import com.example.bean.InfoData;
import com.example.bean.Message;
import com.example.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class InfoDataController {

    private final PaymentService infoDataService;
    public InfoDataController(PaymentService infoDataService) {
        this.infoDataService = infoDataService;
    }

    @PostMapping("/bankCode")
    public ResponseEntity<?> bankCode(@RequestBody @Valid InfoData infoData) {
        Message message = infoDataService.pay(infoData);
        return ResponseEntity.ok(message);
    }
}
