package com.example.demo.Repositories;

import com.example.demo.Classes.Employee;
import com.example.demo.Classes.SalaryDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalaryHistoryRepository extends JpaRepository<SalaryDetails, Integer> {
    List<SalaryDetails> findAllByEmployee(Employee employee);

    boolean existsByEmployee(Employee employee);



}
