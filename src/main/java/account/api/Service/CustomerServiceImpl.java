package account.api.Service;

import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Override
    public boolean hasCustomerFile(String personalId, String firstName, String lastName) {
        return false;
    }
}
