package com.example.HR.Services;

import com.example.HR.Classes.*;
import com.example.HR.Repositories.AbsenceRepository;
import com.example.HR.Repositories.EmployeeRepository;
import com.example.HR.Repositories.UserAccountRepository;
import com.example.HR.Security.Roles;
import com.example.HR.Security.UserAccount;
import com.example.HR.errors.ConflictException;
import com.example.HR.errors.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Component
public class EmployeeService {


    @Autowired
    public EmployeeRepository employeeRepository;
    @Autowired
    public AbsenceRepository absenceRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    UserAccountService userAccountService;
    @Autowired
    UserAccountRepository userAccountRepository;

    public Employee saveEmployee(Employee employee) {
        checkBeforeSaving(employee);
        generateAcceptedLeave(employee);
        CalcNetSalary(employee);
        return employeeRepository.save(employee);

    }

    private void checkBeforeSaving(Employee employee) {

        if (employee.getNationalId() == null) {
            throw new NotFoundException("national id must not be null");
        }
        if (employee.getFirst_name() == null || employee.getLast_name() == null) {
            throw new NotFoundException("name missing !");
        }
        if(employee.getFirst_name().matches("[a-zA-Z]+") || employee.getLast_name().matches("[a-zA-Z]+")){
            throw new NotFoundException(" name must be characters only");
        }

        if (employee.getYearsOfExperience() == null) {
            throw new NotFoundException("years of experience must not be empty!");
        }
        if (employee.getGrossSalary() == 0d) {
            throw new NotFoundException(" Actual gross salary is missing");
        }

        if (employeeRepository.existsByNationalId(employee.getNationalId())) {
            throw new ConflictException("national id exists before !");
        }
        if (employeeRepository.existsById(employee.getId())) {
            throw new ConflictException("this employee is already added");
        }

        if (employee.getManager() != null && !employeeRepository.existsById(employee.getManager().getId())) {
            throw new NotFoundException(" manager does not exists!");
        }

    }


    public void generateAcceptedLeave(Employee employee) {
        if (employee.getYearsOfExperience() < AllowedVacations.HIGHWORKINGYEARS) {
            employee.setAcceptableLeaves(AllowedVacations.getLessEXPERIENCED());
        } else {
            employee.setAcceptableLeaves(AllowedVacations.getEXPERIENCED());
        }
    }


    public void generatePasswordAndUserName(Employee employee) throws Exception {

        String username = employee.getFirst_name() + employee.getLast_name() + employee.getId();
        String password = employee.getNationalId();
        UserAccount userAccount = new UserAccount();
        userAccount.setUserName(username);
        userAccount.setPassword(passwordEncoder.encode(password));
        userAccount.setRoles(Roles.EMPLOYEE);
        userAccount.setEmployee(employee);
        userAccountService.addUserAccount(userAccount);
    }

    public void CalcNetSalary(Employee employee) {
        final double taxRatio = 0.85;
        final double insurance = 500;
        double netSalary;
        if (employee.getGrossSalary() == 0d) {
            netSalary = 0d;
        }
        if (employee.getGrossSalary() < 0) {
            throw new ConflictException("salary must positive");
        } else {
            netSalary = employee.getGrossSalary() * taxRatio - insurance;
        }
        employee.setNetSalary(netSalary);
    }

    public Employee getEmployeeInfoByID(int id) throws NotFoundException {
        if (!employeeRepository.existsById(id)) {
            throw new NotFoundException("no employee with this ID");
        }
        return employeeRepository.getById(id);
    }



    public boolean existsById(int id) {
        return employeeRepository.existsById(id);
    }


    public void updateEmployee(Employee updateEmployee, Employee originalEmployee) throws NotFoundException {

        transferEmployee(updateEmployee, originalEmployee);

        employeeRepository.save(originalEmployee);
    }

    public SalaryDTO getEmployeeSalary(int id) throws NotFoundException {
        Employee employee = getEmployeeInfoByID(id);
        return new SalaryDTO(employee);
    }

    public List<Employee> getEmployeesInTeam(int teamId) {
        if (employeeRepository.existsByTeamId(teamId) == false) {
            throw new NotFoundException("team does not exists !");
        }
        return employeeRepository.findAllByTeamTeamId(teamId);
    }

    public List<Employee> getEmployeesUnderManger(int mangerId) {
        if (employeeRepository.existsById(mangerId) == false) {
            throw new NotFoundException("no employee with this ID");
        }
        return employeeRepository.findAllByManagerId(mangerId);
    }

    public List<Employee> getEmployeesOnSpeceficManger(int mangerId) {
        if (employeeRepository.existsById(mangerId) == false) {
            throw new NotFoundException("no employee with this ID");
        }
        return employeeRepository.findAllUnderSomeManager(mangerId);
    }

    @Transactional
    public void delete(int employeeId) {

        Employee employee = getEmployeeInfoByID(employeeId);
        if (employee.getManager() == null) {
            throw new NotFoundException(" can't delete employee with no manager");
        }

        for (Employee employee1 : employee.getEmployees()) {
            employee1.setManager(employee.getManager());
            employeeRepository.save(employee);
        }
        userAccountService.deleteAccount(employee);
        employeeRepository.deleteById(employee.getId());


    }

    public void transferEmployee(Employee updateEmployee, Employee originalEmployee) {
        updateEmployee.setId(originalEmployee.getId());
        ModelMapperGenerator.getModelMapperSingleton().map(updateEmployee, originalEmployee);
   }


    public Department userGetDepartment(Employee employee) {
        if(employee.getDepartment()==null){
            throw new NotFoundException("no department is found");
        }
        return employee.getDepartment();
    }

    public Teams userGetTeam(Employee employee) {
        if(employee.getTeam()==null){
            throw new NotFoundException("no Team is found");
        }
        return employee.getTeam();
    }
}
