package com.example.HR.Services;

import com.example.HR.Classes.Employee;
import com.example.HR.Classes.Earnings;
import com.example.HR.Classes.Salaries;
import com.example.HR.Classes.SalaryDetails;
import com.example.HR.Repositories.AbsenceRepository;
import com.example.HR.Repositories.EmployeeRepository;
import com.example.HR.Repositories.EarningsRepository;
import com.example.HR.Repositories.SalaryHistoryRepository;
import com.example.HR.errors.ConflictException;
import com.example.HR.errors.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

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
    EarningsRepository earningsRepository;
    @Autowired
    EarningsService earningsService;


    public void addExtraPayments(Earnings earnings) {
        if(earnings.getEmployee()==null){
            throw new NotFoundException("no employee is found");
        }
        if (!employeeService.existsById(earnings.getEmployee().getId())) {
            throw new NotFoundException("no employee with this id !");
        }
        if (earnings.getBonus() < 0) {
            throw new ConflictException("bonus must be positive number");
        }
        if (earnings.getRaise() < 0) {
            throw new ConflictException("raise must be positive number");
        }

        Calendar newCalendar = checkDate();
        java.sql.Date date = new Date(newCalendar.getTimeInMillis());
        if (earningsRepository.existsByEmployeeAndAndDate(earnings.getEmployee(), date)) {
            throw new ConflictException("you already inserted extra payments to this employee this month!");
        }
        earnings.setDate(date);
        earningsRepository.save(earnings);
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

    private SalaryDetails CreateSalaryDetails(Employee employee, Date date) {
        SalaryDetails salaryDetails = new SalaryDetails();
        double bonus = earningsService.getBonus(employee, date);
        double raise = earningsService.getRaise(employee, date);
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

    public List<Earnings> getEarnings(int id) {
        Employee employee = employeeService.getEmployeeInfoByID(id);
        return earningsRepository.findAllByEmployee(employee);
    }
    public SalaryDetails getEmployeeSalaryByMonth(int id , Date date){
        Employee employee =employeeService.getEmployeeInfoByID(id);
        try {
            Optional<SalaryDetails> salaryDetails = salaryHistoryRepository.getByEmployeeAndDate(employee, date);
            return  salaryDetails.get();
        }catch (Exception e){
            e.printStackTrace();
            throw  new NotFoundException("can't find salary by this date or employee!" );
        }
    }

    public Earnings getEarningsByDate(int id, Date date) {
        Employee employee =employeeService.getEmployeeInfoByID((id));
        try {
            Optional<Earnings> earnings = earningsRepository.getByEmployeeAndDate(employee, date);
            return  earnings.get();
        }catch (Exception e){
            e.printStackTrace();
            throw  new NotFoundException("can't find earnings  by this date or employee!" );
        }
    }

    public void deleteSalaryHistory(Employee employee) {
        try{
            salaryHistoryRepository.deleteAllByEmployee(employee);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
