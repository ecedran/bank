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

    public List<BalanceDTO> accessBalanceAll(Long userId) {

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

    // INFO OF THE ACCOUNT

    public AccountInfoReturnDTO accountInfo(Long accountId) {

        accountRepository.findById(accountId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no account with the given id."));

        String accountType = null;

        if(checkingRepository.findById(accountId).isPresent()) {accountType = "Checking Account";}
        else if(studentCheckingRepository.findById(accountId).isPresent()) {accountType = "Student Checking Account";}
        else if(savingRepository.findById(accountId).isPresent()) {accountType = "Saving Account";}
        else if(creditCardRepository.findById(accountId).isPresent()) {accountType = "Credit Card Account";}

        Long id1 = accountRepository.findById(accountId).get().getPrimaryOwner().getId();
        String name1 = accountHolderRepository.findById(id1).get().getUsername();

        Long id2 = null;
        String name2 = null;

        if (accountRepository.findById(accountId).get().getSecondaryOwner() != null ) {
            id2 = accountRepository.findById(accountId).get().getSecondaryOwner().getId();
            name2 = accountRepository.findById(accountId).get().getSecondaryOwner().getUsername();
        }

        BigDecimal balance = accountRepository.findById(accountId).get().getBalance();

        BigDecimal penaltyFee = accountRepository.findById(accountId).get().getPenaltyFee();

        BigDecimal minimumBalance = null;
        if(checkingRepository.findById(accountId).isPresent()) {minimumBalance = checkingRepository.findById(accountId).get().getMinimumBalance();}
        else if(savingRepository.findById(accountId).isPresent()) {minimumBalance = savingRepository.findById(accountId).get().getMinimumBalance();}

        BigDecimal monthlyMaintenanceFee = null;
        if(checkingRepository.findById(accountId).isPresent()) {monthlyMaintenanceFee = checkingRepository.findById(accountId).get().getMonthlyMaintenanceFee();}

        BigDecimal interestRate = null;
        if(savingRepository.findById(accountId).isPresent()) {interestRate = savingRepository.findById(accountId).get().getInterestRate();}
        else if(creditCardRepository.findById(accountId).isPresent()) {interestRate = creditCardRepository.findById(accountId).get().getInterestRate();}

        BigDecimal creditLimit = null;
        if(creditCardRepository.findById(accountId).isPresent()) {creditLimit = creditCardRepository.findById(accountId).get().getCreditLimit();}

        return new AccountInfoReturnDTO(accountId, accountType, id1, name1, id2, name2, balance, minimumBalance, monthlyMaintenanceFee, penaltyFee, interestRate, creditLimit);

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
    public SavingReturnDTO newSavingAccount(SavingDTO savingDTO) {
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

        SavingReturnDTO savingReturnDTO;

        if (id2 == null) {
            Saving saving = new Saving(accountHolder1, secretKey, balance, minimumBalance, interestRate);
            savingRepository.save(saving);
            savingReturnDTO = new SavingReturnDTO(saving.getAccountNumber(), "Saving Account", saving.getPrimaryOwner().getId(), saving.getPrimaryOwner().getUsername(), null, null, saving.getBalance(), saving.getMinimumBalance(), saving.getInterestRate());
        } else {
            AccountHolder accountHolder2 = accountHolderRepository.findById(id2).get();
            Saving saving = new Saving(accountHolder1, accountHolder2, secretKey, balance, minimumBalance, interestRate);
            savingRepository.save(saving);
            savingReturnDTO = new SavingReturnDTO(saving.getAccountNumber(), "Saving Account", saving.getPrimaryOwner().getId(), saving.getPrimaryOwner().getUsername(), saving.getSecondaryOwner().getId(), saving.getSecondaryOwner().getUsername(), saving.getBalance(), saving.getMinimumBalance(), saving.getInterestRate());

        }

        return savingReturnDTO;
    }

    // CREATE CREDIT CARD ACCOUNT
    // If the interestRate or creditLimit are null in the DTO, the constructor will create the account with the deffault value.

    public CreditCardReturnDTO newCreditCardAccount(CreditCardDTO creditCardDTO) {

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

        CreditCardReturnDTO creditCardReturnDTO;

        if (id2 == null) {
            CreditCard creditCard = new CreditCard(accountHolder1, balance, interestRate, creditLimit);
            creditCardRepository.save(creditCard);
            creditCardReturnDTO = new CreditCardReturnDTO(creditCard.getAccountNumber(), "Credit Card Account", creditCard.getPrimaryOwner().getId(), creditCard.getPrimaryOwner().getUsername(), null, null, creditCard.getBalance(), creditCard.getCreditLimit(), creditCard.getInterestRate());

        } else {
            AccountHolder accountHolder2 = accountHolderRepository.findById(id2).get();
            CreditCard creditCard = new CreditCard(accountHolder1, accountHolder2, balance, interestRate, creditLimit);
            creditCardRepository.save(creditCard);
            creditCardReturnDTO = new CreditCardReturnDTO(creditCard.getAccountNumber(), "Credit Card Account", creditCard.getPrimaryOwner().getId(), creditCard.getPrimaryOwner().getUsername(), creditCard.getSecondaryOwner().getId(), creditCard.getSecondaryOwner().getUsername(), creditCard.getBalance(), creditCard.getCreditLimit(), creditCard.getInterestRate());

        }

        //     public CreditCardReturnDTO(Long accountNumber, String accountType, Long id1, String name1, Long id2, String name2, BigDecimal balance, BigDecimal creditLimit, BigDecimal interestRate) {

        return creditCardReturnDTO;


    }



    // CREATE CHECKING ACCOUNT
    // If the primary Owner is under 24 years old, it generates a Student Checking Account.
    public CheckingReturnDTO newCheckingAccount(CheckingDTO checkingDTO) {

        Long id1 = checkingDTO.getId1();

        Long id2 = checkingDTO.getId2();

        String secretKey = checkingDTO.getSecretKey();

        BigDecimal balance = checkingDTO.getBalance();

        String accountType = null;

        Long accountNumber;

        String name2 = null;

        // Checks if the given account holders are valid

        accountHolderRepository.findById(id1).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no user with the given id for primary holder."));

        String name1 = accountHolderRepository.findById(id1).get().getUsername();

        if (id2 != null) {
            accountHolderRepository.findById(id2).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no user with the given id for secondly holder."));
        }

        AccountHolder primaryHolder = accountHolderRepository.findById(id1).get();

        // Checks if the primary holder is under 24 y/o and

        if (Period.between(primaryHolder.getDateOfBirth(), LocalDate.now()).getYears() < 24 && id2 != null) {

            AccountHolder secondaryHolder = accountHolderRepository.findById(id2).get();

            StudentChecking studentChecking = new StudentChecking(primaryHolder, secondaryHolder, secretKey, balance);

            accountType = "Student Checking Account";

            name2 = accountHolderRepository.findById(id2).get().getUsername();

            studentCheckingRepository.save(studentChecking);

            accountNumber = studentChecking.getAccountNumber();

        } else if (Period.between(primaryHolder.getDateOfBirth(), LocalDate.now()).getYears() < 24 && id2 == null) {

            StudentChecking studentChecking = new StudentChecking(primaryHolder, secretKey, balance);

            accountType = "Student Checking Account";

            studentCheckingRepository.save(studentChecking);

            accountNumber = studentChecking.getAccountNumber();

        } else if (id2 != null) {

            AccountHolder secondaryHolder = accountHolderRepository.findById(id2).get();

            Checking checking = new Checking(primaryHolder, secondaryHolder, secretKey, balance);

            accountType = "Checking Account";

            name2 = accountHolderRepository.findById(id2).get().getUsername();

            checkingRepository.save(checking);

            accountNumber = checking.getAccountNumber();


        } else {

            Checking checking = new Checking(primaryHolder, secretKey, balance);

            checkingRepository.save(checking);

            accountType = "Checking Account";

            accountNumber = checking.getAccountNumber();


        }

        return new CheckingReturnDTO(accountNumber, accountType, id1, name1, id2, name2, balance);

    }

    // Update info from account
    public AccountInfoReturnDTO accountUpdate(Long accountId, AccountUpdateDTO accountUpdateDTO) {

        accountRepository.findById(accountId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no account with the given id."));

        Account account = accountRepository.findById(accountId).get();

        if (accountUpdateDTO.getId1() != null ) {
            accountHolderRepository.findById(accountUpdateDTO.getId1()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no user with the given id for primary owner."));
            account.setPrimaryOwner(accountHolderRepository.findById(accountUpdateDTO.getId1()).get());
        }

        if (accountUpdateDTO.getId2() != null ) {
            accountHolderRepository.findById(accountUpdateDTO.getId2()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no user with the given id for primary owner."));
            account.setSecondaryOwner(accountHolderRepository.findById(accountUpdateDTO.getId2()).get());
        }

        accountRepository.save(account);


        if(savingRepository.findById(accountId).isPresent()) {
            Saving saving = savingRepository.findById(accountId).get();
            if(accountUpdateDTO.getInterestRate() != null) {
                saving.setInterestRate(accountUpdateDTO.getInterestRate());
            }
            if(accountUpdateDTO.getMinimumBalance() != null) {
                saving.setMinimumBalance(accountUpdateDTO.getMinimumBalance());
            }
            savingRepository.save(saving);
        }
        else if(creditCardRepository.findById(accountId).isPresent()) {
             CreditCard creditCard = creditCardRepository.findById(accountId).get();
            if(accountUpdateDTO.getInterestRate() != null) {
                creditCard.setInterestRate(accountUpdateDTO.getInterestRate());
            }
            if(accountUpdateDTO.getCreditLimit() != null) {
                creditCard.setCreditLimit(accountUpdateDTO.getCreditLimit());
            }
            creditCardRepository.save(creditCard);
        }


        return accountInfo(accountId);
    }


}
