package account.api.Service;

import account.api.Entity.AccountData;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService{
    @Override
    public String createAndReturnAccountNumber(String personalId, String productType, String accountName) {
        return null;
    }

    @Override
    public void modifyAccountName(String accountNumber, String accountName) {

    }

    @Override
    public AccountData fetchDetails(String accountNumber) {
        return null;
    }

    @Override
    public void close(String accountNumber) {

    }
}
