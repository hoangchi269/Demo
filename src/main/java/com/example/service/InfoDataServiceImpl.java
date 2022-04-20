package com.example.service;

import com.example.common.Common;
import com.example.entity.ConfigBanks;
import com.example.entity.InfoData;
import com.example.entity.Message;
import com.google.common.hash.Hashing;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InfoDataServiceImpl implements InfoDataService {

    @Autowired
    private ConfigBanks configBanks;
    private static final Logger logger = LogManager.getLogger(InfoDataServiceImpl.class);

    @Override
    public Message checkBanksCode(InfoData infoData) {

        Message message = new Message();
        List<ConfigBanks.Bank> configBanksList =  configBanks.getBanks().stream().filter(bank -> bank.getBankCode().equals(infoData.getBankCode()))
                .collect(Collectors.toList());
        if (configBanksList.size() <= 0) {
            message.setCode(Common.CODE_02);
            message.setMessage("BankCode find not found!");
        }else {
            String checkSum = "";
            checkSum =  infoData.getMobile() + infoData.getBankCode() + infoData.getAccountNo() + infoData.getPayDate()
                    + infoData.getDebitAmount() + infoData.getRespCode() + infoData.getTraceTransfer() + infoData.getMessageType()
                     + configBanksList.get(0).getPrivateKey();
            String sha256hex = Hashing.sha256()
                    .hashString(checkSum, StandardCharsets.UTF_8)
                    .toString();
            if (! sha256hex.trim().equals(infoData.getCheckSum())) {
                message.setCode(Common.CODE_03);
                message.setMessage("CheckSum error");
            }else {
                message.setMessage(Common.SUCCESS);
                message.setCode(Common.CODE_00);
            }
        }
        return message;
    }
}
