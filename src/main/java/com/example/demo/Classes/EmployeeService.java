package com.example.demo.Classes;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    public EmployeeRepository employeeRepository;

    public Employee saveEmployee(Employee employee)
    {
        return employeeRepository.save(employee);
    }

    public Optional<Employee> getByID (int id) throws NotFoundException {
        try{

            return employeeRepository.findById(id);
        }catch (Exception e)
        {
            throw new NotFoundException("No record with this ID!");
        }
    }

}
