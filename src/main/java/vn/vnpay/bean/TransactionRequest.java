package vn.vnpay.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {

    @NotNull(message = "TokenKey must not be null.")
    @NotEmpty(message = "TokenKey must not be empty.")
    @NotBlank(message = "TokenKey must not be blank.")
    private String tokenKey;

    @NotEmpty(message = "ApiID must not be empty.")
    @NotNull(message = "ApiID must not be null.")
    @NotBlank(message = "ApiID must not be blank.")
    private String apiID;

    @NotEmpty(message = "Mobile must not be empty.")
    @NotNull(message = "Mobile must not be null.")
    @NotBlank(message = "Mobile must not be blank.")
    private String mobile;

    @NotEmpty(message = "BankCode must not be empty.")
    @NotNull(message = "BankCode must not be null.")
    @NotBlank(message = "BankCode must not be blank.")
    private String bankCode;

    @NotEmpty(message = "AccountNo must not be empty.")
    @NotNull(message = "AccountNo must not be null.")
    @NotBlank(message = "AccountNo must not be blank.")
    private String accountNo;

    @NotEmpty(message = "PayDate must not be empty.")
    @NotNull(message = "PayDate must not be null.")
    @NotBlank(message = "PayDate must not be blank.")
    private String payDate;

    @NotEmpty(message = "AdditionalData must not be empty.")
    @NotNull(message = "AdditionalData must not be null.")
    @NotBlank(message = "AdditionalData must not be blank.")
    private String additionalData;

    @NotEmpty(message = "debitAmount must not be empty.")
    @NotNull(message = "debitAmount must not be null.")
    @NotBlank(message = "debitAmount must not be blank.")
    private String debitAmount;

    @NotEmpty(message = "respCode must not be empty.")
    @NotNull(message = "respCode must not be null.")
    @NotBlank(message = "respCode must not be blank.")
    private String respCode;

    @NotEmpty(message = "respDesc must not be empty.")
    @NotNull(message = "respDesc must not be null.")
    @NotBlank(message = "respDesc must not be blank.")
    private String respDesc;

    @NotEmpty(message = "traceTransfer must not be empty.")
    @NotNull(message = "traceTransfer must not be null.")
    @NotBlank(message = "traceTransfer must not be blank.")
    private String traceTransfer;

    @NotEmpty(message = "messageType must not be empty.")
    @NotNull(message = "messageType must not be null.")
    @NotBlank(message = "messageType must not be blank.")
    private String messageType;

    @NotEmpty(message = "checkSum must not be empty.")
    @NotNull(message = "checkSum must not be null.")
    @NotBlank(message = "checkSum must not be blank.")
    private String checkSum;

    @NotEmpty(message = "orderCode must not be empty.")
    @NotNull(message = "orderCode must not be null.")
    @NotBlank(message = "orderCode must not be blank.")
    private String orderCode;

    @NotEmpty(message = "userName must not be empty.")
    @NotNull(message = "userName must not be null.")
    @NotBlank(message = "userName must not be blank.")
    private String userName;

    @NotEmpty(message = "realAmount must not be empty.")
    @NotNull(message = "realAmount must not be null.")
    @NotBlank(message = "realAmount must not be blank.")
    private String realAmount;

    @NotEmpty(message = "promotionCode must not be empty.")
    @NotNull(message = "promotionCode must not be null.")
    @NotBlank(message = "promotionCode must not be blank.")
    private String promotionCode;

}
