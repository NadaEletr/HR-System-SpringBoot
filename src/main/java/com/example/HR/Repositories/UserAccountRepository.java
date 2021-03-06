package com.example.HR.Repositories;

import com.example.HR.Classes.Employee;
import com.example.HR.Security.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {


    @Query(value ="DELETE from UserAccount e where e.employee = ?1")
    @Modifying()
    void deleteByEmployee(Employee employee);

}
