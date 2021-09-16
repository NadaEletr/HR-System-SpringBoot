package com.example.demo.Controllers;

import com.example.demo.Classes.Absence;
import com.example.demo.Classes.Employee;
import com.example.demo.Repositories.EmployeeRepository;
import com.example.demo.Repositories.UserAccountRepository;
import com.example.demo.Security.UserAccount;
import com.example.demo.Security.UserDetailPrincipalService;
import com.example.demo.Services.EmployeeService;
import com.example.demo.Services.AbsenceService;
import com.example.demo.errors.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/absence")
public class AbsenceController {
    @Autowired
    AbsenceService absenceService;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    UserAccountRepository userAccountRepository;
    @Autowired
    UserDetailPrincipalService userDetailPrincipalService;
    @Autowired
    EmployeeRepository employeeRepository;

    @PostMapping(value = "/record", produces = MediaType.APPLICATION_JSON_VALUE)
    public String
    recordLeave() throws NotFoundException {
        UserAccount userAccount = userDetailPrincipalService.getCurrentUser();
        absenceService.recordLeave(userAccount.getEmployee().getId());
        return "your Absence are " + userAccount.getEmployee().getLeaves();
    }

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<Absence> getAbsence() {
        UserAccount userAccount = userDetailPrincipalService.getCurrentUser();
        return absenceService.getAbsence(userAccount.getEmployee().getId());
    }
//    @GetMapping(value = "/getLeaves", produces = MediaType.APPLICATION_JSON_VALUE)
//    public @ResponseBody
//    List<Absence> getEmployeeAbsence(@RequestParam("")) {
//        UserAccount userAccount =userDetailPrincipalService.getCurrentUser();
//        return absenceService.getAbsence(userAccount.getEmployee().getId());
//    }


}
