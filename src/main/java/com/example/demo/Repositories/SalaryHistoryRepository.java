package com.example.demo.Repositories;

import com.example.demo.Classes.SalaryHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaryHistoryRepository extends JpaRepository<SalaryHistory,Integer> {
    SalaryHistory getByEmployeeId(int nationalId);
}
