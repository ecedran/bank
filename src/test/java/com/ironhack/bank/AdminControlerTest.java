package com.ironhack.bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.bank.dtos.CheckingDTO;
import com.ironhack.bank.dtos.CreditCardDTO;
import com.ironhack.bank.dtos.SavingDTO;
import com.ironhack.bank.dtos.ThirdPartyDTO;
import com.ironhack.bank.models.Checking;
import com.ironhack.bank.models.CreditCard;
import com.ironhack.bank.models.StudentChecking;
import com.ironhack.bank.models.users.AccountHolder;
import com.ironhack.bank.models.users.ThirdParty;
import com.ironhack.bank.models.users.embedded.Address;
import com.ironhack.bank.repositories.*;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
        accountStatementRepository.deleteAll();
        checkingRepository.deleteAll();
        studentCheckingRepository.deleteAll();
        savingRepository.deleteAll();
        creditCardRepository.deleteAll();
        accountRepository.deleteAll();
    }


    // Checks if when creating a Checking of a user under 24 y/o, the account created is Student type.
    @Test
    @WithMockUser(username = "Admin", password = "admin")
    void postNewStudentChecking_Success() throws Exception {

        AccountHolder accountHolder1 = accountHolderRepository.save(new AccountHolder("Eric Cedran", "1234", LocalDate.of(2008,10,8), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España"), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España")));

        CheckingDTO checkingDTO = new CheckingDTO(accountHolder1.getId(), null, "1234", new BigDecimal("10000"));


        String body = objectMapper.writeValueAsString(checkingDTO);

        MvcResult result = mockMvc.perform(post("/admin/new-checking").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();

        // checks if the account created if the age of the owner is under 24 is automatically a Student Checking Account.
        assertTrue(result.getResponse().getContentAsString().contains("Student"));
        assertEquals(studentCheckingRepository.findAll().get(studentCheckingRepository.findAll().size()-1).getPrimaryOwner().getId(), accountHolder1.getId());
        assertEquals(studentCheckingRepository.findAll().get(studentCheckingRepository.findAll().size()-1).getBalance(), new BigDecimal("10000.00"));
    }

    // Checks if when creating a Checking of a user over 24 y/o, the account created is Checking type.
    @Test
    @WithMockUser(username = "Admin", password = "admin")
    void postNewChecking_Success() throws Exception {

        AccountHolder accountHolder1 = accountHolderRepository.save(new AccountHolder("Eric Cedran", "1234", LocalDate.of(1989,10,8), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España"), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España")));

        CheckingDTO checkingDTO = new CheckingDTO(accountHolder1.getId(), null, "1234", new BigDecimal("5000"));

        String body = objectMapper.writeValueAsString(checkingDTO);

        MvcResult result = mockMvc.perform(post("/admin/new-checking").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();

        assertEquals(checkingRepository.findAll().get(checkingRepository.findAll().size()-1).getPrimaryOwner().getId(), accountHolder1.getId());
        assertEquals(checkingRepository.findAll().get(checkingRepository.findAll().size()-1).getBalance(), new BigDecimal("5000.00"));
    }


    // ----------------------------------- /admin/new-saving -------------------------------------------------------

    // Checks if it creates a saving account correctly
    @Test
    @WithMockUser(username = "Admin", password = "admin")
    void postNewSaving_Success() throws Exception {

        AccountHolder accountHolder1 = accountHolderRepository.save(new AccountHolder("Eric Cedran", "1234", LocalDate.of(1989,10,8), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España"), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España")));

        SavingDTO savingDTO = new SavingDTO(accountHolder1.getId(), null, "1234", new BigDecimal("5000"), null, null);

        String body = objectMapper.writeValueAsString(savingDTO);

        MvcResult result = mockMvc.perform(post("/admin/new-saving").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Saving"));
        assertEquals(savingRepository.findAll().get(savingRepository.findAll().size()-1).getPrimaryOwner().getId(), accountHolder1.getId());
        assertEquals(savingRepository.findAll().get(savingRepository.findAll().size()-1).getBalance(), new BigDecimal("5000.00"));

        // default values are assigned
        assertEquals(savingRepository.findAll().get(savingRepository.findAll().size()-1).getMinimumBalance(), new BigDecimal("1000.00"));
        assertEquals(savingRepository.findAll().get(savingRepository.findAll().size()-1).getInterestRate(), new BigDecimal("0.0025"));
    }

    // Checks if it creates a Saving account correctly with personalized interestRate and minimumBalance
    @Test
    @WithMockUser(username = "Admin", password = "admin")
    void postNewSavingPersonalized_Success() throws Exception {

        AccountHolder accountHolder1 = accountHolderRepository.save(new AccountHolder("Eric Cedran", "1234", LocalDate.of(1989,10,8), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España"), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España")));

        SavingDTO savingDTO = new SavingDTO(accountHolder1.getId(), null, "1234", new BigDecimal("5000"), new BigDecimal("400"),  new BigDecimal("0.4"));

        String body = objectMapper.writeValueAsString(savingDTO);

        MvcResult result = mockMvc.perform(post("/admin/new-saving").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Saving"));
        assertEquals(savingRepository.findAll().get(savingRepository.findAll().size()-1).getPrimaryOwner().getId(), accountHolder1.getId());
        assertEquals(savingRepository.findAll().get(savingRepository.findAll().size()-1).getBalance(), new BigDecimal("5000.00"));

        // default values are assigned
        assertEquals(savingRepository.findAll().get(savingRepository.findAll().size()-1).getMinimumBalance(), new BigDecimal("400.00"));
        assertEquals(savingRepository.findAll().get(savingRepository.findAll().size()-1).getInterestRate(), new BigDecimal("0.4000"));
    }

    // Checks if it creates a Saving account correctly with personalized interestRate and minimumBalance that are out of limits
    // and assigns the nearest permitted parameter.
    @Test
    @WithMockUser(username = "Admin", password = "admin")
    void postNewSavingPersonalized2_Success() throws Exception {

        AccountHolder accountHolder1 = accountHolderRepository.save(new AccountHolder("Eric Cedran", "1234", LocalDate.of(1989,10,8), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España"), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España")));

        SavingDTO savingDTO = new SavingDTO(accountHolder1.getId(), null, "1234", new BigDecimal("5000"), new BigDecimal("0"),  new BigDecimal("1"));

        String body = objectMapper.writeValueAsString(savingDTO);

        MvcResult result = mockMvc.perform(post("/admin/new-saving").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Saving"));
        assertEquals(savingRepository.findAll().get(savingRepository.findAll().size()-1).getPrimaryOwner().getId(), accountHolder1.getId());
        assertEquals(savingRepository.findAll().get(savingRepository.findAll().size()-1).getBalance(), new BigDecimal("5000.00"));

        // default values are assigned
        assertEquals(savingRepository.findAll().get(savingRepository.findAll().size()-1).getMinimumBalance(), new BigDecimal("100.00"));
        assertEquals(savingRepository.findAll().get(savingRepository.findAll().size()-1).getInterestRate(), new BigDecimal("0.5000"));
    }



    // ----------------------------------- /admin/new-credit -------------------------------------------------------


    // Checks if it creates a CreditCard account correctly with de default values as interestRate and creditLimit
    @Test
    @WithMockUser(username = "Admin", password = "admin")
    void postNewCredit_Success() throws Exception {

        AccountHolder accountHolder1 = accountHolderRepository.save(new AccountHolder("Eric Cedran", "1234", LocalDate.of(1989,10,8), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España"), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España")));

        CreditCardDTO creditCardDTO = new CreditCardDTO(accountHolder1.getId(), null, new BigDecimal("4500"), null, null);

        String body = objectMapper.writeValueAsString(creditCardDTO);

        MvcResult result = mockMvc.perform(post("/admin/new-credit").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Credit"));
        assertEquals(creditCardRepository.findAll().get(creditCardRepository.findAll().size()-1).getPrimaryOwner().getId(), accountHolder1.getId());
        assertEquals(creditCardRepository.findAll().get(creditCardRepository.findAll().size()-1).getBalance(), new BigDecimal("4500.00"));

        // default values are assigned
        assertEquals(creditCardRepository.findAll().get(creditCardRepository.findAll().size()-1).getCreditLimit(), new BigDecimal("100.00"));
        assertEquals(creditCardRepository.findAll().get(creditCardRepository.findAll().size()-1).getInterestRate(), new BigDecimal("0.2000"));

    }

    // Checks if it creates a CreditCard account correctly with personalized interestRate and creditLimit
    @Test
    @WithMockUser(username = "Admin", password = "admin")
    void postNewCreditPersonalized_Success() throws Exception {

        AccountHolder accountHolder1 = accountHolderRepository.save(new AccountHolder("Eric Cedran", "1234", LocalDate.of(1989,10,8), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España"), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España")));

        CreditCardDTO creditCardDTO = new CreditCardDTO(accountHolder1.getId(), null, new BigDecimal("4500"), new BigDecimal("0.1"), new BigDecimal("10000"));

        String body = objectMapper.writeValueAsString(creditCardDTO);

        MvcResult result = mockMvc.perform(post("/admin/new-credit").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();


        // Personalized values are assigned
        assertEquals(creditCardRepository.findAll().get(creditCardRepository.findAll().size()-1).getCreditLimit(), new BigDecimal("10000.00"));
        assertEquals(creditCardRepository.findAll().get(creditCardRepository.findAll().size()-1).getInterestRate(), new BigDecimal("0.1000"));

    }

    // Checks if it creates a CreditCard account correctly with personalized interestRate and creditLimit that are out of limits
    // and assigns the nearest permitted parameter.
    @Test
    @WithMockUser(username = "Admin", password = "admin")
    void postNewCreditPersonalized2_Success() throws Exception {

        AccountHolder accountHolder1 = accountHolderRepository.save(new AccountHolder("Eric Cedran", "1234", LocalDate.of(1989,10,8), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España"), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España")));

        CreditCardDTO creditCardDTO = new CreditCardDTO(accountHolder1.getId(), null, new BigDecimal("4500"), new BigDecimal("0.5"), new BigDecimal("20000000"));

        String body = objectMapper.writeValueAsString(creditCardDTO);

        MvcResult result = mockMvc.perform(post("/admin/new-credit").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();


        // Personalized values are assigned
        assertEquals(creditCardRepository.findAll().get(creditCardRepository.findAll().size()-1).getCreditLimit(), new BigDecimal("100000.00"));
        assertEquals(creditCardRepository.findAll().get(creditCardRepository.findAll().size()-1).getInterestRate(), new BigDecimal("0.2000"));

    }

    // Checks if it creates a CreditCard account correctly with personalized interestRate and creditLimit that are out of limits
    // and assigns the nearest permitted parameter.
    @Test
    @WithMockUser(username = "Admin", password = "admin")
    void postNewCreditPersonalized3_Success() throws Exception {

        AccountHolder accountHolder1 = accountHolderRepository.save(new AccountHolder("Eric Cedran", "1234", LocalDate.of(1989,10,8), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España"), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España")));

        CreditCardDTO creditCardDTO = new CreditCardDTO(accountHolder1.getId(), null, new BigDecimal("4500"), new BigDecimal("0"), new BigDecimal("10"));

        String body = objectMapper.writeValueAsString(creditCardDTO);

        MvcResult result = mockMvc.perform(post("/admin/new-credit").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();


        // Personalized values are assigned
        assertEquals(creditCardRepository.findAll().get(creditCardRepository.findAll().size()-1).getCreditLimit(), new BigDecimal("100.00"));
        assertEquals(creditCardRepository.findAll().get(creditCardRepository.findAll().size()-1).getInterestRate(), new BigDecimal("0.1000"));

    }


    // ------------------------------------ DELETE ACCOUNT --------------------------------------------------------

    @Test
    @WithMockUser(username = "Admin", password = "admin")
    void deleteAccount_Success() throws Exception {

        AccountHolder accountHolder1 = accountHolderRepository.save(new AccountHolder("Eric Cedran", "1234", LocalDate.of(1989,10,8), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España"), new Address("Calle Balmes, 2", "Barcelona", "Barcelona", "08007","España")));

        Checking checking = checkingRepository.save(new Checking(accountHolder1, null, "123456", new BigDecimal("10000")));

        this.mockMvc.perform(MockMvcRequestBuilders
                        .delete("/admin/delete-account?id=1")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());


        assertFalse(checkingRepository.findById(1L).isPresent());
    }

    // ------------------------------------ DELETE THIRD --------------------------------------------------------

    @Test
    @WithMockUser(username = "Admin", password = "admin")
    void deleteThird_Success() throws Exception {

        thirdPartyRepository.save(thirdPartyService.createThirdParty(new ThirdPartyDTO("Amazon")));

        this.mockMvc.perform(MockMvcRequestBuilders
                        .delete("/admin/delete-third?id=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        assertFalse(thirdPartyRepository.findById(1L).isPresent());
    }



}
