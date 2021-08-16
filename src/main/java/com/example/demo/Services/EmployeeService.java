package com.example.demo.Services;

import com.example.demo.Classes.Employee;
import com.example.demo.Repositories.EmployeeRepository;
import com.example.demo.errors.ConflictException;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    @Autowired
    public EmployeeRepository employeeRepository;

    public Employee saveEmployee(Employee employee) throws NotFoundException {
        if(employeeRepository.existsById(employee.getEmployeeId()))
        {
            throw new ConflictException("this employee is already added");
        }
        if(employeeRepository.existsBydepartmentId(employee.getDepartment().getDepartmentId())==false)
        {
            throw new ConflictException("this department does not exists");
        }
        return employeeRepository.save(employee);

    }


}
