package com.example.HR.Repositories;

import com.example.HR.Classes.Employee;
import com.example.HR.Security.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {



    void deleteByEmployee(Employee employee);

}
