package com.ironhack.bank.services;

import com.ironhack.bank.models.*;
import com.ironhack.bank.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@EnableScheduling
public class ScheduledServices {

    @Autowired
    AccountStatementRepository accountStatementRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CheckingRepository checkingRepository;
    @Autowired
    SavingRepository savingRepository;
    @Autowired
    CreditCardRepository creditCardRepository;


    // Automatically, each day at 6AM, the program checks if they should apply a Maintenance Fee.
    @Scheduled(cron = "0 0 6 * * *")
    public void checkMaintenanceFee() {

        List<Checking> checkingList = checkingRepository.findByMaintenanceFeeNextPaymentBefore(LocalDate.now());

        for (int i = 0; i < checkingList.size(); i++) {

            // Subtract the Maintenance Fee from the Checking Account

            Account account = accountRepository.findById(checkingList.get(i).getAccountNumber()).get();

            account.setBalance(account.getBalance().subtract(checkingList.get(i).getMonthlyMaintenanceFee()));

            accountRepository.save(account);

            // Adds the statement

            AccountStatement accountStatement = new AccountStatement(account, checkingList.get(i).getMonthlyMaintenanceFee().multiply(new BigDecimal("-1.0")), "Monthly maintenance fee of the account (" + checkingList.get(i).getMonthlyMaintenanceFee() + "€)" );

            accountStatementRepository.save(accountStatement);

            // Add 1 month to the counter

            Checking checking = checkingRepository.findById(checkingList.get(i).getAccountNumber()).get();

            checking.setMaintenanceFeeNextPayment(checking.getMaintenanceFeeNextPayment().plusMonths(1));

            checkingRepository.save(checking);

        }

        }

    // CHECKS EVERY HOUR IF THE CHECKING ACCOUNTS ARE UNDER THE MINIMUM BALANCE

    @Scheduled(cron = "0 1 * * * *")
    public void checkMinimumBalanceChecking() {

        // creates a list of all the checking accounts with the minimumBalanceCheck = false

        List<Checking> checkingListFalse = checkingRepository.findByMinimumBalanceCheckFalse();

        for (int i = 0; i < checkingListFalse.size(); i++) {

            Checking checkingFalse = checkingRepository.findById(checkingListFalse.get(i).getAccountNumber()).get();

            // Check if the Balance is under the Minimum Balance:

            if(checkingFalse.getBalance().compareTo(checkingFalse.getMinimumBalance()) < 0 ) {

                // Subtract the penalty fee if it's true.
                checkingFalse.setBalance(checkingFalse.getBalance().subtract(checkingFalse.getPenaltyFee()));

                // adds the statement
                AccountStatement accountStatement = new AccountStatement(checkingFalse, checkingFalse.getPenaltyFee().multiply(new BigDecimal("-1")), "Penalty fee applied. The balance of the account dropped under the minimum balance of " + checkingFalse.getMinimumBalance());
                
                accountStatementRepository.save(accountStatement);

                // Switches de false state of the MinimumBalanceCheck to true.
                checkingFalse.setMinimumBalanceCheck(true);

                //save the changes
                checkingRepository.save(checkingFalse);
            }

        }

        // creates a list of all the checking accounts with the minimumBalanceCheck = true

        List<Checking> checkingListTrue = checkingRepository.findByMinimumBalanceCheckTrue();

        for (int i = 0; i < checkingListTrue.size(); i++) {

            Checking checkingTrue = checkingRepository.findById(checkingListFalse.get(i).getAccountNumber()).get();

            // check if the Balance is over the minimumBalance

            if(checkingTrue.getBalance().compareTo(checkingTrue.getMinimumBalance()) > 0 ) {

                // Switches de true state of the MinimumBalanceCheck to false.
                checkingTrue.setMinimumBalanceCheck(false);

                //save the changes
                checkingRepository.save(checkingTrue);
            }

        }



    }

    // CHECKS EVERY HOUR IF THE SAVING ACCOUNTS ARE UNDER THE MINIMUM BALANCE

