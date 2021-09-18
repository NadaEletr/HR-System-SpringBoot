package com.example.HR.Repositories;

import com.example.HR.Classes.Absence;
import com.example.HR.Classes.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AbsenceRepository extends JpaRepository<Absence, Integer> {

    boolean existsByEmployeeAndDate(Employee employee, Date date);

    Absence findByEmployee(Employee employee);


    List<Absence> findAllByEmployee_Id(int employee_id);
}
