package account.api;

import account.api.DTO.AccountNameDTO;
import account.api.DTO.AccountNumberDTO;
import account.api.DTO.CustomerDTO;
import account.api.Entity.AccountData;
import account.api.Service.AccountService;
import account.api.Service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.Assert;

@SpringBootTest()
@AutoConfigureMockMvc
class ZadanieKontoApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private CustomerService customerService;

    @Test
    void testGetAccountByNumber() throws Exception {
        AccountData ac = new AccountData();
        ac.productType = "asd";
        ac.accountNumber = "123";
        ac.accountName = "ROR";
        ac.personalId = "123123123";

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);


        Mockito.when(this.accountService.fetchDetails("123"))
                .thenReturn(ac);

        mockMvc.perform(MockMvcRequestBuilders.get("/account/12"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        mockMvc.perform(MockMvcRequestBuilders.get("/account/123"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(ac)));
    }

    @Test
    void testPatchAccountData() throws Exception {
        AccountData ac = new AccountData();
        ac.productType = "asd";
        ac.accountNumber = "123";
        ac.accountName = "ROR";
        ac.personalId = "123123123";

        AccountNameDTO accountName = new AccountNameDTO();
        accountName.setAccountName("name");

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        Mockito.when(this.accountService.fetchDetails("123"))
                .thenReturn(ac);

        String accountJson = mapper.writeValueAsString(accountName);

        Assert.hasText("accountName", accountJson);

        mockMvc.perform(MockMvcRequestBuilders.patch("/account/12"))
                .andExpect(MockMvcResultMatchers.status().isUnsupportedMediaType());

        mockMvc.perform(MockMvcRequestBuilders.patch("/account/12")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(accountJson))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        mockMvc.perform(MockMvcRequestBuilders.patch("/account/123")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(accountJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testDeleteAccountData() throws Exception {
        AccountData ac = new AccountData();
        ac.productType = "asd";
        ac.accountNumber = "123";
        ac.accountName = "ROR";
        ac.personalId = "123123123";

        Mockito.when(this.accountService.fetchDetails("123"))
                .thenReturn(ac);

        mockMvc.perform(MockMvcRequestBuilders.delete("/account/12"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        mockMvc.perform(MockMvcRequestBuilders.delete("/account/123"))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void testPutAccountDataBadPesel() throws Exception {

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setAccountName("CoffeMoney");
        customerDTO.setFirstName("Tadeusz");
        customerDTO.setLastName("Kowalski");
        customerDTO.setProductType("ROR");

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);


        mockMvc.perform(MockMvcRequestBuilders.put("/account")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(customerDTO)))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

    @Test
    void testPutAccountDataNotFound() throws Exception {

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setAccountName("CoffeMoney");
        customerDTO.setFirstName("Tadeusz");
        customerDTO.setLastName("Kowal");
        customerDTO.setProductType("ROR");
        customerDTO.setPersonalId("99012109890");

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        Mockito.when(this.customerService.hasCustomerFile("99012109890", "Tadeusz", "Kowal"))
                .thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.put("/account")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(customerDTO)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testPutAccountDataNotCreate() throws Exception {

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setAccountName("CoffeMoney");
        customerDTO.setFirstName("Tadeusz");
        customerDTO.setLastName("Kowal");
        customerDTO.setProductType("ROR");
        customerDTO.setPersonalId("99012109890");

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        Mockito.when(this.customerService.hasCustomerFile("99012109890", "Tadeusz", "Kowal"))
                .thenReturn(true);

        Mockito.when(this.accountService.createAndReturnAccountNumber("99012109890", "ROR", "TEST"))
                .thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.put("/account")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(customerDTO)))
                .andExpect(MockMvcResultMatchers.status().isNotAcceptable());
    }

    @Test
    void testPutAccountDataCreate() throws Exception {

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setAccountName("CoffeMoney");
        customerDTO.setFirstName("Tadeusz");
        customerDTO.setLastName("Kowal");
        customerDTO.setProductType("ROR");
        customerDTO.setPersonalId("99012109890");

        AccountNumberDTO accountNumberDTO = new AccountNumberDTO("1234");

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        Mockito.when(this.customerService.hasCustomerFile("99012109890", "Tadeusz", "Kowal"))
                .thenReturn(true);

        Mockito.when(this.accountService.createAndReturnAccountNumber("99012109890", "ROR", "CoffeMoney"))
                .thenReturn(accountNumberDTO.accountNumber);

        mockMvc.perform(MockMvcRequestBuilders.put("/account")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(customerDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(mapper.writeValueAsString(accountNumberDTO)));
    }

}