package vn.vnpay.common;

public class Common {

    public enum ResponeCode {
        SUCCESS("00", "Success"),
        INVALID_REQUEST("01", "Invalid request"),
        INVALID_BANKCODE("02","Invalid bankcode"),
        INVALID_CHECKSUM("03","Invalid checksum");
        private final String code;
        private final String message;

        ResponeCode(String code, String message) {
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
}
