package vn.vnpay.common;

import vn.vnpay.config.Snowflake;

public class Common {

    public enum ResponseCode {
        SUCCESS("00", "Success"),
        INVALID_REQUEST("01", "Invalid request"),
        INVALID_BANKCODE("02","Invalid bankcode"),
        INVALID_CHECKSUM("03","Invalid checksum");
        private final String code;
        private final String message;

        ResponseCode(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }

    public static long SNOWFLAKE = (new Snowflake()).nextId();
}
