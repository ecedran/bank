package com.ironhack.bank.dtos;

import java.math.BigDecimal;

public class AccountInfoReturnDTO {

    private Long accountNumber;
    private String accountType;
    private Long id1;
    private String name1;
    private Long id2;
    private String name2;
    private BigDecimal balance;
    private BigDecimal minimumBalance;
    private BigDecimal monthlyMaintenanceFee;
    private BigDecimal penaltyFee;
    private BigDecimal interestRate;
    private BigDecimal creditLimit;

    public AccountInfoReturnDTO(Long accountNumber, String accountType, Long id1, String name1, Long id2, String name2, BigDecimal balance, BigDecimal minimumBalance, BigDecimal monthlyMaintenanceFee, BigDecimal penaltyFee, BigDecimal interestRate, BigDecimal creditLimit) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.id1 = id1;
        this.name1 = name1;
        this.id2 = id2;
        this.name2 = name2;
        this.balance = balance;
        this.minimumBalance = minimumBalance;
        this.monthlyMaintenanceFee = monthlyMaintenanceFee;
        this.penaltyFee = penaltyFee;
        this.interestRate = interestRate;
        this.creditLimit = creditLimit;
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

    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(BigDecimal minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public BigDecimal getMonthlyMaintenanceFee() {
        return monthlyMaintenanceFee;
    }

    public void setMonthlyMaintenanceFee(BigDecimal monthlyMaintenanceFee) {
        this.monthlyMaintenanceFee = monthlyMaintenanceFee;
    }

    public BigDecimal getPenaltyFee() {
        return penaltyFee;
    }

    public void setPenaltyFee(BigDecimal penaltyFee) {
        this.penaltyFee = penaltyFee;
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
