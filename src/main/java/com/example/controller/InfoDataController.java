package com.example.controller;

import com.example.entity.InfoData;
import com.example.entity.Message;
import com.example.service.InfoDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class InfoDataController {

    final InfoDataService infoDataService;
    public InfoDataController(InfoDataService infoDataService) {
        this.infoDataService = infoDataService;
    }

    @PostMapping("/bankCode")
    public ResponseEntity<?> bankCode(@RequestBody @Valid InfoData infoData) {
        Message message = infoDataService.checkBanksCode(infoData);
        return ResponseEntity.ok(message);
    }
}
