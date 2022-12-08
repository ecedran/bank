package com.ironhack.bank.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AccountStatementDTO {

    private LocalDate date;

    private String description;

    private BigDecimal amount;

    public AccountStatementDTO(LocalDate date, String description, BigDecimal amount) {
        this.date = date;
        this.description = description;
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
