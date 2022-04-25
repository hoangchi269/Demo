package com.example.service;

import com.example.bean.InfoData;
import com.example.bean.Message;

public interface PaymentService {
    Message pay(InfoData infoData);
}
