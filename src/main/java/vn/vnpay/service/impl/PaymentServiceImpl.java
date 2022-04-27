package vn.vnpay.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import vn.vnpay.bean.TransactionRequest;
import vn.vnpay.common.Common.ResponseCode;
import vn.vnpay.config.Bank;
import vn.vnpay.config.ConfigBanks;
import vn.vnpay.service.PaymentService;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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
        Bank bank = findBankByBankCode(transactionRequest.getBankCode()).get();
        if (!checkBankExistInConfig(transactionRequest, bank)) {
            return ResponseCode.INVALID_BANKCODE;
        }
        String makeCheckSum = makeCheckSum(transactionRequest, bank);
        if (!isEqualCheckSum(transactionRequest, makeCheckSum)) {
            return ResponseCode.INVALID_CHECKSUM;
        }
        cachePaymentInfo(transactionRequest);
        return ResponseCode.SUCCESS;
    }

    public boolean checkBankExistInConfig(TransactionRequest transactionRequest, Bank bank) {
        if (bank != null) {
            log.info("BankCode exist:{}", transactionRequest.getBankCode());
            return true;
        }
        log.info("BankCode does not exist: {}", transactionRequest.getBankCode());
        return false;
    }

    public Optional<Bank> findBankByBankCode(String bankCode) {
        try {
            return configBanks.getBanks().stream()
                    .filter(bank -> bank.getBankCode().equals(bankCode))
                    .findFirst();
        }catch (NoSuchElementException ex) {
            log.info("Can not find bank by bank code", ex);
            return Optional.empty();
        }
    }

    public boolean isEqualCheckSum(TransactionRequest transactionRequest, String checkSum) { //TODO: sửa lại tên hàm, gọi checkSumAndHashString 3 lần
        if (checkSum.equalsIgnoreCase(transactionRequest.getCheckSum())) {
            log.info("Pass CheckSum: {}", checkSum);
            return true;
        }
        log.info("CheckSum check equal fail: {} --- {}"
                , checkSum
                , transactionRequest.getCheckSum());
        return false;
    }

    public String makeCheckSum(TransactionRequest transactionRequest, Bank bank) { // TODO: tên hàm đổi -> makeCheckSum, thêm log các thông số checkSum trước và sau khi hasString
        String checkSum = transactionRequest.getMobile()
                + transactionRequest.getBankCode()
                + transactionRequest.getAccountNo()
                + transactionRequest.getPayDate()
                + transactionRequest.getDebitAmount()
                + transactionRequest.getRespCode()
                + transactionRequest.getTraceTransfer()
                + transactionRequest.getMessageType()
                + bank.getPrivateKey(); // TODO: duplicate findBankByBankCode, truyền bank đã tìm đc
        return hashStringCheckSum(checkSum);
    }

    public String hashStringCheckSum(String checkSum) {
        return Hashing.sha256()
                .hashString(checkSum, StandardCharsets.UTF_8)
                .toString();
    }

    public void cachePaymentInfo(TransactionRequest transactionRequest) { //TODO ghi log
        setSerializerRedisTemplate();
        cacheData(transactionRequest);
    }

    public void cacheData(TransactionRequest transactionRequest) {
        try {
            String jsonInfoData = objectMapper.writeValueAsString(transactionRequest);
            HashOperations<Object, Object, Object> hashOperations = redisTemplate.opsForHash();
            hashOperations.put(transactionRequest.getBankCode(), transactionRequest.getTokenKey(), jsonInfoData);
            log.info("CacheData success");
        } catch (JsonProcessingException e) {
            log.error("CacheData error!", e);
            throw new RuntimeException(e);
        }

    }

        public void setSerializerRedisTemplate() { //TODO  để hàm này trong file config redis
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
    }

}

