package com.ironhack.bank.services;

import com.ironhack.bank.dtos.*;
import com.ironhack.bank.models.*;
import com.ironhack.bank.models.users.AccountHolder;
import com.ironhack.bank.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    CheckingRepository checkingRepository;

    @Autowired
    StudentCheckingRepository studentCheckingRepository;

    @Autowired
    AccountHolderRepository accountHolderRepository;
    @Autowired
    SavingRepository savingRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountStatementRepository accountStatementRepository;

    // BALANCE OF ALL THE ACCOUNTS OF A USER WITH THE GIVEN NUMBER

    public List<BalanceDTO> accessBalance(Long userId) {

        accountHolderRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no user with the given id."));

        List<Account> accounts = accountRepository.findAll();
        List<BalanceDTO> dtos = new ArrayList<>();

        for (Account account : accounts) {

            // || userId.equals(account.getSecondaryOwner().getId())
            Long id1 = account.getPrimaryOwner().getId();
            Long id2 = 0L;

            if(account.getSecondaryOwner() != null) {
                id2 = account.getSecondaryOwner().getId();
            } else {
                id2 = 0L;
            };


            if(userId.equals(id1) || userId.equals(id2) ) {

                Long accountId = account.getAccountNumber();
                BigDecimal balance = account.getBalance();
                String primaryOrSecondary = "";
                String accountType = "";

                // Is the user the primary or secondary owner
                if(userId.equals(account.getPrimaryOwner().getId())) {
                    primaryOrSecondary = "Primary Owner";
                } else {
                    primaryOrSecondary = "Secondary Owner";
                }

                // type of the account
                if(checkingRepository.findById(accountId).isPresent()) {accountType = "Checking Account";}
                else if(studentCheckingRepository.findById(accountId).isPresent()) {accountType = "Student Checking Account";}
                else if(savingRepository.findById(accountId).isPresent()) {accountType = "Saving Account";}
                else if(creditCardRepository.findById(accountId).isPresent()) {accountType = "Credit Card Account";}

                dtos.add(new BalanceDTO(accountId, primaryOrSecondary, accountType, balance));

            }

        }

        return dtos;
    }


    // LIST OF STATEMENTS FROM ACCOUNT
    public List<AccountStatementDTO> findStatementsById(Long accountId) {

        accountRepository.findById(accountId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no account with the given id."));

        List<AccountStatement> statements = accountStatementRepository.findByAccount(accountRepository.findById(accountId).get());

        List<AccountStatementDTO> dtos = new ArrayList<>();

        for (AccountStatement statement : statements) {
            dtos.add(new AccountStatementDTO(statement.getDate(), statement.getDescription(), statement.getAmount()));
        }

        return dtos;

    }


    // CRATE SAVING ACCOUNT
    // Saving(AccountHolder primaryOwner, String secretKey, BigDecimal balance)
    public void newSavingAccount(SavingDTO savingDTO) {
        //SavingDTO(Long id, Long id2, String secretKey, BigDecimal balance, BigDecimal minimumBalance, BigDecimal interestRate)

        Long id1 = savingDTO.getId1();
        Long id2 = savingDTO.getId2();
        String secretKey = savingDTO.getSecretKey();
        BigDecimal balance = savingDTO.getBalance();
        BigDecimal minimumBalance = savingDTO.getMinimumBalance();
        BigDecimal interestRate = savingDTO.getInterestRate();

        // checks if the given account numbers are valid
        accountHolderRepository.findById(id1).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no user with the given id for primary holder."));
        if (id2 != null) {
            accountHolderRepository.findById(id2).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no user with the given id for secondly holder."));
        }

        AccountHolder accountHolder1 = accountHolderRepository.findById(id1).get();

        if (id2 == null) {
            savingRepository.save(new Saving(accountHolder1, secretKey, balance, minimumBalance, interestRate));
        }

        if (id2 != null) {
            AccountHolder accountHolder2 = accountHolderRepository.findById(id2).get();
            savingRepository.save(new Saving(accountHolder1, accountHolder2, secretKey, balance, minimumBalance, interestRate));
        }

    }

    // CREATE CREDIT CARD ACCOUNT
    // If the interestRate or creditLimit are null in the DTO, the constructor will create the account with the deffault value.

    public void newCreditCardAccount(CreditCardDTO creditCardDTO) {

        Long id1 = creditCardDTO.getId1();

        Long id2 = creditCardDTO.getId2();

        BigDecimal balance = creditCardDTO.getBalance();

        BigDecimal interestRate = creditCardDTO.getInterestRate();

        BigDecimal creditLimit = creditCardDTO.getCreditLimit();

        // checks if the given account numbers are valid
        accountHolderRepository.findById(id1).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Primary holder: There is no user with the given id."));
        if (id2 != null) {
            accountHolderRepository.findById(id2).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Secondly holder: There is no user with the given id."));
        }

        AccountHolder accountHolder1 = accountHolderRepository.findById(id1).get();

        if (id2 == null) {
            creditCardRepository.save(new CreditCard(accountHolder1, balance, interestRate, creditLimit));
        }

        if (id2 != null) {
            AccountHolder accountHolder2 = accountHolderRepository.findById(id2).get();
            creditCardRepository.save(new CreditCard(accountHolder1, accountHolder2, balance, interestRate, creditLimit));
        }
    }



    // CREATE CHECKING ACCOUNT
    // If the primary Owner is under 24 years old, it generates a Student Checking Account.
    public void newCheckingAccount(CheckingDTO checkingDTO) {

        Long id1 = checkingDTO.getId1();

        Long id2 = checkingDTO.getId2();

        String secretKey = checkingDTO.getSecretKey();

        BigDecimal balance = checkingDTO.getBalance();

        // Checks if the given account numbers are valid

        accountHolderRepository.findById(id1).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no user with the given id for primary holder."));

        if (id2 != null) {
            accountHolderRepository.findById(id2).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no user with the given id for secondly holder."));
        }

        AccountHolder primaryHolder = accountHolderRepository.findById(id1).get();

        // Checks if the primary holder is under 24 y/o and

        if (Period.between(primaryHolder.getDateOfBirth(), LocalDate.now()).getYears() < 24 && id2 != null) {

            AccountHolder secondaryHolder = accountHolderRepository.findById(id2).get();

            StudentChecking studentChecking = new StudentChecking(primaryHolder, secondaryHolder, secretKey, balance);

            studentCheckingRepository.save(studentChecking);

        } else if (Period.between(primaryHolder.getDateOfBirth(), LocalDate.now()).getYears() < 24 && id2 == null) {

            StudentChecking studentChecking = new StudentChecking(primaryHolder, secretKey, balance);

            studentCheckingRepository.save(studentChecking);

        } else if (id2 != null) {

            AccountHolder secondaryHolder = accountHolderRepository.findById(id2).get();

            Checking checking = new Checking(primaryHolder, secondaryHolder, secretKey, balance);

            checkingRepository.save(checking);

        } else {

            Checking checking = new Checking(primaryHolder, secretKey, balance);

            checkingRepository.save(checking);

        }

    }


}
