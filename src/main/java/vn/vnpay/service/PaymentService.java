package vn.vnpay.service;

import vn.vnpay.bean.InfoData;
import vn.vnpay.bean.Message;

public interface PaymentService {
    Message pay(InfoData infoData);
}
