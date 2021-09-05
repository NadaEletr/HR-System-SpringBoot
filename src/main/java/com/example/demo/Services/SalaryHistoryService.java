package com.example.demo.Services;

import com.example.demo.Classes.Employee;
import com.example.demo.Classes.Salaries;
import com.example.demo.Classes.SalaryHistory;
import com.example.demo.Repositories.EmployeeRepository;
import com.example.demo.Repositories.SalaryHistoryRepository;
import com.example.demo.errors.ConflictException;
import com.example.demo.errors.NotFoundException;
import org.hibernate.service.spi.InjectService;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.scheduling.BackgroundJob;
import org.jobrunr.scheduling.cron.Cron;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.nio.file.Path;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@Service
public class SalaryHistoryService {
    @Autowired
    EmployeeService employeeService;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    SalaryHistoryRepository salaryHistoryRepository;
    @Autowired
    VacationService vacationService;

//
//    public void updateSalary(int employeeId)
//    {
//        Employee employee= employeeService.getEmployeeInfoByID(employeeId);
//        SalaryHistory salaryHistory=new SalaryHistory();
//       if(checkExceededLeaves(employee))
//       {
//            salaryHistory.setExceededLeaves(50);
//           salaryHistory.setNetSalary(employee.getNetSalary()-salaryHistory.getExceededLeaves());
//           salaryHistory.setEmployee(employee);
//           salaryHistoryRepository.save(salaryHistory);
//       }
//
//
//    }
//    public boolean checkExceededLeaves(Employee employee)
//    {
//        int countLeaves =employeeRepository.getLeaves(employee.getNationalId());
//        if(employee.getAcceptableLeaves()== AllowedVacations.LessEXPERIENCED&&countLeaves>AllowedVacations.LessEXPERIENCED)
//        {
//            return true;
//        }
//        else if (employee.getAcceptableLeaves()==AllowedVacations.EXPERIENCED &&countLeaves>AllowedVacations.EXPERIENCED)
//        {
//            return true;
//
//        }
//        return false;
//    }

    public void addRaise(int employeeId, double raise)
    {
        if(raise<0)
        {
            throw  new ConflictException("raise must be positive number");
        }
        Employee employee=employeeService.getEmployeeInfoByID(employeeId);
        employee.setSalaryRaise(raise);
        employee.setGrossSalary(employee.getGrossSalary()+raise);
        employeeService.CalcNetSalary(employee);
        employeeRepository.save(employee);
    }
    public void addBonus(int employeeId,double bonus)
    {

    }
    public void exceededLeaves()
    {

    }
    @Job(name = "Generate salary  to employee %0")
    public void calculateEmployeeMonthlySalary(int employeeId) // update employee each month Salary
    {
       Employee employee= employeeService.getEmployeeInfoByID(employeeId);
       SalaryHistory salaryHistory = new SalaryHistory();
       Date date = Date.valueOf(LocalDate.now());
       salaryHistory.setDate(date);
       double employeeTaxes=(Salaries.taxRatio)*employee.getGrossSalary();
       salaryHistory.setTaxes(employeeTaxes);
       salaryHistory.setRaises(employee.getSalaryRaise());
       salaryHistory.setInsurance(Salaries.insurance);
       vacationService.checkExceededLeave(employee);
       //salaryHistory.setExceededLeaves();
        //set bonus

        double totalDeductions=Salaries.insurance+employeeTaxes;
        salaryHistory.setNetSalary(employee.getGrossSalary()-totalDeductions);
        salaryHistory.setEmployee(employee);
        salaryHistoryRepository.save(salaryHistory);
    }
    public List<SalaryHistory> getEmployeeSalaryHistory(int employeeId)
    {
        Employee employee = employeeService.getEmployeeInfoByID(employeeId);
        if(!salaryHistoryRepository.existsByEmployee(employee))
        {
            throw new NotFoundException("No data entered yet!");
        }
        return salaryHistoryRepository.findAllByEmployee(employee);
    }
//
//    @Transactional
//    @Job(name = "Generate salary to all employees")
//    @Scheduled(cron="0 0 0 1 * *") //each month
//    public void generateSalaryAllEmployees() {
//        final Stream<Integer> Employees = employeeRepository.getAllByNationalId();
//        BackgroundJob.enqueue(Employees, (employeeId) -> calculateEmployeeMonthlySalary(employeeId));
//    }

}
