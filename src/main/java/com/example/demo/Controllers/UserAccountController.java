package com.example.demo.Controllers;

import com.example.demo.Classes.Employee;
import com.example.demo.Repositories.UserAccountRepository;
import com.example.demo.Security.UserAccount;
import com.example.demo.Security.UserDetailPrincipalService;
import com.example.demo.Services.EmployeeService;
import com.example.demo.errors.InvalidCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/user")
public class UserAccountController {
    @Autowired
    UserDetailPrincipalService userDetailPrincipalService;
    @Autowired
    UserAccountRepository userAccountRepository;
    @PutMapping(path = "/changePassword")
    @ResponseBody
    public String changePassword(@RequestBody String password)  {
        if(password.length()<4){
            throw new InvalidCredentialsException("week password!, password must be at least 4 characters");
        }
        userDetailPrincipalService.changePassword(password);
        return "password is changed successfully";
    }

}
