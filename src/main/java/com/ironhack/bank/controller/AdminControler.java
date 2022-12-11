package com.ironhack.bank.controller;

import com.ironhack.bank.dtos.*;
import com.ironhack.bank.models.users.AccountHolder;
import com.ironhack.bank.models.users.Role;
import com.ironhack.bank.models.users.ThirdParty;
import com.ironhack.bank.repositories.*;
import com.ironhack.bank.services.AccountService;
import com.ironhack.bank.services.ThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminControler {

    @Autowired
    AccountService accountService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Autowired
    ThirdPartyService thirdPartyService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountStatementRepository accountStatementRepository;
    @Autowired
    private CheckingRepository checkingRepository;
    @Autowired
    private StudentCheckingRepository studentCheckingRepository;
    @Autowired
    private SavingRepository savingRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    @PostMapping("/new-checking")
    @ResponseStatus(HttpStatus.CREATED)
    public CheckingReturnDTO createAccount(@RequestBody CheckingDTO checkingDTO) {
        return accountService.newCheckingAccount(checkingDTO);
    }

    @PostMapping("/new-saving")
    @ResponseStatus(HttpStatus.CREATED)
    public SavingReturnDTO createSaving(@RequestBody SavingDTO savingDTO) {
        return accountService.newSavingAccount(savingDTO);
    }

    @PostMapping("/new-credit")
    @ResponseStatus(HttpStatus.CREATED)
    public CreditCardReturnDTO createCredit(@RequestBody CreditCardDTO creditCardDTO) {
        return accountService.newCreditCardAccount(creditCardDTO);
    }

    //  create new ACCOUNT HOLDERS

    // localhost:8080/admin/create-user

    @PostMapping("/create-user")
    public void createAccountHolder(@RequestBody AccountHolder accountHolder) {
        String encodedPassword = passwordEncoder.encode(accountHolder.getPassword());
        accountHolder.setPassword(encodedPassword);
        accountHolder = accountHolderRepository.save(accountHolder);
        Role role = roleRepository.save(new Role("USER", accountHolder));
    }
    // localhost:8080/admin/create-third

    @PostMapping("/create-third")
    public ThirdParty createThirdParty(@RequestBody ThirdPartyDTO thirdPartyDTO) {
        return thirdPartyService.createThirdParty(thirdPartyDTO);
    }


    // localhost:8080/admin/delete-account?id=
    @DeleteMapping("/delete-account")
    public void deleteAccount(@RequestParam Long id) {
        accountRepository.deleteById(id);
    }

    // localhost:8080/admin/delete-third?id=
    @DeleteMapping("/delete-third")
    public void deleteThird(@RequestParam Long id) {
        thirdPartyRepository.deleteById(id);
    }

    // localhost:8080/admin/change-account-info?id=
    @PatchMapping("change-account-info")
    @ResponseStatus(HttpStatus.OK)
    public AccountInfoReturnDTO accountUpdate(@RequestParam Long id, @RequestBody AccountUpdateDTO accountUpdateDTO){
        return accountService.accountUpdate(id, accountUpdateDTO);
    }

    // LIST OF ALL ACCOUNTS WHERE THE USER IS THE PRIMARY OR SECONDARY OWNER
    @GetMapping("/balance-all")
    @ResponseStatus(HttpStatus.OK)
    public List<BalanceDTO> accessBalanceAll(@RequestParam Long id) {
        return accountService.accessBalanceAll(id);
    }
}
