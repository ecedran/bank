package com.ironhack.bank.dtos;

import java.math.BigDecimal;

public class TransferDTO {

    // transferMoney(Long accountNumberOut, Long accountNumberIn, String name, BigDecimal amount)

    private Long accountNumberOut;

    private Long accountNumberIn;

    private String name;

    private BigDecimal amount;

    public TransferDTO(Long accountNumberOut, Long accountNumberIn, String name, BigDecimal amount) {
        this.accountNumberOut = accountNumberOut;
        this.accountNumberIn = accountNumberIn;
        this.name = name;
        this.amount = amount;
    }

    public Long getAccountNumberOut() {
        return accountNumberOut;
    }

    public void setAccountNumberOut(Long accountNumberOut) {
        this.accountNumberOut = accountNumberOut;
    }

    public Long getAccountNumberIn() {
        return accountNumberIn;
    }

    public void setAccountNumberIn(Long accountNumberIn) {
        this.accountNumberIn = accountNumberIn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
