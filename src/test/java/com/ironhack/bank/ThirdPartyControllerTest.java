package com.ironhack.bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.bank.dtos.ThirdPartyOpDTO;
import com.ironhack.bank.models.Checking;
import com.ironhack.bank.models.users.AccountHolder;
import com.ironhack.bank.models.users.ThirdParty;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class ThirdPartyControllerTest {

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
        accountStatementRepository.deleteAll();
        checkingRepository.deleteAll();
        studentCheckingRepository.deleteAll();
        savingRepository.deleteAll();
        creditCardRepository.deleteAll();
        accountRepository.deleteAll();
    }

    //     @PostMapping("/send-money")
    //    @ResponseStatus(HttpStatus.CREATED)
    //    public void sendMoney(@RequestBody ThirdPartyOpDTO thirdPartyOpDTO) {
    //        thirdPartyService.sendMoney(thirdPartyOpDTO);
    //    }

    @Test
    void shouldSendMoneyToAccount_Success() throws Exception {

        AccountHolder accountHolder1 = accountHolderRepository.save(new AccountHolder("Eric Cedran", "1234", LocalDate.of(1989,10,8), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España"), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España")));
        Checking checking1 = checkingRepository.save(new Checking(accountHolder1, "1234", new BigDecimal("10000")));
        ThirdParty thirdParty = thirdPartyRepository.save(new ThirdParty("Amazon", "XfcWzVEeOnIugDEsGblx"));


        ThirdPartyOpDTO thirdPartyOpDTO = new ThirdPartyOpDTO(checking1.getAccountNumber(), "1234", new BigDecimal("1000"));

        String body = objectMapper.writeValueAsString(thirdPartyOpDTO);

        MvcResult result = mockMvc.perform(post("/third-party/send-money")
                        .header("hashkey", "XfcWzVEeOnIugDEsGblx")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        // The account recieved the money.
        assertEquals(new BigDecimal("11000.00"), accountRepository.findAll().get(accountRepository.findAll().size()-1).getBalance());

    }

    @Test
    void shouldWithdrawMoneyFromAccount_Success() throws Exception {

        AccountHolder accountHolder1 = accountHolderRepository.save(new AccountHolder("Eric Cedran", "1234", LocalDate.of(1989,10,8), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España"), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España")));
        Checking checking1 = checkingRepository.save(new Checking(accountHolder1, "1234", new BigDecimal("10000")));
        ThirdParty thirdParty = thirdPartyRepository.save(new ThirdParty("Amazon", "XfcWzVEeOnIugDEsGblx"));


        ThirdPartyOpDTO thirdPartyOpDTO = new ThirdPartyOpDTO(checking1.getAccountNumber(), "1234", new BigDecimal("1000"));

        String body = objectMapper.writeValueAsString(thirdPartyOpDTO);

        MvcResult result = mockMvc.perform(post("/third-party/withdraw-money")
                .header("hashkey", "XfcWzVEeOnIugDEsGblx")
                .content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();

        // The account was withdrawn of the money.
        assertEquals(new BigDecimal("9000.00"), accountRepository.findAll().get(accountRepository.findAll().size()-1).getBalance());

    }


}
