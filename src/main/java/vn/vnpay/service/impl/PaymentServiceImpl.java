package vn.vnpay.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;
import io.lettuce.core.RedisCommandTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import vn.vnpay.bean.TransactionRequest;
import vn.vnpay.common.Common.ResponseCode;
import vn.vnpay.config.Bank;
import vn.vnpay.config.ConfigBanks;
import vn.vnpay.service.PaymentService;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

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
    public ResponseCode pay(TransactionRequest transactionRequest) {
        Optional<Bank> bank = findBankByBankCode(transactionRequest.getBankCode());
        if (bank.isEmpty()) {
            return ResponseCode.INVALID_BANKCODE;
        }
        String makeCheckSum = makeCheckSum(transactionRequest, bank.get());
        if (!checkSumRequest(transactionRequest.getCheckSum(), makeCheckSum)) {
            return ResponseCode.INVALID_CHECKSUM;
        }
        if (cachePaymentInfo(transactionRequest)) {
            return ResponseCode.SUCCESS;
        }
        return ResponseCode.CONNECTION_FAILURE;
    }

    public Optional<Bank> findBankByBankCode(String bankCode) {
        Optional<Bank> optionalBank = configBanks.getBanks().stream()
                .filter(bank -> bank.getBankCode().equals(bankCode))
                .findFirst();
        if (optionalBank.isPresent()) {
            log.info("BankCode exist:{}", bankCode);
            return optionalBank;
        }
        log.info("Can not find bank by bank code: {}", bankCode);
        return Optional.empty();
    }

    public boolean checkSumRequest(String checkSumTransactionRequest, String checkSum) {
        log.info("CheckSum Transaction Request: {}", checkSumTransactionRequest);
        if (checkSum.trim().equalsIgnoreCase(checkSumTransactionRequest)) {
            log.info("Pass CheckSum: {}", checkSum);
            return true;
        }
        log.info("CheckSum check equal fail: {} --- {}"
                , checkSum
                , checkSumTransactionRequest);
        return false;
    }

    public String makeCheckSum(TransactionRequest transactionRequest, Bank bank) {
        String checkSum = transactionRequest.getMobile()
                + transactionRequest.getBankCode()
                + transactionRequest.getAccountNo()
                + transactionRequest.getPayDate()
                + transactionRequest.getDebitAmount()
                + transactionRequest.getRespCode()
                + transactionRequest.getTraceTransfer()
                + transactionRequest.getMessageType()
                + bank.getPrivateKey();
        log.info("CheckSum before hashString: {} ", checkSum);
        return hashStringCheckSum(checkSum);
    }

    public String hashStringCheckSum(String checkSum) {
        String hashStringCheckSum = Hashing.sha256()
                .hashString(checkSum, StandardCharsets.UTF_8)
                .toString();
        log.info("CheckSum after hashString: {}", hashStringCheckSum);
        return hashStringCheckSum;
    }

    public boolean cachePaymentInfo(TransactionRequest transactionRequest) {
        try {
            log.info("Begin cache payment to redis");
            String jsonTransactionRequest = objectMapper.writeValueAsString(transactionRequest);
            HashOperations<Object, Object, Object> hashOperations = redisTemplate.opsForHash();
            hashOperations.put(transactionRequest.getBankCode(), transactionRequest.getTokenKey(), jsonTransactionRequest);
            log.info("CacheData success");
            return true;
        } catch (JsonProcessingException | RedisConnectionFailureException | RedisCommandTimeoutException e) {
            log.error("CacheData error!", e);
            return false;
        }
    }

}

