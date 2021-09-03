package com.example.demo.Repositories;

import com.example.demo.Classes.Employee;
import com.example.demo.Classes.SalaryHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface SalaryHistoryRepository extends JpaRepository<SalaryHistory,Integer> {
//     List<SalaryHistory> findAllByEmployeeId(int employeeId);
    // SalaryHistory getByEmployeeId(int nationalId);
     List<SalaryHistory> findAllByEmployee(Employee employee);

     boolean existsByEmployee(Employee employee);

     SalaryHistory getByEmployee(Employee employee);

     @Modifying
     @Query(value = "update SalaryHistory s set s.raises=?2 where s.employee=?1 and s.date=?3")
     void setRaise(Employee employee, double raise, Date date);
}
