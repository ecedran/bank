package com.ironhack.bank.models;

import com.ironhack.bank.models.users.AccountHolder;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class CreditCard extends Account{

    // An interestRate
    // A creditLimit

    @DecimalMin(value = "0.1", message = "Minimum interest rate should be 0.1")
    @DecimalMax(value = "0.2", message = "Maximum interest rate should be 0.2")
    @NotNull(message = "An interest rate is required")
    @Digits(integer=36, fraction=4)
    private BigDecimal interestRate;

    @Min(value = 100, message = "Credit limit must be higher than 100")
    @Max(value = 100000, message = "Credit limit must be lower than 100.000")
    @NotNull(message = "A credit limit is required")
    private BigDecimal creditLimit;

    private LocalDate interestRateNextPayment;



    // Constructor with default parameters
    public CreditCard(AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal balance) {
        super(balance, primaryOwner, secondaryOwner);
        this.interestRate = new BigDecimal("0.2");
        this.creditLimit = new BigDecimal("100.0");
        this.interestRateNextPayment=LocalDate.now().plusMonths(1);
    }

    public CreditCard(AccountHolder primaryOwner, BigDecimal balance) {
        super(balance, primaryOwner);
        this.interestRate = new BigDecimal("0.2");
        this.creditLimit = new BigDecimal("100.0");
        this.interestRateNextPayment=LocalDate.now().plusMonths(1);

    }

    // constructor with personalized parameters
    public CreditCard(AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal balance, BigDecimal interestRate, BigDecimal creditLimit) {
        super(balance, primaryOwner, secondaryOwner);
        setInterestRate(interestRate);
        setCreditLimit(creditLimit);
        this.interestRateNextPayment=LocalDate.now().plusMonths(1);

    }

    public CreditCard(AccountHolder primaryOwner, BigDecimal balance, BigDecimal interestRate, BigDecimal creditLimit) {
        super(balance, primaryOwner);
        setInterestRate(interestRate);
        setCreditLimit(creditLimit);
        this.interestRateNextPayment=LocalDate.now().plusMonths(1);

    }

    public CreditCard() {
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        if (interestRate == null) {
            this.interestRate = new BigDecimal("0.2");
        }
        else if (interestRate.compareTo(new BigDecimal("0.1")) < 0) {
            this.interestRate = new BigDecimal("0.1");
        }
        else if (interestRate.compareTo(new BigDecimal("0.2")) > 0) {
            this.interestRate = new BigDecimal("0.2");

        } else {
            this.interestRate = interestRate;
        }
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        if (creditLimit == null) {
            this.creditLimit = new BigDecimal("100.0");
        }
        else if (creditLimit.compareTo(new BigDecimal("100")) < 0) {
            this.creditLimit = new BigDecimal("100");
        }
        else if (creditLimit.compareTo(new BigDecimal("100000")) > 0) {
            this.creditLimit = new BigDecimal("100000");

        } else {
            this.creditLimit = creditLimit;
        }
    }

    public LocalDate getInterestRateNextPayment() {
        return interestRateNextPayment;
    }

    public void setInterestRateNextPayment(LocalDate interestRateNextPayment) {
        this.interestRateNextPayment = interestRateNextPayment;
    }
}
