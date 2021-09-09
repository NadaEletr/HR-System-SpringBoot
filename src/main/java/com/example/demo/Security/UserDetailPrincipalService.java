package com.example.demo.Security;

import com.example.demo.Classes.Employee;
import com.example.demo.Repositories.EmployeeRepository;
import com.example.demo.Repositories.UserAccountRepository;
import com.example.demo.errors.InvalidCredentialsException;
import com.example.demo.errors.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Service
@Slf4j
public class UserDetailPrincipalService implements UserDetailsService {
    @Autowired
    UserAccountRepository userAccountRepository;
    @Autowired
    EmployeeRepository employeeRepository;

    public void UserPrincipalDetailsService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }


    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = this.userAccountRepository.getById(username);
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
        Optional<UserAccount> userAccount = this.userAccountRepository.findById(this.getLoggedUserName());
        if (!userAccount.isPresent()) {
            throw new InvalidCredentialsException(" user is not found! please try again");
        }
        userAccount.get().setPassword(passwordEncoder().encode(newPassword));
        userAccountRepository.save(userAccount.get());

    }

    public UserAccount getCurrentUser() {
        Optional<UserAccount> userAccount = userAccountRepository.findById(this.getLoggedUserName());
        if (!userAccount.isPresent()) {
            throw new InvalidCredentialsException("this account does not exists!");
        }
        Optional<Employee> employee = employeeRepository.findById(userAccount.get().getEmployee().getNationalId());
        if (!employee.isPresent()) {
            throw new NotFoundException("this employee does not exists!");
        }
        return userAccount.get();
    }
}
