package com.example.demo.Controllers;

import com.example.demo.Classes.Employee;
import com.example.demo.Classes.SalaryDTO;
import com.example.demo.Classes.Teams;
import com.example.demo.Repositories.EmployeeRepository;
import com.example.demo.Repositories.UserAccountRepository;
import com.example.demo.Security.UserAccount;
import com.example.demo.Security.UserDetailPrincipalService;
import com.example.demo.Services.EmployeeService;
import com.example.demo.Services.VacationService;
import com.example.demo.errors.InvalidCredentialsException;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/user/record/leave")
public class VacationController {
    @Autowired
    VacationService vacationService;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    UserAccountRepository userAccountRepository;
    @Autowired
    UserDetailPrincipalService userDetailPrincipalService;
    @Autowired
    EmployeeRepository employeeRepository;
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String
    recordLeave() throws NotFoundException {
        UserAccount userAccount =userDetailPrincipalService.getCurrentUser();
        vacationService.recordLeave(userAccount.getEmployee().getNationalId());
        return "your absence are "+userAccount.getEmployee().getLeaves();
    }

}
