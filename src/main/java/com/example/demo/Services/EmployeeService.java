package com.example.demo.Services;

import com.example.demo.Classes.Absence;
import com.example.demo.Classes.AllowedVacations;
import com.example.demo.Classes.Employee;
import com.example.demo.Classes.SalaryDTO;
import com.example.demo.Repositories.UserAccountRepository;
import com.example.demo.Security.Roles;
import com.example.demo.Security.UserAccount;
import com.example.demo.Repositories.EmployeeRepository;
import com.example.demo.Repositories.AbsenceRepository;
import com.example.demo.errors.ConflictException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.example.demo.errors.NotFoundException;

import javax.transaction.Transactional;
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

    public Employee saveEmployee(Employee employee) throws Exception {
        checkBeforeSaving(employee);
        generateAcceptedLeave(employee);
        CalcNetSalary(employee);
        return employeeRepository.save(employee);

    }

    private void checkBeforeSaving(Employee employee) {
        if (employeeRepository.existsByNationalId(employee.getNationalId())) {
            throw new ConflictException("national id exists before !");
        }
        if (employeeRepository.existsById(employee.getId())) {
            throw new ConflictException("this employee is already added");
        }
        if (employee.getDepartment() != null && employeeRepository.existsByDepartmentId(employee.getDepartment().getDepartmentId()) == false) {
            throw new NotFoundException("this department does not exists");
        }

        if (employee.getTeam() != null && employeeRepository.existsByTeamId(employee.getTeam().getTeamId()) == false) {
            throw new NotFoundException("this team does not exists");
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

        String username = employee.getFirst_name() + employee.getLast_name()+employee.getId();
        String password = employee.getNationalId();
        UserAccount userAccount = new UserAccount();
        userAccount.setUserName(username);
        userAccount.setPassword(passwordEncoder.encode(password));
        userAccount.setRoles(Roles.EMPLOYEE.name());
        userAccount.setEmployee(employee);
        userAccountService.addUserAccount(userAccount);
    }

    public void CalcNetSalary(Employee employee) {
        final double taxRatio = 0.85;
        final double insurance = 500;
        double netSalary;
        if (employee.getGrossSalary() == 0d) {
            netSalary = 0d;
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

//    public void deleteEmployee(int id) throws NotFoundException {
//        Employee employeeToBeDeleted=employeeRepository.getById(id);
//        if (employeeRepository.existsById(id) == false) {
//            throw new NotFoundException("no employee with this ID");
//        }
//        if(employeeToBeDeleted.getManager()==null)
//        {
//            throw new NotFoundException("cannot delete this manager!");
//        }
//        employeeRepository.deleteById(id);
//
//    }

    public boolean existsById(int id) {
        return employeeRepository.existsById(id);
    }


    public Employee updateEmployee(Employee updateEmployee, Employee originalEmployee) throws NotFoundException {

        transferEmployee(updateEmployee, originalEmployee);

        return employeeRepository.save(originalEmployee);
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
        if (updateEmployee.getFirst_name() != null) {
            originalEmployee.setFirst_name(updateEmployee.getFirst_name());
        }
        if (updateEmployee.getEmployees() != null) {
            originalEmployee.setEmployees(updateEmployee.getEmployees());
        }
        if (updateEmployee.getGraduation_date() != null) {
            originalEmployee.setGraduation_date(updateEmployee.getGraduation_date());
        }
        if (updateEmployee.getBirthDate() != null) {
            originalEmployee.setBirthDate(updateEmployee.getBirthDate());
        }
        if (updateEmployee.getManager() != null) {
            originalEmployee.setManager(updateEmployee.getManager());
        }
        if (updateEmployee.getGender() != null) {
            originalEmployee.setGender(updateEmployee.getGender());
        }
        if (updateEmployee.getDepartment() != null) {
            originalEmployee.setDepartment(updateEmployee.getDepartment());
        }
        if (updateEmployee.getTeam() != null) {
            originalEmployee.setTeam(updateEmployee.getTeam());
        }
        if (updateEmployee.getGrossSalary() != 0d) {
            originalEmployee.setGrossSalary(updateEmployee.getGrossSalary());
        }
        if (updateEmployee.getNetSalary() != null) {
            CalcNetSalary(originalEmployee);
        }
    }


}
