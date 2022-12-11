package com.ironhack.bank.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ironhack.bank.models.users.AccountHolder;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "account")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountNumber;

    @NotNull(message = "A balance is required.")
    private BigDecimal balance;

    @ManyToOne
    @NotNull(message = "You must supply a primary account holder.")
    private AccountHolder primaryOwner;

    @ManyToOne
    private AccountHolder secondaryOwner;

    private BigDecimal penaltyFee;
    private LocalDate creationDate;

    @JsonIgnore
    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<AccountStatement> accountStatements;

    // The penaltyFee for all accounts should be 40.
    public Account(BigDecimal balance, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.penaltyFee = new BigDecimal("40.0");
        this.creationDate = LocalDate.now();
    }

    // Overcharging the constructor because the secondaryOwner is optional
    public Account(BigDecimal balance, AccountHolder primaryOwner) {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.penaltyFee = new BigDecimal("40.0");
        this.creationDate = LocalDate.now();
    }


    public Account() {

    }

    public Long getAccountNumber() {
        return accountNumber;
    }


    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public AccountHolder getPrimaryOwner() {
        return primaryOwner;
    }

    public void setPrimaryOwner(AccountHolder primaryOwner) {
        this.primaryOwner = primaryOwner;
    }

    public AccountHolder getSecondaryOwner() {
        return secondaryOwner;
    }

    public void setSecondaryOwner(AccountHolder secondaryOwner) {
        this.secondaryOwner = secondaryOwner;
    }

    public BigDecimal getPenaltyFee() {
        return penaltyFee;
    }

    public void setPenaltyFee(BigDecimal penaltyFee) {
        this.penaltyFee = penaltyFee;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

}
