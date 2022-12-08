package com.ironhack.bank.dtos;

import java.math.BigDecimal;

public class SavingDTO {

    // AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey, BigDecimal balance,
    // BigDecimal minimumBalance, BigDecimal interestRate
    private Long id1;
    private Long id2;
    private String secretKey;
    private BigDecimal balance;
    private BigDecimal minimumBalance;
    private BigDecimal interestRate;

    public SavingDTO(Long id1, Long id2, String secretKey, BigDecimal balance, BigDecimal minimumBalance, BigDecimal interestRate) {
        this.id1 = id1;
        this.id2 = id2;
        this.secretKey = secretKey;
        this.balance = balance;
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
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

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
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
}
