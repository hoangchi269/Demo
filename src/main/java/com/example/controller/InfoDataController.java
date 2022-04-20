package com.example.controller;

import com.example.common.Common;
import com.example.entity.InfoData;
import com.example.entity.Message;
import com.example.service.InfoDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class InfoDataController {

    @Autowired
    InfoDataService infoDataService;

    @PostMapping("/bankCode")
    public ResponseEntity<?> bankCode(@RequestBody @Valid InfoData infoData) {
        Message message = infoDataService.checkBanksCode(infoData);
        return ResponseEntity.ok(message);
    }
}