    @Scheduled(cron = "0 30 * * * *")
    public void checkMinimumBalanceSaving() {

        // creates a list of all the saving accounts with the minimumBalanceCheck = false

        List<Saving> savingListFalse = savingRepository.findByMinimumBalanceCheckFalse();

        for (int i = 0; i < savingListFalse.size(); i++) {

            Saving savingFalse = savingRepository.findById(savingListFalse.get(i).getAccountNumber()).get();

            // Check if the Balance is under the Minimum Balance:

            if(savingFalse.getBalance().compareTo(savingFalse.getMinimumBalance()) < 0 ) {

                // Subtract the penalty fee if it's true.
                savingFalse.setBalance(savingFalse.getBalance().subtract(savingFalse.getPenaltyFee()));

                // adds the statement
                AccountStatement accountStatement = new AccountStatement(savingFalse, savingFalse.getPenaltyFee().multiply(new BigDecimal("-1")), "Penalty fee applied. The balance of the account dropped under the minimum balance of " + savingFalse.getMinimumBalance());

                accountStatementRepository.save(accountStatement);

                // Switches de false state of the MinimumBalanceCheck to true.
                savingFalse.setMinimumBalanceCheck(true);

                //save the changes
                savingRepository.save(savingFalse);
            }

        }

        // creates a list of all the saving accounts with the minimumBalanceCheck = true

        List<Saving> savingListTrue = savingRepository.findByMinimumBalanceCheckTrue();

        for (int i = 0; i < savingListTrue.size(); i++) {

            Saving savingTrue = savingRepository.findById(savingListFalse.get(i).getAccountNumber()).get();

            // check if the Balance is over the minimumBalance

            if(savingTrue.getBalance().compareTo(savingTrue.getMinimumBalance()) > 0 ) {

                // Switches de true state of the MinimumBalanceCheck to false.
                savingTrue.setMinimumBalanceCheck(false);

                //save the changes
                savingRepository.save(savingTrue);
            }

        }



    }

    // CHECKS EVERYDAY AT 6AM IF IT'S NECESSARY TO APPLY AN INTEREST RATE ON CREDIT ACCOUNTS

    @Scheduled(cron = "0 0 6 * * *")
    public void checkInterestRateCreditCard() {

        List<CreditCard> creditCardList = creditCardRepository.findByInterestRateNextPaymentBefore(LocalDate.now());

        for (int i = 0; i < creditCardList.size(); i++) {

            Account account = accountRepository.findById(creditCardList.get(i).getAccountNumber()).get();

            // Adds the statement

            AccountStatement accountStatement = new AccountStatement(account, account.getBalance().multiply(creditCardList.get(i).getInterestRate()), "Monthly interest of " + creditCardList.get(i).getInterestRate().multiply(new BigDecimal("100")).setScale(2, RoundingMode.DOWN) +" %");

            accountStatementRepository.save(accountStatement);


            // Adds the interest rate to the balance of the account

            account.setBalance(account.getBalance().multiply(creditCardList.get(i).getInterestRate().add(new BigDecimal("1.0"))));

            accountRepository.save(account);


            // Add 1 month to the counter

            CreditCard creditCard = creditCardRepository.findById(creditCardList.get(i).getAccountNumber()).get();

            creditCard.setInterestRateNextPayment(creditCard.getInterestRateNextPayment().plusMonths(1));

            creditCardRepository.save(creditCard);

        }

    }

    // CHECKS EVERYDAY AT 6AM IF IT'S NECESSARY TO APPLY AN INTEREST RATE ON SAVING ACCOUNTS

    @Scheduled(cron = "0 0 6 * * *")
    public void checkInterestRateSaving() {

        List<Saving> savingList = savingRepository.findByInterestRateNextPaymentBefore(LocalDate.now());

        for (int i = 0; i < savingList.size(); i++) {

            Account account = accountRepository.findById(savingList.get(i).getAccountNumber()).get();

            // Adds the statement

            AccountStatement accountStatement = new AccountStatement(account, account.getBalance().multiply(savingList.get(i).getInterestRate()), "Yearly interest of " + savingList.get(i).getInterestRate().multiply(new BigDecimal("100")).setScale(2, RoundingMode.DOWN) +" %");

            accountStatementRepository.save(accountStatement);


            // Adds the interest rate to the balance of the account

            account.setBalance(account.getBalance().multiply(savingList.get(i).getInterestRate().add(new BigDecimal("1.0"))));

            accountRepository.save(account);


            // Add 1 month to the counter

            Saving saving = savingRepository.findById(savingList.get(i).getAccountNumber()).get();

            saving.setInterestRateNextPayment(saving.getInterestRateNextPayment().plusYears(1));

            savingRepository.save(saving);

        }

    }


}




