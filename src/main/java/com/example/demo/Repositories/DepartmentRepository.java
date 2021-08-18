package com.example.demo.Repositories;

import com.example.demo.Classes.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    @Query(value="select case when count (d)>0 then true else false end from Department d where d.departmentName=?1 ")
    boolean existsByName(@Param("departmentName")String departmentName);
}
