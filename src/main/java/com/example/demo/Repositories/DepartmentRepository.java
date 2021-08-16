package com.example.demo.Repositories;

import com.example.demo.Classes.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}
