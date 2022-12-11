package com.ironhack.bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.bank.dtos.CheckingDTO;
import com.ironhack.bank.dtos.SavingDTO;
import com.ironhack.bank.models.StudentChecking;
import com.ironhack.bank.models.users.AccountHolder;
import com.ironhack.bank.models.users.embedded.Address;
import com.ironhack.bank.repositories.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class AdminControlerTest {

    @Autowired
    WebApplicationContext context;

    @Autowired
    AccountStatementRepository accountStatementRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountHolderRepository accountHolderRepository;

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

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

/*

    @BeforeEach
    void setUp() {
        //Construimos el falseador, introduciendo el contexto de la app
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @AfterEach
    void tearDown() {
        accountStatementRepository.deleteAll();
        checkingRepository.deleteAll();
        studentCheckingRepository.deleteAll();
        savingRepository.deleteAll();
        creditCardRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    void postNewStudentChecking_Success() throws Exception {

        AccountHolder accountHolder1 = accountHolderRepository.save(new AccountHolder("Eric Cedran", "1234", LocalDate.of(2008,10,8), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España"), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España")));

        CheckingDTO checkingDTO = new CheckingDTO(accountHolder1.getId(), null, "1234", new BigDecimal("10000"));


        String body = objectMapper.writeValueAsString(checkingDTO);

        MvcResult result = mockMvc.perform(post("/new-checking").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();

        // checks if the account created if the age of the owner is under 24 is automatically a Student Checking Account.
        assertTrue(result.getResponse().getContentAsString().contains("Student"));

    }

    @Test
    void postNewSaving_Success() throws Exception {

        AccountHolder accountHolder1 = accountHolderRepository.save(new AccountHolder("Eric Cedran", "1234", LocalDate.of(1989,10,8), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España"), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España")));

        SavingDTO savingDTO = new SavingDTO(accountHolder1.getId(), null, "123", new BigDecimal("10000"), null, null);

        String body = objectMapper.writeValueAsString(savingDTO);

        MvcResult result = mockMvc.perform(post("/new-saving").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Eric"));

        // check if interest rate assigned is the default
        assertTrue(result.getResponse().getContentAsString().contains("0.0025"));

        // check if the last Saving account matches de balance
        assertEquals(new BigDecimal("10000.00"), savingRepository.findAll().get((savingRepository.findAll().size()-1)).getBalance());
    }

*/


}
