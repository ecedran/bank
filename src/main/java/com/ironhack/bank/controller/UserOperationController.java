package com.ironhack.bank.controller;

import com.ironhack.bank.dtos.AccountInfoReturnDTO;
import com.ironhack.bank.dtos.AccountStatementDTO;
import com.ironhack.bank.dtos.BalanceDTO;
import com.ironhack.bank.dtos.TransferDTO;
import com.ironhack.bank.repositories.AccountRepository;
import com.ironhack.bank.repositories.UserRepository;
import com.ironhack.bank.services.AccountService;
import com.ironhack.bank.services.OperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserOperationController {

    @Autowired
    AccountService accountService;

    @Autowired
    OperationsService operationsService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;

    // LIST OF ALL ACCOUNTS WHERE THE USER IS THE PRIMARY OR SECONDARY OWNER
    // localhost:8080/user/account/balance-all
    @GetMapping("/account/balance-all")
    @ResponseStatus(HttpStatus.OK)
    public List<BalanceDTO> accessBalanceAll(@AuthenticationPrincipal UserDetails userDetails) {
        return accountService.accessBalanceAll(userRepository.findByUsername(userDetails.getUsername()).get().getId());
    }

    // INFO OF ONE ACCOUNT
    // localhost:8080/user/account/info?id=
    @GetMapping("/account/info")
    @ResponseStatus(HttpStatus.OK)
    public AccountInfoReturnDTO accountInfo(@RequestParam Long id, @AuthenticationPrincipal UserDetails userDetails) {

        if (accountRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("No account was found with the given account ID.");
        } else if (userRepository.findByUsername(userDetails.getUsername()).get().getId() == accountRepository.findById(id).get().getPrimaryOwner().getId()) {
            return accountService.accountInfo(id);
        } else if (accountRepository.findById(id).get().getSecondaryOwner() != null) {
            if (userRepository.findByUsername(userDetails.getUsername()).get().getId() == accountRepository.findById(id).get().getSecondaryOwner().getId()) {
                return accountService.accountInfo(id);
            }  else throw new IllegalArgumentException("The user doesn't have access to this account.");
        } else throw new IllegalArgumentException("The user doesn't have access to this account.");
    }

    // LIST OF STATEMENTS FROM ONE ACCOUNT
    // localhost:8080/user/account/statements?id=

    @GetMapping("/account/statements")
    @ResponseStatus(HttpStatus.OK)
    public List<AccountStatementDTO> accountStatements(@RequestParam Long id, @AuthenticationPrincipal UserDetails userDetails) {
        if (accountRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("No account was found with the given account ID.");
        } else if (userRepository.findByUsername(userDetails.getUsername()).get().getId() == accountRepository.findById(id).get().getPrimaryOwner().getId()) {
            return accountService.findStatementsById(id);
        } else if (accountRepository.findById(id).get().getSecondaryOwner() != null) {
            if (userRepository.findByUsername(userDetails.getUsername()).get().getId() == accountRepository.findById(id).get().getSecondaryOwner().getId()) {
                return accountService.findStatementsById(id);
            }  else throw new IllegalArgumentException("The user doesn't have access to this account.");
        } else throw new IllegalArgumentException("The user doesn't have access to this account.");
    }

    // TRANSFER MONEY FROM ONE ACCOUNT TO THE OTHER
    @PostMapping("/transfer-money")
    @ResponseStatus(HttpStatus.CREATED)
    public void transferMoney(@AuthenticationPrincipal UserDetails userDetails, @RequestBody TransferDTO transferDTO) {

        if (userRepository.findByUsername(userDetails.getUsername()).get().getId() == accountRepository.findById(transferDTO.getAccountNumberOut()).get().getPrimaryOwner().getId()) {
            operationsService.transferMoney(transferDTO);
        } else if (accountRepository.findById(transferDTO.getAccountNumberOut()).get().getSecondaryOwner() != null) {
            if (userRepository.findByUsername(userDetails.getUsername()).get().getId() == accountRepository.findById(transferDTO.getAccountNumberOut()).get().getSecondaryOwner().getId()) {
                operationsService.transferMoney(transferDTO);
            } else throw new IllegalArgumentException("The user doesn't have access to this account.");
        } else throw new IllegalArgumentException("The user doesn't have access to this account.");

    }
}
