package com.ironhack.bank.dtos;

import java.math.BigDecimal;

public class BalanceDTO {

    private Long accountId;

    private String primaryOrSecondary;

    private String accountType;

    private BigDecimal balance;

    public BalanceDTO(Long accountId, String primaryOrSecondary, String accountType, BigDecimal balance) {
        this.accountId = accountId;
        this.primaryOrSecondary = primaryOrSecondary;
        this.accountType = accountType;
        this.balance = balance;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getPrimaryOrSecondary() {
        return primaryOrSecondary;
    }

    public void setPrimaryOrSecondary(String primaryOrSecondary) {
        this.primaryOrSecondary = primaryOrSecondary;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
