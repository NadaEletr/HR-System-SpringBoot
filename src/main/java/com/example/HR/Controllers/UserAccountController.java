package com.example.HR.Controllers;

import com.example.HR.Classes.Department;
import com.example.HR.Classes.Teams;
import com.example.HR.Repositories.UserAccountRepository;
import com.example.HR.Security.UserAccount;
import com.example.HR.Security.UserDetailPrincipalService;
import com.example.HR.Services.EmployeeService;
import com.example.HR.errors.InvalidCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
public class UserAccountController {
    @Autowired
    UserDetailPrincipalService userDetailPrincipalService;
    @Autowired
    UserAccountRepository userAccountRepository;
    @Autowired
    EmployeeService employeeService;
    @PutMapping(path = "/changePassword")
    @ResponseBody
    public String changePassword(@RequestBody String password) {
        if (password.length() < 4) {
            throw new InvalidCredentialsException("week password!, password must be at least 4 characters");
        }
        userDetailPrincipalService.changePassword(password);
        return "password is changed successfully";
    }
    @GetMapping(value = "/get/Department", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Department userGetDepartment() {
        UserAccount userAccount = userDetailPrincipalService.getCurrentUser();
        return employeeService.userGetDepartment(userAccount.getEmployee());
    }
    @GetMapping(value = "/get/team", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Teams userGetTeam() {
        UserAccount userAccount = userDetailPrincipalService.getCurrentUser();
        return employeeService.userGetTeam(userAccount.getEmployee());
    }

}
