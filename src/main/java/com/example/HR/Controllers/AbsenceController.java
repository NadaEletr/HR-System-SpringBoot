package com.example.HR.Controllers;

import com.example.HR.Classes.Absence;
import com.example.HR.Repositories.EmployeeRepository;
import com.example.HR.Repositories.UserAccountRepository;
import com.example.HR.Security.UserAccount;
import com.example.HR.Security.UserDetailPrincipalService;
import com.example.HR.Services.AbsenceService;
import com.example.HR.Services.EmployeeService;
import com.example.HR.errors.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
