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

    @Autowired
    private Message message;

    @Override
    public Message message(InfoData infoData) {
        if (infoData.getTokenKey().isBlank() || infoData.getTokenKey().isEmpty()) {
            message.setMessage("TokenKey" + Common.NULL_OR_BLANK);
            logger.error("TokenKey" + Common.NULL_OR_BLANK);
        } else if (infoData.getApiID().isBlank() || infoData.getApiID().isEmpty()) {
            message.setMessage("ApiID" + Common.NULL_OR_BLANK);
            logger.error("ApiID" + Common.NULL_OR_BLANK);
        } else if (infoData.getMobile().isBlank() || infoData.getMobile().isEmpty()) {
            message.setMessage("Mobile" + Common.NULL_OR_BLANK);
            logger.error("Mobile" + Common.NULL_OR_BLANK);
        } else if (infoData.getBankCode().isBlank() || infoData.getBankCode().isEmpty()) {
            message.setMessage("BankCode" + Common.NULL_OR_BLANK);
            logger.error("BankCode" + Common.NULL_OR_BLANK);
        } else if (infoData.getAccountNo().isBlank() || infoData.getAccountNo().isEmpty()) {
            message.setMessage("AccountNo" + Common.NULL_OR_BLANK);
            logger.error("AccountNo" + Common.NULL_OR_BLANK);
        } else if (infoData.getPayDate().isBlank() || infoData.getPayDate().isEmpty()) {
            message.setMessage("PayDate" + Common.NULL_OR_BLANK);
            logger.error("PayDate" + Common.NULL_OR_BLANK);
        } else if (infoData.getAdditionalData().isBlank() || infoData.getAdditionalData().isEmpty()) {
            message.setMessage("AdditionalData" + Common.NULL_OR_BLANK);
            logger.error("AdditionalData" + Common.NULL_OR_BLANK);
        } else if (infoData.getDebitAmount().isBlank() || infoData.getDebitAmount().isEmpty()) {
            message.setMessage("DebitAmount" + Common.NULL_OR_BLANK);
            logger.error("DebitAmount" + Common.NULL_OR_BLANK);
        } else if (infoData.getRespCode().isBlank() || infoData.getRespCode().isEmpty()) {
            message.setMessage("RespCode" + Common.NULL_OR_BLANK);
            logger.error("RespCode" + Common.NULL_OR_BLANK);
        } else if (infoData.getRespDesc().isBlank() || infoData.getRespDesc().isEmpty()) {
            message.setMessage("RespDesc" + Common.NULL_OR_BLANK);
            logger.error("RespDesc" + Common.NULL_OR_BLANK);
        } else if (infoData.getTraceTransfer().isBlank() || infoData.getTraceTransfer().isEmpty()) {
            message.setMessage("TraceTransfer" + Common.NULL_OR_BLANK);
            logger.error("TraceTransfer" + Common.NULL_OR_BLANK);
        } else if (infoData.getMessageType().isBlank() || infoData.getMessageType().isEmpty()) {
            message.setMessage("MessageType" + Common.NULL_OR_BLANK);
            logger.error("MessageType" + Common.NULL_OR_BLANK);
        } else if (infoData.getCheckSum().isBlank() || infoData.getCheckSum().isEmpty()) {
            message.setMessage("CheckSum" + Common.NULL_OR_BLANK);
            logger.error("CheckSum" + Common.NULL_OR_BLANK);
        } else if (infoData.getOrderCode().isBlank() || infoData.getOrderCode().isEmpty()) {
            message.setMessage("OrderCode" + Common.NULL_OR_BLANK);
            logger.error("OrderCode" + Common.NULL_OR_BLANK);
        } else if (infoData.getUserName().isBlank() || infoData.getUserName().isEmpty()) {
            message.setMessage("UserName" + Common.NULL_OR_BLANK);
            logger.error("UserName" + Common.NULL_OR_BLANK);
        } else if (infoData.getRealAmount().isBlank() || infoData.getRealAmount().isEmpty()) {
            message.setMessage("RealAmount" + Common.NULL_OR_BLANK);
            logger.error("RealAmount" + Common.NULL_OR_BLANK);
        } else if (infoData.getPromotionCode().isBlank() || infoData.getPromotionCode().isEmpty()) {
            message.setMessage("PromotionCode" + Common.NULL_OR_BLANK);
            logger.error("PromotionCode" + Common.NULL_OR_BLANK);
        } else {
            logger.info("Pass check null or blank");
            message.setCode(Common.CODE_00);
            return message;
        }

        if (!message.getMessage().isEmpty()) {
            message.setCode(Common.CODE_01);
        }
        return message;
    }

    @Override
    public Message checkBanksCode(InfoData infoData) {

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
