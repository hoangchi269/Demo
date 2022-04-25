package com.example.common;

import com.example.bean.Message;

public class Common {
    public static final String CODE_00 = "00";
    public static final String CODE_01 = "01";
    public static final String CODE_02 = "02";
    public static final String CODE_03 = "03";
    public static final String SUCCESS = "Success";
    public static final String NULL_OR_BLANK = " is null or blank";

    public static Message getMessageBankCodeExist() {
        Message message = new Message();
        message.setCode(Common.CODE_02);
        message.setMessage("BankCode find not found!");
        return message;
    }

    public static Message getMessageSuccess() {
        Message message = new Message();
        message.setCode(Common.CODE_00);
        message.setMessage(Common.SUCCESS);
        return message;
    }

    public static Message getMessageCheckSumError() {
        Message message = new Message();
        message.setCode(Common.CODE_03);
        message.setMessage("CheckSum error");
        return message;
    }
}
