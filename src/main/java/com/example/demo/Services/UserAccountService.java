package com.example.demo.Services;

import com.example.demo.Classes.Employee;
import com.example.demo.Repositories.UserAccountRepository;
import com.example.demo.Security.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAccountService {

    @Autowired
    UserAccountRepository userAccountRepository;

    public void addUserAccount(UserAccount userAccount) throws Exception {
        try {

            userAccountRepository.save(userAccount);

        } catch (Exception ex) {
            throw new Exception("can't save user " + userAccount.getEmployee().getFirst_name() + " to database! ");
        }
    }

    public void deleteAccount(Employee employee) {
        userAccountRepository.deleteByEmployee(employee);
    }
}
