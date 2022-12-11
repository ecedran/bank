package com.ironhack.bank.dtos;

import java.math.BigDecimal;

public class CreditCardReturnDTO {

    private Long accountNumber;
    private String accountType;
    private Long id1;
    private String name1;
    private Long id2;
    private String name2;
    private BigDecimal balance;
    private BigDecimal creditLimit;
    private BigDecimal interestRate;

    public CreditCardReturnDTO(Long accountNumber, String accountType, Long id1, String name1, Long id2, String name2, BigDecimal balance, BigDecimal creditLimit, BigDecimal interestRate) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.id1 = id1;
        this.name1 = name1;
        this.id2 = id2;
        this.name2 = name2;
        this.balance = balance;
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Long getId1() {
        return id1;
    }

    public void setId1(Long id1) {
        this.id1 = id1;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public Long getId2() {
        return id2;
    }

    public void setId2(Long id2) {
        this.id2 = id2;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}
