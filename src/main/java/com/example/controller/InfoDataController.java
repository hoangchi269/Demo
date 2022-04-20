package com.example.controller;

import com.example.common.Common;
import com.example.entity.InfoData;
import com.example.entity.Message;
import com.example.service.InfoDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class InfoDataController {

    @Autowired
    InfoDataService infoDataService;

    @Autowired
    Message message;

    @PostMapping("/bankCode")
    public ResponseEntity<?> bankCode(@RequestBody InfoData infoData) {

        message = infoDataService.message(infoData);
        if (message.getCode().equals(Common.CODE_00)) {
            message = infoDataService.checkBanksCode(infoData);
            return ResponseEntity.ok(message);
        }else {
            return ResponseEntity.ok(message);
        }

    }
}
