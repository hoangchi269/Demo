package vn.vnpay.service.impl;

import lombok.extern.slf4j.Slf4j;

import vn.vnpay.bean.TransactionRequest;
import vn.vnpay.config.ConfigBanks;
import vn.vnpay.config.Snowflake;
import vn.vnpay.service.PaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import vn.vnpay.common.Common.ResponseCode;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService, Serializable {
    private final ConfigBanks configBanks;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<Object, Object> redisTemplate;


    public PaymentServiceImpl(
            ConfigBanks configBanks,
            RedisTemplate<Object, Object> redisTemplate,
            ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.configBanks = configBanks;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public ResponseCode pay(TransactionRequest transactionRequest) throws JsonProcessingException {
        log.info("Begin pay with request: {}", objectMapper.writeValueAsString(transactionRequest));
        getConfigBank(transactionRequest);
        if (!checkBankExistInConfig(transactionRequest)) {
            return ResponseCode.INVALID_BANKCODE;
        }
        checkSumAndHashString(transactionRequest);
        if (!isEqualCheckSum(transactionRequest)) {
            return ResponseCode.INVALID_CHECKSUM;
        }
        cachePaymentInfo(transactionRequest);
        return ResponseCode.SUCCESS;
    }

    public Boolean checkBankExistInConfig(TransactionRequest transactionRequest) {
        if (getConfigBank(transactionRequest).size() > 0) {
            log.info("BankCode exist:{}", transactionRequest.getBankCode());
            return true;
        }
        log.error("BankCode does not exist: {}", transactionRequest.getBankCode());
        return false;
    }

    public List<ConfigBanks.Bank> getConfigBank(TransactionRequest transactionRequest) {
        return configBanks.getBanks().stream()
                .filter(bank -> bank.getBankCode().equals(transactionRequest.getBankCode()))
                .collect(Collectors.toList());
    }

    public Boolean isEqualCheckSum(TransactionRequest transactionRequest) {
        if (checkSumAndHashString(transactionRequest).trim().equals(transactionRequest.getCheckSum())) {
            log.info("CheckSum: {}", checkSumAndHashString(transactionRequest));
            return true;
        }
        log.error("CheckSum check equal fail: {} --- {}"
                , checkSumAndHashString(transactionRequest).trim()
                , transactionRequest.getCheckSum());
        return false;
    }

    public String checkSumAndHashString(TransactionRequest transactionRequest) {
        String checkSum = transactionRequest.getMobile()
                + transactionRequest.getBankCode()
                + transactionRequest.getAccountNo()
                + transactionRequest.getPayDate()
                + transactionRequest.getDebitAmount()
                + transactionRequest.getRespCode()
                + transactionRequest.getTraceTransfer()
                + transactionRequest.getMessageType()
                + getConfigBank(transactionRequest).get(0).getPrivateKey();
        return hashStringCheckSum(checkSum);
    }

    public String hashStringCheckSum(String checkSum) {
        return Hashing.sha256()
                .hashString(checkSum, StandardCharsets.UTF_8)
                .toString();
    }

    public void cachePaymentInfo(TransactionRequest transactionRequest) {
        cacheData(transactionRequest);
        setSerializerRedisTemplate();
    }

    public void cacheData(TransactionRequest transactionRequest) {
        try {
            HashOperations<Object, Object, Object> hashOperations = redisTemplate.opsForHash();
            String jsonInfoData = objectMapper.writeValueAsString(transactionRequest);
            hashOperations.put(transactionRequest.getBankCode(), transactionRequest.getTokenKey(), jsonInfoData);
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

