package com.example.demo.Services;

import com.example.demo.Classes.Department;
import com.example.demo.Repositories.DepartmentRepository;
import com.example.demo.errors.ConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {

    @Autowired
    public DepartmentRepository departmentRepository;

    public Department saveEmployee(Department department) {
        if (departmentRepository.existsById(department.getDepartmentId()) || departmentRepository.existsByDepartmentName(department.getDepartmentName())) {
            throw new ConflictException("department already exists !");
        }

        return departmentRepository.save(department);


    }
}

