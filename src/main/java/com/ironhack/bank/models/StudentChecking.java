package com.ironhack.bank.models;

import com.ironhack.bank.models.enums.AccountStatus;
import com.ironhack.bank.models.users.AccountHolder;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;

@Entity
public class StudentChecking extends Account{

    @NotEmpty(message = "You must supply a secret key")
    private String secretKey;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;


    // Checking accounts should have a minimumBalance of 250 and a monthlyMaintenanceFee of 12.

    public StudentChecking(AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey, BigDecimal balance) {
        super(balance, primaryOwner, secondaryOwner);
        this.secretKey = secretKey;
        this.accountStatus = AccountStatus.ACTIVE;
    }

    // Overcharging the constructor because the secondaryOwner is optional
    public StudentChecking(AccountHolder primaryOwner, String secretKey, BigDecimal balance) {
        super(balance, primaryOwner);
        this.secretKey = secretKey;
        this.accountStatus = AccountStatus.ACTIVE;
    }

    public StudentChecking() {
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }


    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }
}
