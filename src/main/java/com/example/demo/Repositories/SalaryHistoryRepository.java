package com.example.demo.Repositories;

import com.example.demo.Classes.Employee;
import com.example.demo.Classes.SalaryDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface SalaryHistoryRepository extends JpaRepository<SalaryDetails, Integer> {
    //     List<SalaryDetails> findAllByEmployeeId(int employeeId);
    // SalaryDetails getByEmployeeId(int nationalId);
    List<SalaryDetails> findAllByEmployee(Employee employee);

    boolean existsByEmployee(Employee employee);

    SalaryDetails getByEmployee(Employee employee);

    @Modifying
    @Query(value = "update SalaryDetails s set s.raises=?2 where s.employee=?1 and s.date=?3")
    void setRaise(Employee employee, double raise, Date date);
}
