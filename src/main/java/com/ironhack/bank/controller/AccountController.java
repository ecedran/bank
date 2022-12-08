package com.ironhack.bank.controller;

import com.ironhack.bank.dtos.*;
import com.ironhack.bank.models.AccountStatement;
import com.ironhack.bank.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @PostMapping("/new-checking")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAccount(@RequestBody CheckingDTO checkingDTO) {
        accountService.newCheckingAccount(checkingDTO);
    }

    @PostMapping("/new-saving")
    @ResponseStatus(HttpStatus.CREATED)
    public void createSaving(@RequestBody SavingDTO savingDTO) {
        accountService.newSavingAccount(savingDTO);
    }

    @PostMapping("/new-credit")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCredit(@RequestBody CreditCardDTO creditCardDTO) {
        accountService.newCreditCardAccount(creditCardDTO);
    }

    // LIST OF ALL ACCOUNTS WHERE THE USER IS THE PRIMARY OR SECONDARY OWNER
    @GetMapping("/balance/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<BalanceDTO> accessBalance(@PathVariable Long userId) {
        return accountService.accessBalance(userId);
    }


    @GetMapping("/{accountId}/statements")
    @ResponseStatus(HttpStatus.OK)
    public List<AccountStatementDTO> accountStatements(@PathVariable Long accountId) {
        return accountService.findStatementsById(accountId);
    }


}
