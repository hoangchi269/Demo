package com.example.service;

import com.example.common.Common;
import com.example.entity.ConfigBanks;
import com.example.entity.InfoData;
import com.example.entity.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InfoDataServiceImpl implements InfoDataService, Serializable {

    private final Message message;
    private final ConfigBanks configBanks;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<Object, Object> redisTemplate;

    public InfoDataServiceImpl(ConfigBanks configBanks, RedisTemplate<Object, Object> redisTemplate, Message message, ObjectMapper objectMapper) {
        this.message = message;
        this.objectMapper = objectMapper;
        this.configBanks = configBanks;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Message checkBanksCode(InfoData infoData) {
        getConfigBank(infoData);
        if (checkBankExistInConfig(infoData)) {
            return bankCodeIsExist();
        }
        checkSumAndHashString(infoData);
        if (!isEqualCheckSum(infoData)) {
            return getMessageCheckSumError();
        }
        putHsetDataToRedisAndSetSerializer(infoData);
        return getMessageSuccess();
    }

    public Boolean checkBankExistInConfig(InfoData infoData) {
        return getConfigBank(infoData).size() <= 0;
    }

    public List<ConfigBanks.Bank> getConfigBank(InfoData infoData) {
        return configBanks.getBanks().stream()
                .filter(bank -> bank.getBankCode().equals(infoData.getBankCode()))
                .collect(Collectors.toList());
    }

    public Boolean isEqualCheckSum(InfoData infoData) {
        return checkSumAndHashString(infoData).trim().equals(infoData.getCheckSum());
    }

    public String checkSumAndHashString(InfoData infoData) {
        String checkSum = infoData.getMobile()
                + infoData.getBankCode()
                + infoData.getAccountNo()
                + infoData.getPayDate()
                + infoData.getDebitAmount()
                + infoData.getRespCode()
                + infoData.getTraceTransfer()
                + infoData.getMessageType()
                + getConfigBank(infoData).get(0).getPrivateKey();
        return hashStringCheckSum(checkSum);
    }

    public String hashStringCheckSum(String checkSum) {
        return Hashing.sha256()
                .hashString(checkSum, StandardCharsets.UTF_8)
                .toString();
    }

    public void putHsetDataToRedisAndSetSerializer(InfoData infoData) {
        putHsetData(infoData);
        setSerializerRedisTemplate();
    }

    public void putHsetData(InfoData infoData) {
        try {
            HashOperations<Object, Object, Object> hashOperations = redisTemplate.opsForHash();
            String jsonInfoData = objectMapper.writeValueAsString(infoData);
            hashOperations.put(infoData.getBankCode(), infoData.getTokenKey(), jsonInfoData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public void setSerializerRedisTemplate() {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
    }

    public Message bankCodeIsExist() {
        message.setCode(Common.CODE_02);
        message.setMessage("BankCode find not found!");
        return message;
    }

    public Message getMessageSuccess() {
        message.setCode(Common.CODE_00);
        message.setMessage(Common.SUCCESS);
        return message;
    }

    public Message getMessageCheckSumError() {
        message.setCode(Common.CODE_03);
        message.setMessage("CheckSum error");
        return message;
    }

}

