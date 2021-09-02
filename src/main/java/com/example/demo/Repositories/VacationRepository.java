package com.example.demo.Repositories;

import com.example.demo.Classes.Employee;
import com.example.demo.Classes.Vacations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface VacationRepository  extends JpaRepository<Vacations, Integer> {

    boolean existsByEmployeeAndDate(Employee employee, Date date);
}
