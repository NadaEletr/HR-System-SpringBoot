package com.example.HR.Services;

import com.example.HR.Classes.Department;
import com.example.HR.Repositories.DepartmentRepository;
import com.example.HR.errors.ConflictException;
import com.example.HR.errors.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {

    @Autowired
    public DepartmentRepository departmentRepository;

    public Department saveEmployee(Department department) {
        if (department.getDepartmentName() == null) {
            throw new NotFoundException("name must not be null");
        }
        if (departmentRepository.existsById(department.getDepartmentId()) || departmentRepository.existsByDepartmentName(department.getDepartmentName())) {
            throw new ConflictException("department already exists !");
        }

        return departmentRepository.save(department);
    }

    public Department getDepartment(int id) {
        if (!departmentRepository.existsById(id)) {
            throw new NotFoundException("department does not exists!");
        }
        return departmentRepository.getById(id);
    }
}

