package com.ironhack.bank.models;

import com.ironhack.bank.models.enums.AccountStatus;
import com.ironhack.bank.models.users.AccountHolder;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Checking extends Account{
    @NotEmpty(message = "You must supply a secret key")
    private String secretKey;

    private BigDecimal minimumBalance;

    private boolean minimumBalanceCheck;

    private BigDecimal monthlyMaintenanceFee;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    private LocalDate maintenanceFeeNextPayment;


    // Checking accounts should have a minimumBalance of 250 and a monthlyMaintenanceFee of 12.

    public Checking(AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey, BigDecimal balance) {
        super(balance, primaryOwner, secondaryOwner);
        this.secretKey = secretKey;
        this.minimumBalance = new BigDecimal("250.0");
        this.minimumBalanceCheck = false;
        this.monthlyMaintenanceFee = new BigDecimal("12.0");
        this.accountStatus = AccountStatus.ACTIVE;
        this.maintenanceFeeNextPayment = LocalDate.now().plusMonths(1);
    }

    // Overcharging the constructor because the secondaryOwner is optional
    public Checking(AccountHolder primaryOwner, String secretKey, BigDecimal balance) {
        super(balance, primaryOwner);
        this.secretKey = secretKey;
        this.minimumBalance = new BigDecimal("250.0");
        this.minimumBalanceCheck = false;
        this.monthlyMaintenanceFee = new BigDecimal("12.0");
        this.accountStatus = AccountStatus.ACTIVE;
        this.maintenanceFeeNextPayment = LocalDate.now().plusMonths(1);
    }

    public Checking() {
    }

    public boolean isMinimumBalanceCheck() {
        return minimumBalanceCheck;
    }

    public void setMinimumBalanceCheck(boolean minimumBalanceCheck) {
        this.minimumBalanceCheck = minimumBalanceCheck;
    }

    public LocalDate getMaintenanceFeeNextPayment() {
        return maintenanceFeeNextPayment;
    }

    public void setMaintenanceFeeNextPayment(LocalDate maintenanceFeeNextPayment) {
        this.maintenanceFeeNextPayment = maintenanceFeeNextPayment;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
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

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }
}
