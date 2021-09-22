package com.example.HR.Security;

import com.example.HR.Classes.Employee;
import com.example.HR.Repositories.EmployeeRepository;
import com.example.HR.Repositories.UserAccountRepository;
import com.example.HR.errors.InvalidCredentialsException;
import com.example.HR.errors.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserDetailPrincipalService implements UserDetailsService {
    @Autowired
    UserAccountRepository userAccountRepository;
    @Autowired
    EmployeeRepository employeeRepository;

    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = this.userAccountRepository.findById(username).orElse(null);
        if (userAccount == null) {
            throw new InvalidCredentialsException("user not found");
        }
        return new UserPrincipal(userAccount);

    }

    public String getLoggedUserName() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return authentication.getName();
    }


    public void changePassword(String newPassword) {
        UserAccount userAccount = this.userAccountRepository.findById(this.getLoggedUserName()).orElse(null);
        if (userAccount == null) {
            throw new InvalidCredentialsException(" user is not found! please try again");
        }
        userAccount.setPassword(passwordEncoder().encode(newPassword));
        System.out.println("**************"+userAccount.getPassword());
        userAccountRepository.save(userAccount);

    }

    public UserAccount getCurrentUser() {
        Optional<UserAccount> userAccount = userAccountRepository.findById(this.getLoggedUserName());
        if (!userAccount.isPresent()) {
            throw new InvalidCredentialsException("this account does not exists!");
        }
        Optional<Employee> employee = employeeRepository.findById(userAccount.get().getEmployee().getId());
        if (!employee.isPresent()) {
            throw new NotFoundException("this employee does not exists!");
        }
        return userAccount.get();
    }
}
