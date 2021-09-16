package com.example.demo.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailPrincipalService userDetailPrincipalService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.
                authorizeRequests().antMatchers("/HR/employee/**").hasRole(Roles.HR.name())
                .antMatchers("/Teams/**").hasRole(Roles.HR.name())
                .antMatchers("/department/**").hasRole(Roles.HR.name())
                .antMatchers("/Salary/add/extraPayments").hasRole(Roles.HR.name())
                .antMatchers("/Salary/get/SalaryHistory").hasAnyRole(Roles.EMPLOYEE.name(),Roles.HR.name())
                .antMatchers("/Salary/get/UserActualSalaries").hasAnyRole(Roles.EMPLOYEE.name(),Roles.HR.name())
                .antMatchers("/Salary/get/ActualSalaries").hasAnyRole(Roles.HR.name())
                .antMatchers("/Salary/get/SalaryHistory/{id}").hasAnyRole(Roles.HR.name())
                .antMatchers("/absence/**").hasAnyRole(Roles.EMPLOYEE.name(),Roles.HR.name())
                .antMatchers("/absence/").hasRole(Roles.EMPLOYEE.name())
                .antMatchers("/user/changePassword").hasAnyRole(Roles.EMPLOYEE.name(),Roles.HR.name())
                .and().httpBasic().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(this.userDetailPrincipalService);
        return daoAuthenticationProvider;
    }
    SecurityConfiguration(UserDetailPrincipalService userDetailPrincipalService) {
        this.userDetailPrincipalService = userDetailPrincipalService;
    }
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
