package com.example.demo.Services;

import com.example.demo.Classes.Employee;
import com.example.demo.Classes.SalaryDTO;
import com.example.demo.Repositories.EmployeeRepository;
import com.example.demo.errors.ConflictException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.errors.NotFoundException;

import java.util.List;

@Service
public class EmployeeService {


    @Autowired
    public EmployeeRepository employeeRepository;
    public Employee saveEmployee(Employee employee) {
        if (employeeRepository.existsById(employee.getEmployeeId())) {
            throw new ConflictException("this employee is already added");
        }
        if (employee.getDepartment() != null && employeeRepository.existsByDepartmentId(employee.getDepartment().getDepartmentId()) == false) {
            throw new NotFoundException("this department does not exists");
        }
        if (employee.getTeam() != null && employeeRepository.existsByTeamId(employee.getTeam().getTeamId()) == false) {
            throw new NotFoundException("this team does not exists");
        }
        if (employee.getManager() != null && !employeeRepository.existsById(employee.getManager().getEmployeeId())) {
            throw new NotFoundException(" manager does not exists!");
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
        return employeeRepository.findAllByManagerEmployeeId(mangerId);
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
        if(updateEmployee.getName() !=null)
        {
            originalEmployee.setName(updateEmployee.getName());
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
}
