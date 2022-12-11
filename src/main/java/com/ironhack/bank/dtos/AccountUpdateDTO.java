package com.ironhack.bank.dtos;

import java.math.BigDecimal;

public class AccountUpdateDTO {

    private Long id1;
    private Long id2;
    private BigDecimal minimumBalance;
    private BigDecimal interestRate;
    private BigDecimal creditLimit;

    public AccountUpdateDTO(Long id1, Long id2, BigDecimal minimumBalance, BigDecimal interestRate, BigDecimal creditLimit) {
        this.id1 = id1;
        this.id2 = id2;
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
        this.creditLimit = creditLimit;
    }

    public Long getId1() {
        return id1;
    }

    public void setId1(Long id1) {
        this.id1 = id1;
    }

    public Long getId2() {
        return id2;
    }

    public void setId2(Long id2) {
        this.id2 = id2;
    }

    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(BigDecimal minimumBalance) {
        this.minimumBalance = minimumBalance;
    }


    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }
}
