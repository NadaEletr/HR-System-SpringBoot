package com.example.HR.Controllers;


import com.example.HR.Classes.Earnings;
import com.example.HR.DTO.SalaryDTO;
import com.example.HR.Classes.SalaryDetails;
import com.example.HR.Repositories.EmployeeRepository;
import com.example.HR.Repositories.UserAccountRepository;
import com.example.HR.Security.UserAccount;
import com.example.HR.Security.UserDetailPrincipalService;
import com.example.HR.Services.EarningsService;
import com.example.HR.Services.EmployeeService;
import com.example.HR.Services.SalaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/Salary")
public class SalaryController {
    @Autowired
    SalaryService salaryService;
    @Autowired
    UserAccountRepository userAccountRepository;
    @Autowired
    UserDetailPrincipalService userDetailPrincipalService;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    EarningsService earningsService;

    @GetMapping(value = "/get/UserSalaryHistory", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<SalaryDetails> getEmployeeSalaryHistory() {
        UserAccount userAccount = userDetailPrincipalService.getCurrentUser();
        return salaryService.getEmployeeSalaryHistory(userAccount.getEmployee().getId());
    }

    @GetMapping(value = "/get/ActualSalaries", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<SalaryDTO> getEmployeeActualSalaries(@RequestParam("id") String id) {
        SalaryDTO employeeSalary = employeeService.getEmployeeSalary(Integer.parseInt(id));
        return new ResponseEntity<>(employeeSalary, HttpStatus.OK);
    }


    @PostMapping(value = "/add/earnings", produces = MediaType.APPLICATION_JSON_VALUE)
    public String addBonusAndRaise(@RequestBody Earnings earnings) {
        salaryService.addExtraPayments(earnings);
        return "extra payments is added!";
    }
    @GetMapping(value = "/get/earnings", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<Earnings> getBonusAndRaise(@RequestParam("id") String id) {
        return salaryService.getEarnings(Integer.parseInt(id));
    }
    @GetMapping(value = "/get/UserEarnings", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<Earnings> getUserBonusAndRaise() {
        UserAccount userAccount = userDetailPrincipalService.getCurrentUser();
        return salaryService.getEarnings(userAccount.getEmployee().getId());
    }
    @GetMapping(value = "/get/SalaryHistory", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<SalaryDetails> getSalaryHistoryById(@RequestParam("id") String id) {
        return salaryService.getEmployeeSalaryHistory(Integer.parseInt(id));
    }

    @GetMapping(value = "/get/EarningsByDate", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
        Earnings HRGetBonusAndEarningsBYDate(@RequestParam("id") String id,@RequestBody @DateTimeFormat Date date) {
        return salaryService.getEarningsByDate(Integer.parseInt(id),date);

    }

    @GetMapping(value = "/get/UserEarningsByDate", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Earnings EmployeeGetBonusAndEarningsBYDate(@RequestBody @DateTimeFormat Date date) {
        UserAccount userAccount = userDetailPrincipalService.getCurrentUser();
        return salaryService.getEarningsByDate(userAccount.getEmployee().getId(),date);

    }
    @GetMapping(value = "/get/UserActualSalaries", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<SalaryDTO> userGetActualSalaries() {
        UserAccount userAccount = userDetailPrincipalService.getCurrentUser();
        SalaryDTO employeeSalary = employeeService.getEmployeeSalary(userAccount.getEmployee().getId());
        return new ResponseEntity<>(employeeSalary, HttpStatus.OK);
    }
    @GetMapping(value = "/get/UserSalaryByDate", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<SalaryDetails> userGetSalaryByDate(@RequestBody @DateTimeFormat Date date) {
        UserAccount userAccount = userDetailPrincipalService.getCurrentUser();
        SalaryDetails employeeSalary = salaryService.getEmployeeSalaryByMonth(userAccount.getEmployee().getId(),date);
        return new ResponseEntity<>(employeeSalary, HttpStatus.OK);
    }
    @GetMapping(value = "/get/SalaryByDate", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<SalaryDetails> HRGetSalaryByDate(@RequestParam("id") String id,@RequestBody @DateTimeFormat Date date) {
        SalaryDetails employeeSalary = salaryService.getEmployeeSalaryByMonth(Integer.parseInt(id),date);
        return new ResponseEntity<>(employeeSalary, HttpStatus.OK);
    }


}
