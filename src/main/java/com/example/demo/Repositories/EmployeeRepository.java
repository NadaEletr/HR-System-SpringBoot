package com.example.demo.Repositories;

import com.example.demo.Classes.Department;
import com.example.demo.Classes.Employee;
import com.example.demo.Classes.Teams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface EmployeeRepository extends JpaRepository<Employee, Integer> {


    @Query(value="select case when count (d)>0 then true else false end from Department  d where d.departmentId =?1 ")
    boolean existsBydepartmentId(@Param("departmentId")int departmentId);


    @Query(value="select case when count (t)>0 then true else false end from Teams  t where t.teamId =?1 ")
    boolean existsByTeamId(int teamId);

    @Query(value = "SELECT e FROM Employee  e where e.team.teamId =?1")
    List<Employee> findAllByTeamId(@Param("teamId")int teamId);
}
