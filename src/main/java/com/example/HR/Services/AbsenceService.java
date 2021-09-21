package com.example.HR.Services;

import com.example.HR.Classes.Absence;
import com.example.HR.Classes.Employee;
import com.example.HR.Repositories.AbsenceRepository;
import com.example.HR.Repositories.EmployeeRepository;
import com.example.HR.errors.ConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class AbsenceService {

    @Autowired
    AbsenceRepository absenceRepository;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    SalaryService salaryHistoryService;

    public void recordLeave(int id,Date date) {
//        Date date = Date.valueOf(LocalDate.now());
        Employee employee = employeeService.getEmployeeInfoByID(id);
        if (absenceRepository.existsByEmployeeAndDate(employee, date)) {
            throw new ConflictException("you are already recorded this day !");
        }
        employee.setLeaves(employee.getLeaves() + 1);
        Absence absence = new Absence(date);
        absence.setEmployee(employee);
        absenceRepository.save(absence);
        employeeRepository.save(employee);
    }

    public double calculateExceededLeaves(Employee employee) {
        int leaves = employee.getLeaves();
        if (leaves > employee.getAcceptableLeaves()) {
            final int workingDays = 22;
            double dayPayed = employee.getGrossSalary() / workingDays;
            return (leaves - employee.getAcceptableLeaves()) * dayPayed;
        } else {
            return 0.0;
        }

    }

    public List<Absence> getAbsence(int id) {
        Employee employee = employeeService.getEmployeeInfoByID(id);
        return absenceRepository.findAllByEmployee_Id(employee.getId());

    }

    public void deleteAbsence(Employee employee) {
       try {
           absenceRepository.deleteAllByEmployee(employee);
       }catch (Exception e){
           e.printStackTrace();
       }
    }
}
