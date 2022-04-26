package vn.vnpay.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import vn.vnpay.bean.TransactionRequest;
import vn.vnpay.common.Common.ResponseCode;

public interface PaymentService {
    ResponseCode pay(TransactionRequest transactionRequest) throws JsonProcessingException;
}
