package com.example.demo.Repositories;

import com.example.demo.Classes.Department;
import com.example.demo.Classes.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface EmployeeRepository extends JpaRepository<Employee, Integer> {


    @Query(value="select case when count (e)>0 then true else false end from Employee  e where e.department.departmentId =?1 ")
    boolean existsBydepartmentId(@Param("departmentId")int departmentId);

}
