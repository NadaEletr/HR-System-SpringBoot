package com.example.demo.Repositories;

import com.example.demo.Classes.Employee;
import com.example.demo.Security.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {



    void deleteByEmployee(Employee employee);

}
