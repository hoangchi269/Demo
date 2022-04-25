package vn.vnpay.common;

import vn.vnpay.bean.Message;

public class Common {

    public enum Code {
        CODE_00("00"), CODE_01("01"), CODE_02("02"), CODE_03("03");

        private String code;

        Code(String code) {
            this.code = code;
        }
        public String getCode(){
            return code;
        }
    }

    public static final String SUCCESS = "Success";

    public static Message getMessageBankCodeExist() {
        Message message = new Message();
        message.setCode(Code.CODE_02.code);
        message.setMessage("BankCode find not found!");
        return message;
    }

    public static Message getMessageSuccess() {
        Message message = new Message();
        message.setCode(Code.CODE_00.code);
        message.setMessage(Common.SUCCESS);
        return message;
    }

    public static Message getMessageCheckSumError() {
        Message message = new Message();
        message.setCode(Code.CODE_03.code);
        message.setMessage("CheckSum error");
        return message;
    }
}
