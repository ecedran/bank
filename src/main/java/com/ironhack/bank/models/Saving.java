package com.ironhack.bank.models;

import com.ironhack.bank.models.enums.AccountStatus;
import com.ironhack.bank.models.users.AccountHolder;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Saving extends Account{

    @NotEmpty(message = "You must supply a secret key")
    private String secretKey;

    private BigDecimal minimumBalance ;

    private boolean minimumBalanceCheck;


    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @DecimalMax(value = "0.5", message = "The maximum interest rate is 0.5")
    @DecimalMin(value = "0", message = "Minimum interest rate is 0")
    @NotNull(message = "An interest rate is required")
    @Digits(integer=36, fraction=4)
    private BigDecimal interestRate;

    private LocalDate interestRateNextPayment;


    // Constructors with default parameters

    public Saving(AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey, BigDecimal balance) {
        super(balance, primaryOwner, secondaryOwner);
        this.minimumBalance = new BigDecimal("1000");
        this.minimumBalanceCheck = false;
        this.accountStatus = AccountStatus.ACTIVE;
        this.interestRate = new BigDecimal("0.0025");
        this.interestRateNextPayment = LocalDate.now().plusYears(1);
        this.secretKey = secretKey;
    }

    public Saving(AccountHolder primaryOwner, String secretKey, BigDecimal balance) {
        super(balance, primaryOwner);
        this.minimumBalance = new BigDecimal("1000");
        this.minimumBalanceCheck = false;
        this.accountStatus = AccountStatus.ACTIVE;
        this.interestRate = new BigDecimal("0.0025");
        this.interestRateNextPayment = LocalDate.now().plusYears(1);
        this.secretKey = secretKey;

    }

    // Constructors with personalized minimumBalance and interestRate parameters
    public Saving(AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey, BigDecimal balance, BigDecimal minimumBalance, BigDecimal interestRate) {
        super(balance, primaryOwner, secondaryOwner);
        setMinimumBalance(minimumBalance);
        this.minimumBalanceCheck = false;
        this.accountStatus = AccountStatus.ACTIVE;
        setInterestRate(interestRate);
        this.interestRateNextPayment = LocalDate.now().plusYears(1);
        this.secretKey = secretKey;
    }

    public Saving(AccountHolder primaryOwner, String secretKey, BigDecimal balance, BigDecimal minimumBalance, BigDecimal interestRate) {
        super(balance, primaryOwner);
        setMinimumBalance(minimumBalance);
        this.minimumBalanceCheck = false;
        this.accountStatus = AccountStatus.ACTIVE;
        setInterestRate(interestRate);
        this.interestRateNextPayment = LocalDate.now().plusYears(1);
        this.secretKey = secretKey;
    }

    public Saving() {
    }

    public String getSecretKey() {
        return secretKey;
    }

    public boolean isMinimumBalanceCheck() {
        return minimumBalanceCheck;
    }

    public void setMinimumBalanceCheck(boolean minimumBalanceCheck) {
        this.minimumBalanceCheck = minimumBalanceCheck;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(BigDecimal minimumBalance) {
        if (minimumBalance == null) {
            this.minimumBalance = new BigDecimal("1000");
        } else if (minimumBalance.compareTo(new BigDecimal("100")) < 0) {
            this.minimumBalance = new BigDecimal("100");
        }
        else if (minimumBalance.compareTo(new BigDecimal("1000")) > 0) {
            this.minimumBalance = new BigDecimal("1000");
        }
        else {
            this.minimumBalance = minimumBalance;
        }
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        if (interestRate == null) {
            this.interestRate = new BigDecimal("0.0025");
        } else if (interestRate.compareTo(new BigDecimal("0")) <= 0) {
            this.interestRate = new BigDecimal("0");
        }
        else if (interestRate.compareTo(new BigDecimal("0.5")) > 0) {
            this.interestRate = new BigDecimal("0.5");

        } else {
            this.interestRate = interestRate;
        }
    }

    public LocalDate getInterestRateNextPayment() {
        return interestRateNextPayment;
    }

    public void setInterestRateNextPayment(LocalDate interestRateNextPayment) {
        this.interestRateNextPayment = interestRateNextPayment;
    }
}
