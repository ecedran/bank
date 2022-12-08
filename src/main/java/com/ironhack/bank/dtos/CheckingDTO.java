package com.ironhack.bank.dtos;

import java.math.BigDecimal;
import java.util.Optional;

public class CheckingDTO {

    // Long primaryOwnerId, Optional<Long> secondaryHolderId, String secretKey, BigDecimal balance

    private Long id1;

    private Long id2;

    private String secretKey;

    private BigDecimal balance;

    public CheckingDTO(Long id1, Long id2, String secretKey, BigDecimal balance) {
        this.id1 = id1;
        this.id2 = id2;
        this.secretKey = secretKey;
        this.balance = balance;
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
}
