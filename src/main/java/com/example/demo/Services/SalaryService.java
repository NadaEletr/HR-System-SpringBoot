package com.example.demo.Services;

import com.example.demo.Classes.Employee;
import com.example.demo.Classes.ExtraPayments;
import com.example.demo.Classes.Salaries;
import com.example.demo.Classes.SalaryDetails;
import com.example.demo.Repositories.AbsenceRepository;
import com.example.demo.Repositories.EmployeeRepository;
import com.example.demo.Repositories.ExtraPaymentsRepository;
import com.example.demo.Repositories.SalaryHistoryRepository;
import com.example.demo.errors.ConflictException;
import com.example.demo.errors.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

@Service
public class SalaryService {
    @Autowired
    EmployeeService employeeService;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    SalaryHistoryRepository salaryHistoryRepository;
    @Autowired
    AbsenceService absenceService;
    @Autowired
    AbsenceRepository absenceRepository;
    @Autowired
    ExtraPaymentsRepository extraPaymentsRepository;
    @Autowired
    ExtraPaymentsService extraPaymentsService;


    public void addExtraPayments(ExtraPayments extraPayments) {

        if (!employeeService.existsById(extraPayments.getEmployee().getId())) {
            throw new NotFoundException("no employee with this id !");
        }
        if (extraPayments.getBonus() < 0) {
            throw new ConflictException("bonus must be positive number");
        }
        if (extraPayments.getRaise() < 0) {
            throw new ConflictException("raise must be positive number");
        }

        Calendar newCalendar = checkDate();
        java.sql.Date date = new Date(newCalendar.getTimeInMillis());
        if (extraPaymentsRepository.existsByEmployeeAndAndDate(extraPayments.getEmployee(), date)) {
            throw new ConflictException("you already inserted extra payments to this employee this month!");
        }
        extraPayments.setDate(date);
        extraPaymentsRepository.save(extraPayments);
    }

    @Scheduled(cron = "0 0 0 25 * *")
    public void generateAllEmployeesMonthlySalary() throws Exception { // update employee each month Salary
        final List<Integer> Employees = employeeRepository.getAllByNationalId();
        for (Integer id : Employees) {
            this.calculateEmployeeMonthlySalary(id);
        }
    }


    public void calculateEmployeeMonthlySalary(int employeeId) throws Exception { //modify this code

        Calendar newCalendar = checkDate();
        java.sql.Date date = new Date(newCalendar.getTimeInMillis());
        Employee employee = employeeService.getEmployeeInfoByID(employeeId);
        SalaryDetails salaryDetails = CreateSalaryDetails(employee, date);
        salaryHistoryRepository.save(salaryDetails);
        if (employee.getLeaves() > employee.getAcceptableLeaves()) {
            employeeRepository.updateMonthlyLeaves(employee.getAcceptableLeaves(), employee.getId());
        }
    }

    private SalaryDetails CreateSalaryDetails(Employee employee, Date date) throws Exception {
        SalaryDetails salaryDetails = new SalaryDetails();
        double bonus = extraPaymentsService.getBonus(employee, date);
        double raise = extraPaymentsService.getRaise(employee, date);
        salaryDetails.setEmployee(employee);
        employee.setGrossSalary(employee.getGrossSalary() + raise);
        salaryDetails.setDate(date);
        salaryDetails.setBonus(bonus);
        salaryDetails.setRaises(raise);
        salaryDetails.setInsurance(Salaries.insurance);
        double employeeTaxes = (Salaries.taxRatio) * employee.getGrossSalary();
        salaryDetails.setTaxes(employeeTaxes);
        double exceededLeaves = absenceService.calculateExceededLeaves(employee);
        salaryDetails.setExceededLeaves(exceededLeaves);
        calculateNetSalary(salaryDetails);
        return salaryDetails;
    }

    public void calculateNetSalary(SalaryDetails salaryDetails) {
        double netSalary = (salaryDetails.getEmployee().getGrossSalary()
                + salaryDetails.getBonus()
                + salaryDetails.getRaises())
                - salaryDetails.getInsurance()
                - salaryDetails.getBonus()
                - salaryDetails.getExceededLeaves();
        salaryDetails.setNetSalary(netSalary);
    }

    private Calendar checkDate() {
        int requirementDay = 25;
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.DATE) <= requirementDay) {
            calendar.set(Calendar.DAY_OF_MONTH, requirementDay);
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, requirementDay);
            calendar.add(Calendar.MONTH, 1);
        }
        return calendar;
    }
    public List<SalaryDetails> getEmployeeSalaryHistory(int employeeId) {
        Employee employee = employeeService.getEmployeeInfoByID(employeeId);
        if (!salaryHistoryRepository.existsByEmployee(employee)) {
            throw new NotFoundException("No data entered yet!");
        }
        return salaryHistoryRepository.findAllByEmployee(employee);
    }
}
