package com.example.demo.Services;

import com.example.demo.Classes.AllowedVacations;
import com.example.demo.Classes.Employee;
import com.example.demo.Classes.SalaryDTO;
import com.example.demo.Classes.Vacations;
import com.example.demo.Repositories.EmployeeRepository;
import com.example.demo.Repositories.VacationRepository;
import com.example.demo.errors.ConflictException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.errors.NotFoundException;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {


    @Autowired
    public EmployeeRepository employeeRepository;
    @Autowired
    public VacationRepository vacationRepository;
    public Employee saveEmployee(Employee employee) {
        if (employeeRepository.existsById(employee.getNationalId())) {
            throw new ConflictException("this employee is already added");
        }
        if (employee.getDepartment() != null && employeeRepository.existsByDepartmentId(employee.getDepartment().getDepartmentId()) == false) {
            throw new NotFoundException("this department does not exists");
        }
        if (employee.getTeam() != null && employeeRepository.existsByTeamId(employee.getTeam().getTeamId()) == false) {
            throw new NotFoundException("this team does not exists");
        }
        if (employee.getManager() != null && !employeeRepository.existsById(employee.getManager().getNationalId())) {
            throw new NotFoundException(" manager does not exists!");
        }
        if(employee.getYearsOfExperience()<AllowedVacations.HIGHWORKINGYEARS)
        {
            employee.setAcceptableLeaves(AllowedVacations.getLessEXPERIENCED());
        }
        else
        {
            employee.setAcceptableLeaves(AllowedVacations.getEXPERIENCED());
        }
        getNetSalary(employee);
        return employeeRepository.save(employee);
    }
    public void getNetSalary(Employee employee) {
        final double taxRatio = 0.85;
        final double insurance = 500;
        double netSalary;
        if (employee.getGrossSalary() == 0d) {
           netSalary=0d;
        }
        else
        {
            netSalary = employee.getGrossSalary() * taxRatio - insurance;
        }
        employee.setNetSalary(netSalary);
    }

    public Employee getEmployeeInfoByID(int id) throws NotFoundException {
        if (employeeRepository.existsById(id) == false) {
            throw new NotFoundException("no employee with this ID");
        }
        return employeeRepository.getById(id);
    }

    public void deleteEmployee(int id) throws NotFoundException {
        Employee employeeToBeDeleted=employeeRepository.getById(id);
        if (employeeRepository.existsById(id) == false) {
            throw new NotFoundException("no employee with this ID");
        }
        if(employeeToBeDeleted.getManager()==null)
        {
            throw new ConflictException("cannot delete this manager!");
        }
        employeeRepository.deleteById(id);

    }

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
        return employeeRepository.findAllByManagerNationalId(mangerId);
    }

    public List<Employee> getEmployeesOnSpeceficManger(int mangerId) {
        if (employeeRepository.existsById(mangerId) == false) {
            throw new NotFoundException("no employee with this ID");
        }
        return employeeRepository.findAllUnderSomeManager(mangerId);
    }

    public void deleteManager(int mangerID) {

        Employee manager = getEmployeeInfoByID(mangerID);
        if (manager.getManager() == null) {
            throw new NotFoundException(" can't delete employee with no manager");
        }

        for (Employee employee : manager.getEmployees()) {
            employee.setManager(manager.getManager());
            employeeRepository.save(employee);
        }
        deleteEmployee(mangerID);
    }
    public void transferEmployee(Employee updateEmployee, Employee originalEmployee)
    {
        if(updateEmployee.getFirst_name() !=null)
        {
            originalEmployee.setFirst_name(updateEmployee.getFirst_name());
        }
        if(updateEmployee.getEmployees()!=null)
        {
            originalEmployee.setEmployees(updateEmployee.getEmployees());
        }
        if(updateEmployee.getGraduation_date()!=null)
        {
            originalEmployee.setGraduation_date(updateEmployee.getGraduation_date());
        }
        if(updateEmployee.getBirthDate()!= null)
        {
            originalEmployee.setBirthDate(updateEmployee.getBirthDate());
        }
        if(updateEmployee.getManager()!=null)
        {
            originalEmployee.setManager(updateEmployee.getManager());
        }
        if(updateEmployee.getGender()!=null)
        {
            originalEmployee.setGender(updateEmployee.getGender());
        }
        if(updateEmployee.getDepartment() !=null)
        {
            originalEmployee.setDepartment(updateEmployee.getDepartment());
        }
        if(updateEmployee.getTeam() !=null)
        {
            originalEmployee.setTeam(updateEmployee.getTeam());
        }
        if(updateEmployee.getGrossSalary()!=0d)
        {
            originalEmployee.setGrossSalary(updateEmployee.getGrossSalary());
        }
        if(updateEmployee.getNetSalary()!=0d)
        {
            getNetSalary(originalEmployee);
        }
    }

    public void checkExceededDays(Vacations vacations ,Employee employee)
    {
        int countLeaves=employeeRepository.countEmployeeExceededDays(vacations.getEmployee().getNationalId())+1;
       if(employee.getAcceptableLeaves()==AllowedVacations.LessEXPERIENCED&&countLeaves>AllowedVacations.LessEXPERIENCED)
       {
           vacations.setExceeded(1);
       }
       else if (employee.getAcceptableLeaves()==AllowedVacations.EXPERIENCED &&countLeaves>AllowedVacations.EXPERIENCED)
       {
           vacations.setExceeded(1);
       }
       else
       {
           vacations.setExceeded(0);
       }

    }


    public void recordLeave(int id) {
        Employee employee= getEmployeeInfoByID(id);
        Date date = Date.valueOf(LocalDate.now());
        employee.setLeaves(employee.getLeaves()+1);
        Vacations vacations =new Vacations(date);
        vacations.setEmployee(employee);
       //        int currentYear= date.getYear()+1900;
        checkExceededDays(vacations,employee);
        employee.getVacations().add(vacations);
        vacationRepository.save(vacations);
        employeeRepository.save(employee);



    }
}
