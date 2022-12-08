package com.ironhack.bank.services;

import com.ironhack.bank.dtos.TransferDTO;
import com.ironhack.bank.models.Account;
import com.ironhack.bank.models.AccountStatement;
import com.ironhack.bank.repositories.AccountRepository;
import com.ironhack.bank.repositories.AccountStatementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OperationsService {

    @Autowired
    AccountStatementRepository accountStatementRepository;
    @Autowired
    AccountRepository accountRepository;


    public void transferMoney(TransferDTO transferDTO) {

        Long accountNumberOut = transferDTO.getAccountNumberOut();
        Long accountNumberIn = transferDTO.getAccountNumberIn();
        String name = transferDTO.getName();
        BigDecimal amount = transferDTO.getAmount();

        // Check if the amount is negative and converts into a positive number.
        if (amount.compareTo(new BigDecimal("0")) < 0) {
            amount = amount.multiply(new BigDecimal("-1"));
        }

        // Check if the amount is 0
        if (amount.equals(new BigDecimal("0"))) {
            throw new IllegalArgumentException("The amount must be a number over 0.");

        }

        // Check if there is an account with the given number
        if (accountRepository.findById(accountNumberOut).isEmpty() || accountRepository.findById(accountNumberIn).isEmpty()) {
            throw new IllegalArgumentException("The account number given didn't exist.");
        }

        Account accountOut = accountRepository.findById(accountNumberOut).get();
        Account accountIn = accountRepository.findById(accountNumberIn).get();



        // Check if there is enough founds in the account.
        if (accountOut.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Not enough balance in the account to make this transfer.");
        }

        // Check if the given name matches the account holder, uppercase, and without blank spaces.

        if (!name.toUpperCase().replace(" ", "").equals(accountIn.getPrimaryOwner().getUsername().toUpperCase().replace(" ", ""))) {
            if (accountIn.getSecondaryOwner() == null) {
                throw new IllegalArgumentException("The given name doesn't match with the account receiver");

            }

            if (!name.toUpperCase().replace(" ", "").equals(accountIn.getSecondaryOwner().getUsername().toUpperCase().replace(" ", ""))) {
                throw new IllegalArgumentException("The given name doesn't match with the account receiver");
            }
        }

        // Register the movement
        AccountStatement movementOut = new AccountStatement(accountOut, amount.multiply(new BigDecimal("-1")), "Transfer to the account number " + accountNumberIn);
        AccountStatement movementIn = new AccountStatement(accountIn, amount, "Transfer income from the account number " + accountNumberOut);
        accountStatementRepository.save(movementOut);
        accountStatementRepository.save(movementIn);

        // Changes the account balance

        accountOut.setBalance(accountOut.getBalance().subtract(amount));
        accountIn.setBalance(accountIn.getBalance().add(amount));
        accountRepository.save(accountOut);
        accountRepository.save(accountIn);

    }




}
