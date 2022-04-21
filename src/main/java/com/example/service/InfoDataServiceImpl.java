package com.example.service;

import com.example.common.Common;
import com.example.entity.ConfigBanks;
import com.example.entity.InfoData;
import com.example.entity.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

    final RedisTemplate<Object, Object> redisTemplate;
    private final ConfigBanks configBanks;
    private static final Logger logger = LogManager.getLogger(InfoDataServiceImpl.class);

    public InfoDataServiceImpl(ConfigBanks configBanks, RedisTemplate redisTemplate) {
        logger.info("Anh la so 2");
        this.configBanks = configBanks;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Message checkBanksCode(InfoData infoData) {

        Message message = new Message();
        List<ConfigBanks.Bank> configBanksList = configBanks.getBanks().stream().filter(bank -> bank.getBankCode().equals(infoData.getBankCode()))
                .collect(Collectors.toList());
        if (configBanksList.size() <= 0) {
            message.setCode(Common.CODE_02);
            message.setMessage("BankCode find not found!");
        } else {
            String checkSum = infoData.getMobile() + infoData.getBankCode() + infoData.getAccountNo() + infoData.getPayDate()
                    + infoData.getDebitAmount() + infoData.getRespCode() + infoData.getTraceTransfer() + infoData.getMessageType()
                    + configBanksList.get(0).getPrivateKey();
            String sha256hex = Hashing.sha256()
                    .hashString(checkSum, StandardCharsets.UTF_8)
                    .toString();
            if (!sha256hex.trim().equals(infoData.getCheckSum())) {
                message.setCode(Common.CODE_03);
                message.setMessage("CheckSum error");
            } else {

                HashOperations hashOperations = redisTemplate.opsForHash();

                ObjectMapper mapper = new ObjectMapper();
                String jsonInfoData = "";
                try {
                    jsonInfoData = mapper.writeValueAsString(infoData);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                hashOperations.put(infoData.getBankCode(), infoData.getTokenKey(), jsonInfoData);
                redisTemplate.setKeySerializer(new StringRedisSerializer());
                redisTemplate.setValueSerializer(new StringRedisSerializer());
                redisTemplate.setHashKeySerializer(new StringRedisSerializer());
                redisTemplate.setHashValueSerializer(new StringRedisSerializer());
                redisTemplate.afterPropertiesSet();


                message.setMessage(Common.SUCCESS);
                message.setCode(Common.CODE_00);
            }
        }
        return message;
    }
}
