package com.example.demo.Controllers;


import com.example.demo.Classes.ExtraPayments;
import com.example.demo.Classes.SalaryDTO;
import com.example.demo.Classes.SalaryDetails;
import com.example.demo.Repositories.EmployeeRepository;
import com.example.demo.Repositories.UserAccountRepository;
import com.example.demo.Security.UserAccount;
import com.example.demo.Security.UserDetailPrincipalService;
import com.example.demo.Services.EmployeeService;
import com.example.demo.Services.SalaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = "/get/SalaryHistory", produces = MediaType.APPLICATION_JSON_VALUE)
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


    @PostMapping(value = "/add/extraPayments", produces = MediaType.APPLICATION_JSON_VALUE)
    public String addBonusAndRaise(@RequestBody ExtraPayments extraPayments) {
        salaryService.addExtraPayments(extraPayments);
        return "extra payments is added!";
    }

    @GetMapping(value = "/get/SalaryHistory/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<SalaryDetails> getSalaryHistoryById(@RequestParam("id") String id) {
        return salaryService.getEmployeeSalaryHistory(Integer.parseInt(id));
    }

    @GetMapping(value = "/get/UserActualSalaries", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<SalaryDTO> userGetActualSalaries() {
        UserAccount userAccount = userDetailPrincipalService.getCurrentUser();
        SalaryDTO employeeSalary = employeeService.getEmployeeSalary(userAccount.getEmployee().getId());
        return new ResponseEntity<>(employeeSalary, HttpStatus.OK);
    }
}
