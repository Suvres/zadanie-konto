package account.api.Controller;

import account.api.DTO.AccountNameDTO;
import account.api.DTO.AccountNumberDTO;
import account.api.DTO.CustomerDTO;
import account.api.Entity.AccountData;
import account.api.Service.AccountGuardService;
import account.api.Service.AccountService;
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
    private AccountGuardService accountGuardService;

    @GetMapping(value = "/account/{accountNumber}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AccountData> getAccountData(@PathVariable String accountNumber) {
        AccountData data = this.accountService.fetchDetails(accountNumber);

        if(data == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @PatchMapping(value = "/account/{accountNumber}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> patchAccountData(@PathVariable String accountNumber, @RequestBody AccountNameDTO accountNameDTO) {
        HttpStatus checkStatus = this.accountGuardService.checkAccountIsFound(accountNumber);
        if (checkStatus != null) {
             return ResponseEntity.status(checkStatus).build();
        }

        this.accountService.modifyAccountName(accountNumber, accountNameDTO.getAccountName());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/account/{accountNumber}")
    public ResponseEntity<String> closeAccount(@PathVariable String accountNumber) throws Exception {
        HttpStatus checkStatus = this.accountGuardService.checkAccountIsFound(accountNumber);
        if (checkStatus != null) {
            return ResponseEntity.status(checkStatus).build();
        }

        this.accountService.close(accountNumber);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping(value = "/account", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AccountNumberDTO> createAccount(@RequestBody CustomerDTO customerDTO) {
        HttpStatus status = this.accountGuardService.canCreateNewAccount(customerDTO);
        if(status != null) {
            return ResponseEntity.status(status).build();
        }

        String accountNumber = this.accountService.createAndReturnAccountNumber(
                customerDTO.getPersonalId(),
                customerDTO.getProductType(),
                customerDTO.getAccountName());

        // Error on account create -> for example, same productType
        if (accountNumber == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(new AccountNumberDTO(accountNumber));
    }
}
