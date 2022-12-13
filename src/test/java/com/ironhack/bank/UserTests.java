package com.ironhack.bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.bank.dtos.CheckingDTO;
import com.ironhack.bank.dtos.SavingDTO;
import com.ironhack.bank.dtos.TransferDTO;
import com.ironhack.bank.models.Checking;
import com.ironhack.bank.models.Saving;
import com.ironhack.bank.models.users.AccountHolder;
import com.ironhack.bank.models.users.embedded.Address;
import com.ironhack.bank.repositories.*;
import com.ironhack.bank.services.AccountService;
import com.ironhack.bank.services.ThirdPartyService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class UserTests {

	@Autowired
	WebApplicationContext context;

	@Autowired
	AccountStatementRepository accountStatementRepository;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	AccountHolderRepository accountHolderRepository;

	@Autowired
	AccountService accountService;

	@Autowired
	CheckingRepository checkingRepository;

	@Autowired
	SavingRepository savingRepository;

	@Autowired
	StudentCheckingRepository studentCheckingRepository;

	@Autowired
	CreditCardRepository creditCardRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	ThirdPartyRepository thirdPartyRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ThirdPartyService thirdPartyService;

	private MockMvc mockMvc;

	private final ObjectMapper objectMapper = new ObjectMapper();



	@BeforeEach
	void setUp() {
		//Construimos el falseador, introduciendo el contexto de la app
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@AfterEach
	void tearDown() {
		accountRepository.deleteAll();
		accountStatementRepository.deleteAll();
		checkingRepository.deleteAll();
		studentCheckingRepository.deleteAll();
		savingRepository.deleteAll();
		creditCardRepository.deleteAll();
		accountHolderRepository.deleteAll();
	}


	@Test
	@WithMockUser(username = "Eric Cedran", password = "1234")
	void getSavingInfo_Success() throws Exception {

		AccountHolder accountHolder1 = accountHolderRepository.save(new AccountHolder("Eric Cedran", "1234", LocalDate.of(1989,10,8), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España"), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España")));

		AccountHolder accountHolder2 = accountHolderRepository.save(new AccountHolder("Mikel Garmilla", "1234", LocalDate.of(1987,10,23), new Address("Calle Zorrilla, 25", "Madrid", "Madrid", "42000","España"), new Address("Calle Zorrilla, 25", "Madrid", "Madrid", "42000","España")));

		Saving saving = savingRepository.save(new Saving(accountHolder1, accountHolder2, "123456", new BigDecimal("10000"),null, null));

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
						.get("/user/account/info?id=1")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		assertTrue(result.getResponse().getContentAsString().contains("{\"accountNumber\":1,\"accountType\":\"Saving Account\",\"id1\":2,\"name1\":\"Eric Cedran\",\"id2\":3,\"name2\":\"Mikel Garmilla\",\"balance\":10000.00,\"minimumBalance\":1000.00,\"monthlyMaintenanceFee\":null,\"penaltyFee\":40.00,\"interestRate\":0.0025,\"creditLimit\":null}"));

	}
	@Test
	@WithMockUser(username = "Eric Cedran", password = "1234")
	void getCheckingInfo_Success() throws Exception {

		AccountHolder accountHolder1 = accountHolderRepository.save(new AccountHolder("Eric Cedran", "1234", LocalDate.of(1989,10,8), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España"), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España")));

		Checking checking = checkingRepository.save(new Checking(accountHolder1, null, "1234", new BigDecimal("1000")));

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
						.get("/user/account/info?id=1")
						.accept(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk())
						.andReturn();

		assertTrue(result.getResponse().getContentAsString().contains("{\"accountNumber\":1,\"accountType\":\"Checking Account\",\"id1\":2,\"name1\":\"Eric Cedran\",\"id2\":null,\"name2\":null,\"balance\":1000.00,\"minimumBalance\":250.00,\"monthlyMaintenanceFee\":12.00,\"penaltyFee\":40.00,\"interestRate\":null,\"creditLimit\":null}"));

	}





	@Test
	@WithMockUser(username = "Eric Cedran", password = "1234")
	void transferMoney_Success() throws Exception {

		AccountHolder accountHolder1 = accountHolderRepository.save(new AccountHolder("Eric Cedran", "1234", LocalDate.of(1989,10,8), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España"), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España")));

		AccountHolder accountHolder2 = accountHolderRepository.save(new AccountHolder("Mikel Garmilla", "1234", LocalDate.of(1987,10,23), new Address("Calle Zorrilla, 25", "Madrid", "Madrid", "42000","España"), new Address("Calle Zorrilla, 25", "Madrid", "Madrid", "42000","España")));

		Saving saving = savingRepository.save(new Saving(accountHolder1, null, "1234", new BigDecimal("10000"),null, null));

		Checking checking = checkingRepository.save(new Checking(accountHolder2, null, "1234", new BigDecimal("1000")));

		TransferDTO transferDTO = new TransferDTO(saving.getAccountNumber(), checking.getAccountNumber(), accountHolder2.getUsername(), new BigDecimal("500"));

		String body = objectMapper.writeValueAsString(transferDTO);

		MvcResult result = mockMvc.perform(post("/user/transfer-money").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();

		// check balance
		assertEquals(new BigDecimal("9500.00"), accountRepository.findById(saving.getAccountNumber()).get().getBalance());
		assertEquals(new BigDecimal("1500.00"), accountRepository.findById(checking.getAccountNumber()).get().getBalance());

		// check the statements
		assertEquals(new BigDecimal("-500.00"), accountStatementRepository.findByAccount(saving).get(accountStatementRepository.findByAccount(saving).size()-1).getAmount());
		assertEquals(new BigDecimal("500.00"), accountStatementRepository.findByAccount(checking).get(accountStatementRepository.findByAccount(checking).size()-1).getAmount());
	}


}
