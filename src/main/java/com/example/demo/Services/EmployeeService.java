package com.example.demo.Services;

import com.example.demo.Classes.Employee;
import com.example.demo.Classes.SalaryDTO;
import com.example.demo.Repositories.EmployeeRepository;
import com.example.demo.errors.ConflictException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.DelegatingServerHttpResponse;
import org.springframework.stereotype.Service;
import com.example.demo.errors.NotFoundException;

import java.util.List;

@Service
public class EmployeeService {


    @Autowired
    public EmployeeRepository employeeRepository;



    public Employee saveEmployee(Employee employee)  {
        if(employeeRepository.existsById(employee.getEmployeeId()))
        {
            throw new ConflictException("this employee is already added");
        }
        if(employee.getDepartment()!=null&&employeeRepository.existsBydepartmentId(employee.getDepartment().getDepartmentId())==false)
        {
            throw new ConflictException("this department does not exists");
        }
        if(employee.getTeam()!=null &&employeeRepository.existsByTeamId(employee.getTeam().getTeamId())==false)
        {
            throw new ConflictException("this team does not exists");
        }

        return employeeRepository.save(employee);

    }


    public Employee getEmployeeInfoByID(int id ) throws NotFoundException {
        if(employeeRepository.existsById(id)==false)
        {
            throw new NotFoundException("no employee with this ID");
        }
        return employeeRepository.getById(id);
    }


    public void deleteEmployee(int id) throws NotFoundException {
        if(employeeRepository.existsById(id)==false)
        {
            throw new NotFoundException("no employee with this ID");
        }
        employeeRepository.deleteById(id);

    }
    public boolean  existsById(int id)
    {
        return employeeRepository.existsById(id);
    }


    public Employee updateEmployee(Employee updateEmployee, Employee originalEmployee) throws NotFoundException {

        Employee.transferEmployee(updateEmployee, originalEmployee);

        return employeeRepository.save(originalEmployee);
    }

    public SalaryDTO getEmployeeSalary(int id) throws NotFoundException {
        Employee employee = getEmployeeInfoByID(id);
        return new SalaryDTO(employee);
    }

    public List<Employee> getEmployeesInTeam(int teamId)  {
        if(employeeRepository.existsByTeamId(teamId)==false)
        {
            throw new NotFoundException("team does not exists !");
        }
        return employeeRepository.findAllByTeamId( teamId);
    }

    public List<Employee> getEmployeesUnderManger(int mangerId) {
        if(employeeRepository.existsById(mangerId)==false)
        {
            throw new NotFoundException("no employee with this ID");
        }
        return employeeRepository.findAllByManagerId(mangerId);
    }
}
