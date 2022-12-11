package com.ironhack.bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.bank.dtos.TransferDTO;
import com.ironhack.bank.models.AccountStatement;
import com.ironhack.bank.models.Checking;
import com.ironhack.bank.models.users.AccountHolder;
import com.ironhack.bank.models.users.embedded.Address;
import com.ironhack.bank.repositories.*;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class OperationControllerTest {

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



    @BeforeEach
    void setUp() {
        //Construimos el falseador, introduciendo el contexto de la app
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @AfterEach
    void tearDown() {

    }


    @Test
    @WithMockUser(username = "Eric Cedran", password = "1234")
    void shouldTransferMoney_Success() throws Exception {


        TransferDTO transferDTO = new TransferDTO(1L, 2L, "Eric Cedran", new BigDecimal("1000"));

        String body = objectMapper.writeValueAsString(transferDTO);

        MvcResult result = mockMvc.perform(post("/user/transfer-money").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();


        // check if the amount has been changed in both accounts.
        assertEquals(new BigDecimal("8000.00"), accountRepository.findById(1L).get().getBalance());
        assertEquals(new BigDecimal("7000.00"), accountRepository.findById(2L).get().getBalance());

        //check if the statements have been created.
        List<AccountStatement> statements1 = accountStatementRepository.findByAccount(accountRepository.findById(1L).get());
        List<AccountStatement> statements2 = accountStatementRepository.findByAccount(accountRepository.findById(2L).get());
        assertEquals(new BigDecimal("-1000.00"), statements1.get(statements1.size()-1).getAmount());
        assertEquals(new BigDecimal("1000.00"), statements2.get(statements2.size()-1).getAmount());

    }




}
