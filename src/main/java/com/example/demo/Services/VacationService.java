package com.example.demo.Services;

import com.example.demo.Classes.Employee;
import com.example.demo.Classes.Vacations;
import com.example.demo.Repositories.EmployeeRepository;
import com.example.demo.Repositories.VacationRepository;
import com.example.demo.errors.ConflictException;
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

            Date date = Date.valueOf(LocalDate.now());
            Employee employee = employeeService.getEmployeeInfoByID(id);
            if(vacationRepository.existsByEmployeeAndDate(employee,date))
            {
                throw new ConflictException("you are already recorded this day !");
            }
            employee.setLeaves(employee.getLeaves() + 1);
            Vacations vacations = new Vacations(date);
            vacations.setEmployee(employee);
            vacationRepository.save(vacations);
            employeeRepository.save(employee);
    }
    public void checkExceededLeave(Employee employee)
    {
        
    }

}
