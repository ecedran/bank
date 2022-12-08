package com.ironhack.bank;

import com.ironhack.bank.dtos.ThirdPartyOpDTO;
import com.ironhack.bank.dtos.TransferDTO;
import com.ironhack.bank.models.*;
import com.ironhack.bank.models.users.AccountHolder;
import com.ironhack.bank.models.users.embedded.Address;
import com.ironhack.bank.repositories.*;
import com.ironhack.bank.services.AccountService;
import com.ironhack.bank.services.OperationsService;
import com.ironhack.bank.services.ThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@SpringBootApplication
public class BankApplication implements CommandLineRunner {


	@Autowired
	AccountHolderRepository accountHolderRepository;

	@Autowired
	CheckingRepository checkingRepository;

	@Autowired
	OperationsService operationsService;

	@Autowired
	StudentCheckingRepository studentCheckingRepository;
	@Autowired
	CreditCardRepository creditCardRepository;
	@Autowired
	SavingRepository savingRepository;

	@Autowired
	ThirdPartyRepository thirdPartyRepository;

	@Autowired
	ThirdPartyService thirdPartyService;

	@Autowired
	AccountService accountService;



	public static void main(String[] args) {
		SpringApplication.run(BankApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {



		AccountHolder accountHolder1 = accountHolderRepository.save(new AccountHolder("Eric Cedran", "1234", LocalDate.of(2010,10,8), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España"), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España")));

		Checking checking1 = checkingRepository.save(new Checking(accountHolder1, "1234", new BigDecimal("10000")));

		AccountHolder accountHolder2 = accountHolderRepository.save(new AccountHolder("Mikel Garmilla", "Ori1234", LocalDate.of(1989,10,8), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España"), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España")));

		Checking checking2 = checkingRepository.save(new Checking(accountHolder2, "1234", new BigDecimal("15000")));

		StudentChecking studentChecking1 = studentCheckingRepository.save(new StudentChecking(accountHolder2, "12324", new BigDecimal("10000")));

		TransferDTO transferDTO1 = new TransferDTO(1L, 2L, "Mikel Garmilla", new BigDecimal("10"));

		operationsService.transferMoney(transferDTO1);

		Checking checking3 = checkingRepository.findById(1L).get();

		checking3.setMaintenanceFeeNextPayment(LocalDate.now().minusDays(1));

		checkingRepository.save(checking3);

		Checking checking4 = checkingRepository.findById(2L).get();

		checking4.setMaintenanceFeeNextPayment(LocalDate.now().minusDays(1));

		checkingRepository.save(checking4);


		CreditCard creditCard1 = creditCardRepository.save(new CreditCard(accountHolder2, new BigDecimal("1000")));

		CreditCard creditCard2 = creditCardRepository.findById(4L).get();

		creditCard2.setInterestRateNextPayment(LocalDate.now().minusDays(1));

		creditCardRepository.save(creditCard2);


		Saving saving1 = savingRepository.save(new Saving(accountHolder1, accountHolder2, "1234", new BigDecimal("15000")));

		Saving saving2 = savingRepository.findById(5L).get();

		saving2.setInterestRateNextPayment(LocalDate.now().minusDays(1));

		savingRepository.save(saving2);

		ThirdPartyOpDTO thirdPartyOpDTO1 = new ThirdPartyOpDTO(1L, "1234", new BigDecimal("1200"));

		thirdPartyService.sendMoney(thirdPartyOpDTO1);

		TransferDTO transferDTO2 = new TransferDTO(1L, 2L, "Mikel Garmilla", new BigDecimal("11000"));

		operationsService.transferMoney(transferDTO2);

	}
}
