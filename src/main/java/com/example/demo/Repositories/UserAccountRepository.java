package com.example.demo.Repositories;

import com.example.demo.Classes.Employee;
import com.example.demo.Security.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {

    @Query("select u.password from  UserAccount u where u.employee=?1")
    String getPassword(Employee employee);

}
