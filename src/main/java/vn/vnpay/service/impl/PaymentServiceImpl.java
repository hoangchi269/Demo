package vn.vnpay.service.impl;

import lombok.extern.slf4j.Slf4j;
import vn.vnpay.bean.InfoData;
import vn.vnpay.bean.Message;
import vn.vnpay.config.ConfigBanks;
import vn.vnpay.service.PaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import vn.vnpay.common.Common;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService, Serializable {
    private final ConfigBanks configBanks;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<Object, Object> redisTemplate;

    public PaymentServiceImpl(
            ConfigBanks configBanks,
            RedisTemplate<Object, Object> redisTemplate,
            ObjectMapper objectMapper
    ) {
        this.objectMapper = objectMapper;
        this.configBanks = configBanks;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Message pay(InfoData infoData) {
        log.info("-------------Start pay-------------");
        getConfigBank(infoData);
        if (!checkBankExistInConfig(infoData)) {
            return Common.getMessageBankCodeExist();
        }
        checkSumAndHashString(infoData);
        if (!isEqualCheckSum(infoData)) {
            return Common.getMessageCheckSumError();
        }
        cachePaymentInfo(infoData);
        return Common.getMessageSuccess();
    }

    public Boolean checkBankExistInConfig(InfoData infoData) {
        if (getConfigBank(infoData).size() > 0) {
            log.info("BankCode does exist: " + infoData.getBankCode());
            return true;
        }
        log.error("BankCode does not exist: " + infoData.getBankCode());
        return false;
    }

    public List<ConfigBanks.Bank> getConfigBank(InfoData infoData) {
        return configBanks.getBanks().stream()
                .filter(bank -> bank.getBankCode().equals(infoData.getBankCode()))
                .collect(Collectors.toList());

    }

    public Boolean isEqualCheckSum(InfoData infoData) {
        if (checkSumAndHashString(infoData).trim().equals(infoData.getCheckSum())) {
            log.info("CheckSum: " + checkSumAndHashString(infoData));
            return true;
        } else {
            log.error("CheckSum check equal fail: "
                    + checkSumAndHashString(infoData).trim()
                    + "---"
                    + infoData.getCheckSum());
            return false;
        }
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

    public void cachePaymentInfo(InfoData infoData) {
        cacheData(infoData);
        setSerializerRedisTemplate();
    }

    public void cacheData(InfoData infoData) {
        try {
            HashOperations<Object, Object, Object> hashOperations = redisTemplate.opsForHash();
            String jsonInfoData = objectMapper.writeValueAsString(infoData);
            hashOperations.put(infoData.getBankCode(), infoData.getTokenKey(), jsonInfoData);
            log.info("CacheData success");
        } catch (JsonProcessingException e) {
            log.error("CacheData error!" + e);
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

}

