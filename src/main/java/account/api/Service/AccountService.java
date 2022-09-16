package account.api.Service;

import account.api.Entity.AccountData;

public interface AccountService {
    String createAndReturnAccountNumber(String personalId, String productType, String accountName);
    void modifyAccountName(String accountNumber, String accountName);
    AccountData fetchDetails(String accountNumber);
    void close(String accountNumber);
}
