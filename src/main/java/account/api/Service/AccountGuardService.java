package account.api.Service;

import account.api.DTO.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AccountGuardService {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private AccountService accountService;

    public HttpStatus canCreateNewAccount(CustomerDTO customerDTO) {
        if (customerDTO.getPersonalId() == null || customerDTO.getPersonalId().trim().length() == 0) {
            return HttpStatus.UNPROCESSABLE_ENTITY;
        }

        if (!this.customerService.hasCustomerFile(customerDTO.getPersonalId(), customerDTO.getFirstName(), customerDTO.getLastName())) {
            return HttpStatus.NOT_FOUND;
        }

        return null;
    }

    public HttpStatus checkAccountIsFound(String accountNumber) {
        if (this.accountService.fetchDetails(accountNumber) == null) {
           return HttpStatus.NOT_FOUND;
        }

        return null;
    }
}
