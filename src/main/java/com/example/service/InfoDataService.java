package com.example.service;

import com.example.entity.InfoData;
import com.example.entity.Message;

public interface InfoDataService {
    public Message message(InfoData infoData);

    public Message checkBanksCode(InfoData infoData);
}
