package com.example.demo.Repositories;

import com.example.demo.Classes.Department;
import com.example.demo.Classes.Employee;
import com.example.demo.Classes.Teams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {


    @Query("select case when count(d)> 0 then true else false end from Department d where d.departmentId=?1 ")
    boolean existsByDepartmentId(@Param("departmentId")int departmentId);


    @Query("select case when count(t)> 0 then true else false end from Teams t where t.teamId=?1 ")
    boolean existsByTeamId(@Param("teamId")int teamId);


    List<Employee> findAllByTeamTeamId(int teamId);


    List<Employee> findAllByManagerEmployeeId(int employeeId);


    @Query(
            value = "with recursive cte as ( \n" +
                    "            select     employee_id, birthdate, gender, graduation_date, gross_salary, employee_name, net_salary, department_id, manager_id, team_id\n" +
                    "\t\t\n" +
                    "              from       employee\n" +
                    "             where      manager_id =:employeeId\n" +
                    "             union all\n" +
                    "             select     p.employee_id, p.birthdate, p.gender, p.graduation_date, p.gross_salary, p.employee_name, p.net_salary, p.department_id, p.manager_id, p.team_id\n" +
                    "             from       employee p\n" +
                    "            inner join cte\n" +
                    "                   on p.manager_id = cte.employee_id\n" +
                    "            )\n" +
                    "            select * from cte; "
            , nativeQuery = true)
    List<Employee> findAllUnderSomeManager(@Param("employeeId")int employeeId);


}
