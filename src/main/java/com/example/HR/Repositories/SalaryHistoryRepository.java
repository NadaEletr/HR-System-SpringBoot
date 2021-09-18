package com.example.HR.Repositories;

import com.example.HR.Classes.Employee;
import com.example.HR.Classes.SalaryDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalaryHistoryRepository extends JpaRepository<SalaryDetails, Integer> {
    List<SalaryDetails> findAllByEmployee(Employee employee);

    boolean existsByEmployee(Employee employee);

       Optional<SalaryDetails> getByEmployeeAndDate(Employee employee, Date date);
}
