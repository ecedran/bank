package com.ironhack.bank.dtos;

import java.math.BigDecimal;

public class ThirdPartyOpDTO {

    // Long accountId, String key, BigDecimal amount

    private Long accountId;
    private String key;
    private BigDecimal amount;

    public ThirdPartyOpDTO(Long accountId, String key, BigDecimal amount) {
        this.accountId = accountId;
        this.key = key;
        this.amount = amount;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
