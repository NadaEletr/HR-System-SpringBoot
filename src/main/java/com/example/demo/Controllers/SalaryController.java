package com.example.demo.Controllers;


import com.example.demo.Classes.Employee;
import com.example.demo.Classes.ExtraPayments;
import com.example.demo.Classes.SalaryDetails;
import com.example.demo.Repositories.EmployeeRepository;
import com.example.demo.Repositories.UserAccountRepository;
import com.example.demo.Security.UserAccount;
import com.example.demo.Security.UserDetailPrincipalService;
import com.example.demo.Services.SalaryService;
import com.example.demo.errors.InvalidCredentialsException;
import com.example.demo.errors.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(value = "/SalaryHistory")
public class SalaryController {
    @Autowired
    SalaryService salaryService;
    @Autowired
    UserAccountRepository userAccountRepository;
    @Autowired
    UserDetailPrincipalService userDetailPrincipalService;
    @Autowired
    EmployeeRepository employeeRepository;

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<SalaryDetails> getEmployeeSalaryHistory() {
        UserAccount userAccount = userDetailPrincipalService.getCurrentUser();
        return salaryService.getEmployeeSalaryHistory(userAccount.getEmployee().getId());
    }

    @PostMapping(value = "/add/extraPayments", produces = MediaType.APPLICATION_JSON_VALUE)
    public String addBonusAndRaise(@RequestBody ExtraPayments extraPayments) {
        salaryService.addExtraPayments(extraPayments);
        return "extra payments is added!";
    }
    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<SalaryDetails> getEmployeeSalaryHistoryById(@RequestParam("id") String id) {
        return salaryService.getEmployeeSalaryHistory(Integer.parseInt(id));
    }
}
