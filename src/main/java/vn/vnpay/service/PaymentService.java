package vn.vnpay.service;

import vn.vnpay.bean.TransactionRequest;
import vn.vnpay.common.Common.ResponeCode;

public interface PaymentService {
    ResponeCode pay(TransactionRequest transactionRequest);
}
