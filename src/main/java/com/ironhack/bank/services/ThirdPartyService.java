package com.ironhack.bank.services;

import com.ironhack.bank.dtos.ThirdPartyOpDTO;
import com.ironhack.bank.models.Account;
import com.ironhack.bank.models.AccountStatement;
import com.ironhack.bank.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ThirdPartyService {

    @Autowired
    ThirdPartyRepository thirdPartyRepository;

    @Autowired
    AccountStatementRepository accountStatementRepository;

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CheckingRepository checkingRepository;
    @Autowired
    StudentCheckingRepository studentCheckingRepository;

    @Autowired
    SavingRepository savingRepository;

    public void sendMoney(ThirdPartyOpDTO thirdPartyOpDTO) {

        Long accountId = thirdPartyOpDTO.getAccountId();
        String key = thirdPartyOpDTO.getKey();
        BigDecimal amount = thirdPartyOpDTO.getAmount();

        // Check if the amount is negative and converts into a positive number.
        if (amount.compareTo(new BigDecimal("0")) < 0) {
            amount = amount.multiply(new BigDecimal("-1"));
        }

        // Check if the amount is 0
        if (amount.equals(new BigDecimal("0"))) {
            throw new IllegalArgumentException("The amount must be a number over 0.");
        }

        // Check if there is an account with the given number
        if (accountRepository.findById(accountId).isEmpty()) {
            throw new IllegalArgumentException("There is no account with the given number.");
        }

        Account accountTransaction = accountRepository.findById(accountId).get();

        // Check if the Key matches the account

        String accountKey;

        if (checkingRepository.findById(accountId).isPresent()) {
            accountKey = checkingRepository.findById(accountId).get().getSecretKey();
        }
        else if (studentCheckingRepository.findById(accountId).isPresent()) {
            accountKey = studentCheckingRepository.findById(accountId).get().getSecretKey();
        }
        else if (savingRepository.findById(accountId).isPresent()) {
            accountKey = savingRepository.findById(accountId).get().getSecretKey();
        } else {
            accountKey = null;
        }

        if (!key.equals(accountKey)) {
            throw new IllegalArgumentException("The provided password isn't correct.");
        }

        // Register the movement
        AccountStatement statement = new AccountStatement(accountTransaction, amount, "Money income from Third-Party");
        accountStatementRepository.save(statement);

        // Changes the account balance

        accountTransaction.setBalance(accountTransaction.getBalance().add(amount));
        accountRepository.save(accountTransaction);
    }

    public void withdrawMoney(ThirdPartyOpDTO thirdPartyOpDTO) {

        Long accountId = thirdPartyOpDTO.getAccountId();
        String key = thirdPartyOpDTO.getKey();
        BigDecimal amount = thirdPartyOpDTO.getAmount();

        // Check if the amount is negative and converts into a positive number.
        if (amount.compareTo(new BigDecimal("0")) < 0) {
            amount = amount.multiply(new BigDecimal("-1"));
        }

        // Check if the amount is 0
        if (amount.equals(new BigDecimal("0"))) {
            throw new IllegalArgumentException("The amount must be a number over 0.");
        }

        // Check if there is an account with the given number
        if (accountRepository.findById(accountId).isEmpty()) {
            throw new IllegalArgumentException("There is no account with the given number.");
        }

        Account accountTransaction = accountRepository.findById(accountId).get();

        // Check if the Key matches the account

        String accountKey;

        if (checkingRepository.findById(accountId).isPresent()) {
            accountKey = checkingRepository.findById(accountId).get().getSecretKey();
        }
        else if (studentCheckingRepository.findById(accountId).isPresent()) {
            accountKey = studentCheckingRepository.findById(accountId).get().getSecretKey();
        }
        else if (savingRepository.findById(accountId).isPresent()) {
            accountKey = savingRepository.findById(accountId).get().getSecretKey();
        } else {
            accountKey = null;
        }

        if (!key.equals(accountKey)) {
            throw new IllegalArgumentException("The provided password isn't correct.");
        }

        // check if there is enough founds in the account

        if (accountTransaction.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("There isn't enough founds in the account.");
        }

        // Register the movement
        AccountStatement statement = new AccountStatement(accountTransaction, amount.multiply(new BigDecimal("-1")), "Money withdraw from Third-Party");
        accountStatementRepository.save(statement);

        // Changes the account balance

        accountTransaction.setBalance(accountTransaction.getBalance().subtract(amount));
        accountRepository.save(accountTransaction);
    }
}
