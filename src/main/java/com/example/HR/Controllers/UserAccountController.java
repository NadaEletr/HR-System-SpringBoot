package com.example.HR.Controllers;

import com.example.HR.Repositories.UserAccountRepository;
import com.example.HR.Security.UserDetailPrincipalService;
import com.example.HR.errors.InvalidCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
public class UserAccountController {
    @Autowired
    UserDetailPrincipalService userDetailPrincipalService;
    @Autowired
    UserAccountRepository userAccountRepository;

    @PutMapping(path = "/changePassword")
    @ResponseBody
    public String changePassword(@RequestBody String password) {
        if (password.length() < 4) {
            throw new InvalidCredentialsException("week password!, password must be at least 4 characters");
        }
        userDetailPrincipalService.changePassword(password);
        return "password is changed successfully";
    }

}
