package com.ironhack.bank;

import com.ironhack.bank.dtos.SavingDTO;
import com.ironhack.bank.models.users.AccountHolder;
import com.ironhack.bank.models.users.embedded.Address;
import com.ironhack.bank.repositories.*;
import com.ironhack.bank.services.AccountService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BankApplicationTests {

	@Autowired
	AccountService accountService;

	@Autowired
	AccountHolderRepository accountHolderRepository;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	AccountStatementRepository accountStatementRepository;

	@Autowired
	CheckingRepository checkingRepository;

	@Autowired
	CreditCardRepository creditCardRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	SavingRepository savingRepository;

	@Autowired
	StudentCheckingRepository studentCheckingRepository;

	@Autowired
	ThirdPartyRepository thirdPartyRepository;

	@Autowired
	UserRepository userRepository;

	@Test
	void contextLoads() {
	}

	@BeforeEach
	void setUp() {

		accountHolderRepository.save(new AccountHolder("Eric Cedran", "12345", LocalDate.of(1989, 10, 8), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España"), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España")));
		accountHolderRepository.save(new AccountHolder("Mikel Garmilla", "Oriñon", LocalDate.of(1987, 10, 23), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España"), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España")));

	}

	@AfterEach
	void tearDown() {
		accountHolderRepository.deleteAll();
		accountRepository.deleteAll();
		accountStatementRepository.deleteAll();
		checkingRepository.deleteAll();
		creditCardRepository.deleteAll();
		roleRepository.deleteAll();
		savingRepository.deleteAll();
		studentCheckingRepository.deleteAll();
		thirdPartyRepository.deleteAll();
		userRepository.deleteAll();
	}


	@Test
	void addSavingAccount_OK() {


		SavingDTO savingDTO = new SavingDTO(1L, 2L, "1234", new BigDecimal("10000"), new BigDecimal("500"), new BigDecimal("0.5"));

		accountService.newSavingAccount(savingDTO);

		assertEquals(1, savingRepository.findAll().size());
	}

}
