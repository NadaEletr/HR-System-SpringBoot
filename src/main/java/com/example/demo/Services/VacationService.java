package com.example.demo.Services;

import com.example.demo.Classes.Employee;
import com.example.demo.Classes.Vacations;
import com.example.demo.Repositories.EmployeeRepository;
import com.example.demo.Repositories.VacationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;

@Service
public class VacationService {

    @Autowired
    VacationRepository vacationRepository;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    SalaryHistoryService salaryHistoryService;
    public void recordLeave(int id) {
        Employee employee=employeeService.getEmployeeInfoByID(id);
        Date date = Date.valueOf(LocalDate.now());
        employee.setLeaves(employee.getLeaves()+1);
        Vacations vacations = new Vacations(date);
        vacations.setEmployee(employee);
        vacationRepository.save(vacations);
        employeeRepository.save(employee);
        //salaryHistoryService.updateSalary(employee.getNationalId());

    }
}
