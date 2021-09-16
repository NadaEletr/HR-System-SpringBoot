package com.example.demo.Repositories;

import com.example.demo.Classes.Employee;
import com.example.demo.Classes.Earnings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;


public interface EarningsRepository extends JpaRepository<Earnings, Integer> {
    @Query(value = "SELECT e.bonus from Earnings e where e.employee=?1 and e.date=?2")
    Double getBonusByDateAndEmployee(Employee employee, Date date);

    @Query(value = "SELECT e.raise from Earnings e where e.employee=?1 and e.date=?2")
    Double getRaiseByDateAndEmployee(Employee employee, Date date);

    @Query("select case when count(e)> 0 then true else false end from Earnings e where e.employee=?1")
    Boolean existsByEmployee(Employee employee);

    @Query("select case when count(e)> 0 then true else false end from Earnings e where e.employee=?1 and  e.date=?2")
    Boolean existsByEmployeeAndAndDate(Employee employee, Date date);



}
