package account.api.Controller;

import account.api.DTO.AccountNameDTO;
import account.api.DTO.AccountNumberDTO;
import account.api.DTO.CustomerDTO;
import account.api.Entity.AccountData;
import account.api.Service.AccountService;
import account.api.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/test")
    public String test() {
        return "Hello World";
    }

    @GetMapping("/account/{accountNumber}")
    public AccountData getAccountData(@PathVariable String accountNumber) {
        return this.accountService.fetchDetails(accountNumber);
    }

    @PatchMapping(value = "/account/{accountNumber}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> patchAccountData(@PathVariable String accountNumber, @RequestParam AccountNameDTO accountNameDTO) {
        AccountData accountData = this.accountService.fetchDetails(accountNumber);
         if (accountData == null) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        this.accountService.modifyAccountName(accountNumber, accountNameDTO.accountName);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/account/{accountNumber}")
    public ResponseEntity<String> closeAccount(@PathVariable String accountNumber) {
        AccountData accountData = this.accountService.fetchDetails(accountNumber);
        if (accountData == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        this.accountService.close(accountNumber);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping(value = "/account", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AccountNumberDTO> createAccount(@RequestBody CustomerDTO customerDTO) {

        if (customerDTO.personalId == null || customerDTO.personalId.trim().length() == 0) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        if (!this.customerService.hasCustomerFile(customerDTO.personalId, customerDTO.firstName, customerDTO.lastName)) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        String accountNumber = this.accountService.createAndReturnAccountNumber(
                customerDTO.personalId,
                customerDTO.productType,
                customerDTO.accountName);

        return ResponseEntity.status(HttpStatus.OK).body(new AccountNumberDTO(accountNumber));
    }
}
