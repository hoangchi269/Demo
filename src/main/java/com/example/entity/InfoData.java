package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfoData {

    @NotNull(message = "TokenKey must not be null.")
    @NotEmpty(message = "TokenKey must not be empty.")
    private String tokenKey;

    @NotEmpty(message = "ApiID must not be empty.")
    @NotNull(message = "ApiID must not be null.")
    private String apiID;

    @NotEmpty(message = "Mobile must not be empty.")
    @NotNull(message = "Mobile must not be null.")
    private String mobile;

    @NotEmpty(message = "BankCode must not be empty.")
    @NotNull(message = "BankCode must not be null.")
    private String bankCode;

    @NotEmpty(message = "AccountNo must not be empty.")
    @NotNull(message = "AccountNo must not be null.")
    private String accountNo;

    @NotEmpty(message = "PayDate must not be empty.")
    @NotNull(message = "PayDate must not be null.")
    private String payDate;

    @NotEmpty(message = "AdditionalData must not be empty.")
    @NotNull(message = "AdditionalData must not be null.")
    private String additionalData;

    @NotEmpty(message = "debitAmount must not be empty.")
    @NotNull(message = "debitAmount must not be null.")
    private String debitAmount;

    @NotEmpty(message = "respCode must not be empty.")
    @NotNull(message = "respCode must not be null.")
    private String respCode;

    @NotEmpty(message = "respDesc must not be empty.")
    @NotNull(message = "respDesc must not be null.")
    private String respDesc;

    @NotEmpty(message = "traceTransfer must not be empty.")
    @NotNull(message = "traceTransfer must not be null.")
    private String traceTransfer;

    @NotEmpty(message = "messageType must not be empty.")
    @NotNull(message = "messageType must not be null.")
    private String messageType;

    @NotEmpty(message = "checkSum must not be empty.")
    @NotNull(message = "checkSum must not be null.")
    private String checkSum;

    @NotEmpty(message = "orderCode must not be empty.")
    @NotNull(message = "orderCode must not be null.")
    private String orderCode;

    @NotEmpty(message = "userName must not be empty.")
    @NotNull(message = "userName must not be null.")
    private String userName;

    @NotEmpty(message = "realAmount must not be empty.")
    @NotNull(message = "realAmount must not be null.")
    private String realAmount;

    @NotEmpty(message = "promotionCode must not be empty.")
    @NotNull(message = "promotionCode must not be null.")
    private String promotionCode;

}
