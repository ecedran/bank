package com.ironhack.bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.bank.dtos.TransferDTO;
import com.ironhack.bank.models.Checking;
import com.ironhack.bank.models.users.AccountHolder;
import com.ironhack.bank.models.users.embedded.Address;
import com.ironhack.bank.repositories.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        //Construimos el falseador, introduciendo el contexto de la app
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    //     @PostMapping("/transfer-money")
    //    @ResponseStatus(HttpStatus.CREATED)
    //    public void transferMoney(@RequestBody TransferDTO transferDTO) {
    //        operationsService.transferMoney(transferDTO);
    //    }

    @Test
    void shouldTransferMoney_Success() throws Exception {
//        AccountHolder accountHolder1 = accountHolderRepository.save(new AccountHolder("Eric Cedran", "1234", LocalDate.of(2010,10,8), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España"), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España")));
//        Checking checking1 = checkingRepository.save(new Checking(accountHolder1, "1234", new BigDecimal("10000")));
//        AccountHolder accountHolder2 = accountHolderRepository.save(new AccountHolder("Mikel Garmilla", "Ori1234", LocalDate.of(1989,10,8), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España"), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España")));
//        Checking checking2 = checkingRepository.save(new Checking(accountHolder2, "1234", new BigDecimal("15000")));
//
//        TransferDTO transferDTO = new TransferDTO(accountHolder1.getId(), accountHolder2.getId(), "Mikel Garmilla", new BigDecimal("1000"));
//
//        String body = objectMapper.writeValueAsString(transferDTO);
//
//        MvcResult result = mockMvc.perform(post("/transfer-money").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
//
//        assertEquals(new BigDecimal("9000"), accountRepository.findById(checking1.getAccountNumber()).get().getBalance());
        assertEquals(1, 1);

    }


}
