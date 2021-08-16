package com.example.demo.Services;

import com.example.demo.Classes.Employee;
import com.example.demo.Repositories.EmployeeRepository;
import com.example.demo.errors.ConflictException;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.DelegatingServerHttpResponse;
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
        /*if(employeeRepository.existsBydepartmentId(employee.getDepartment().getDepartmentId())==false)
        {
            throw new ConflictException("this department does not exists");
        }*/
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


    public Employee updateEmployee(Employee employee, String id) throws NotFoundException {

       if(existsById(Integer.parseInt(id)))
       {
           employee.setEmployeeId(Integer.parseInt(id));
           return employeeRepository.save(employee);
       }
       else
       {
           throw new NotFoundException("no employee with this ID");
       }

    }
}
