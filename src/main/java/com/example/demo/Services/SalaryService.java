package com.example.demo.Services;

import com.example.demo.Classes.Employee;
import com.example.demo.Classes.ExtraPayments;
import com.example.demo.Classes.Salaries;
import com.example.demo.Classes.SalaryDetails;
import com.example.demo.Repositories.EmployeeRepository;
import com.example.demo.Repositories.ExtraPaymentsRepository;
import com.example.demo.Repositories.SalaryHistoryRepository;
import com.example.demo.Repositories.VacationRepository;
import com.example.demo.errors.ConflictException;
import com.example.demo.errors.NotFoundException;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.scheduling.BackgroundJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDate;


import java.util.Calendar;


import java.util.List;
import java.util.stream.Stream;

@Service
public class SalaryService {
    @Autowired
    EmployeeService employeeService;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    SalaryHistoryRepository salaryHistoryRepository;
    @Autowired
    VacationService vacationService;
    @Autowired
    VacationRepository vacationRepository;
    @Autowired
    ExtraPaymentsRepository extraPaymentsRepository;
    @Autowired
    ExtraPaymentsService extraPaymentsService;


    public ExtraPayments addExtraPayments(ExtraPayments extraPayments) {

        if(!employeeService.existsById(extraPayments.getEmployee().getNationalId()))
        {
            throw new NotFoundException("no employee with this id !");
        }
        if (extraPayments.getBonus() < 0) {
            throw new ConflictException("bonus must be positive number");
        }
        if(extraPayments.getRaise()<0)
        {
            throw new ConflictException("raise must be positive number");
        }
        int requirementDay=25;
        Calendar calendar= Calendar.getInstance();
        if(calendar.get(Calendar.DATE)<=requirementDay)
        {
            calendar.set(Calendar.DAY_OF_MONTH,requirementDay);
        }
        else
        {
            calendar.set(Calendar.DAY_OF_MONTH,requirementDay);
            calendar.add(Calendar.MONTH,1);
        }
        java.sql.Date date = new Date (calendar.getTimeInMillis());
        if(extraPaymentsRepository.existsByEmployeeAndAndDate(extraPayments.getEmployee(), date)){
            throw new ConflictException("you already inserted extra payments to this employee this month!");
        }
        extraPayments.setDate(date);
       return  extraPaymentsRepository.save(extraPayments);
    }

    public void exceededLeaves(int employeeId) {

    }
    @Scheduled(fixedDelay = 5000)
    public void generateAllEmployeesMonthlySalary() throws Exception { // update employee each month Salary
        final List<Integer> Employees = employeeRepository.getAllByNationalId();
        for(Integer id: Employees){
            calculateEmployeeMonthlySalary(id);
        }
    }


    public void calculateEmployeeMonthlySalary(int employeeId) throws Exception {
        Calendar calendar= Calendar.getInstance();
        int requirementDay=25;
        if(calendar.get(Calendar.DATE)<=requirementDay)
        {
            calendar.set(Calendar.DAY_OF_MONTH,requirementDay);
        }
        else
        {
            calendar.set(Calendar.DAY_OF_MONTH,requirementDay);
            calendar.add(Calendar.MONTH,1);
        }
        java.sql.Date date = new Date (calendar.getTimeInMillis());
        Employee employee = employeeService.getEmployeeInfoByID(employeeId);
        SalaryDetails salaryDetails =new SalaryDetails();
        double bonus= extraPaymentsService.getBonus(employee, date);
        double raise = extraPaymentsService.getRaise(employee,  date);
        employee.setGrossSalary(employee.getGrossSalary()+raise);
        salaryDetails.setDate(date);
        salaryDetails.setBonus(bonus);
        salaryDetails.setRaises(raise);
        salaryDetails.setInsurance(Salaries.insurance);
        double employeeTaxes = (Salaries.taxRatio) * employee.getGrossSalary();
        salaryDetails.setTaxes(employeeTaxes);
        double netSalary=(employee.getGrossSalary()+bonus+raise)-Salaries.insurance-employeeTaxes;
        salaryDetails.setNetSalary(netSalary);
        salaryDetails.setEmployee(employee);
        salaryHistoryRepository.save(salaryDetails);
    }

    public List<SalaryDetails> getEmployeeSalaryHistory(int employeeId) {
        Employee employee = employeeService.getEmployeeInfoByID(employeeId);
        if (!salaryHistoryRepository.existsByEmployee(employee)) {
            throw new NotFoundException("No data entered yet!");
        }
        return salaryHistoryRepository.findAllByEmployee(employee);
    }


//    @Transactional
//    @Job(name = "Generate salary to all employees")
//    @Scheduled(cron = "0 0 0 25 * *") //each month on day 25
//    public void generateSalaryAllEmployees() {
//        final Stream<Integer> Employees = employeeRepository.getAllByNationalId();
//        BackgroundJob.enqueue(Employees, (employeeId) -> calculateEmployeeMonthlySalary(employeeId));
//    }

}

//    Employee employee = employeeService.getEmployeeInfoByID(employeeId);
//    SalaryDetails salaryDetails = new SalaryDetails();
//    Date date = Date.valueOf(LocalDate.now());
//        salaryDetails.setDate(date);
//                double employeeTaxes = (Salaries.taxRatio) * employee.getGrossSalary();
//                salaryDetails.setTaxes(employeeTaxes);
//                salaryDetails.setRaises(employee.getSalaryRaise());
//                salaryDetails.setInsurance(Salaries.insurance);
//                vacationService.checkExceededLeave(employee);
//                //salaryDetails.setExceededLeaves();
//                //set bonus
//
//
//                double totalDeductions = Salaries.insurance + employeeTaxes;
//                salaryDetails.setNetSalary(employee.getGrossSalary() - totalDeductions);
//                salaryDetails.setEmployee(employee);
////                salaryHistoryRepository.save(salaryDetails);

//cron = "0 0 0 25 * *"






