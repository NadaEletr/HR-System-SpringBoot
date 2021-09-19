package com.example.HR.Repositories;

import com.example.HR.Classes.Employee;
import com.example.HR.Classes.Earnings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;
import java.util.Optional;


public interface EarningsRepository extends JpaRepository<Earnings, Integer> {
    @Query(value = "SELECT e.bonus from Earnings e where e.employee=?1 and e.date=?2")
    Double getBonusByDateAndEmployee(Employee employee, Date date);

    @Query(value = "SELECT e.raise from Earnings e where e.employee=?1 and e.date=?2")
    Double getRaiseByDateAndEmployee(Employee employee, Date date);

    @Query("select case when count(e)> 0 then true else false end from Earnings e where e.employee=?1")
    Boolean existsByEmployee(Employee employee);

    @Query("select case when count(e)> 0 then true else false end from Earnings e where e.employee=?1 and  e.date=?2")
    Boolean existsByEmployeeAndAndDate(Employee employee, Date date);
    @Query(value ="DELETE from Earnings e where e.employee = ?1")
    @Modifying()
    void deleteAllByEmployee(Employee employee);
    List<Earnings> findAllByEmployee(Employee employee);

    Optional<Earnings> getByEmployeeAndDate(Employee employee, Date date);
}
