package com.example.demo.Repositories;

import com.example.demo.Classes.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {


    @Query("select case when count(d)> 0 then true else false end from Department d where d.departmentId=?1 ")
    boolean existsByDepartmentId(@Param("departmentId")int departmentId);


    @Query("select case when count(t)> 0 then true else false end from Teams t where t.teamId=?1 ")
    boolean existsByTeamId(@Param("teamId")int teamId);


    List<Employee> findAllByTeamTeamId(int teamId);


    List<Employee> findAllByManagerNationalId(int employeeId);



    @Query(
            value = "with recursive cte(national_id,first_name,last_name,employee_degree,leaves,years_of_experience,birthdate, gender,acceptable_leaves, graduation_date, gross_salary,net_salary, department_id, manager_id, team_id ) as ( \n" +
                    "            select     national_id,first_name,last_name,leaves,employee_degree,years_of_experience,birthdate, gender,acceptable_leaves, graduation_date, gross_salary,net_salary, department_id, manager_id, team_id \n" +
                    "\t\t\n" +
                    "              from       employee e\n" +
                    "             where      manager_id =:employeeId\n" +
                    "             union all\n" +
                    "             select    p.national_id,p.first_name,p.last_name,p.leaves,p.employee_degree,p.years_of_experience,p.birthdate, p.acceptable_leaves,p.gender, p.graduation_date, p.gross_salary,p.net_salary,p.department_id, p.manager_id, p.team_id \n" +
                    "             from       employee p\n" +
                    "            inner join cte\n" +
                    "                   on p.manager_id = cte.national_id\n" +
                    "            )\n" +
                    "            select * from cte; "
            , nativeQuery = true)
    List<Employee> findAllUnderSomeManager(@Param("employeeId")int employeeId);

    @Query("SELECT e.leaves FROM Employee e WHERE e.nationalId=?1")
    int getLeaves(int employee_id);

    @Modifying
    @Query(value = "update Employee e set e.leaves=0 ")
    void  updateYearlyLeaves();

    @Query("select e.nationalId from Employee e")
    Stream<Integer> getAllByNationalId();

//    Stream<Employee> getAllByNationalId();


//(value = "update Employee e set e.leaves=0  ")

}






//
//    WITH RECURSIVE cte(national_id, name , date_of_birth, gender,
//                       graduation_date, gross_salary, department_name, team_name, manager_national_id, degree, years_of_experience
//        ,leaves, first_name, last_name, base_parent) AS
//             (
//                     SELECT national_id, name,date_of_birth,gender,graduation_date,gross_salary,
//                     department_name,team_name,manager_national_id ,degree,years_of_experience,
//                     leaves,first_name,last_name , national_id as base_parent
//                     FROM employee
//                     UNION ALL
//                     SELECT c.national_id, c.name,c.date_of_birth,c.gender,
//                     c.graduation_date,c.gross_salary,c.department_name,
//                     c.team_name,c.manager_national_id ,c.degree ,
//                     c.years_of_experience,c.leaves,c.first_name,c.last_name ,cte.base_parent
//                     FROM employee c JOIN cte ON
//                     cte.national_id=c.manager_national_id
//                     Where base_parent ='?1'
//                     )
//                     SELECT * FROM cte

//@Query(value="with RECURSIVE cte (employee_id, birthdate, gender,graduation_date, gross_salary, employee_name,net_salary, department_id, manager_id, team_id\n" +
//        "  ) AS\n" +
//        "             (\n" +
//        "               SELECT employee_id, birthdate, gender, graduation_date, gross_salary, employee_name,net_salary, department_id, manager_id, team_id\n" +
//        "               FROM employee\n" +
//        "               UNION ALL\n" +
//        "               SELECT p.employee_id, p.birthdate, p.gender, p.graduation_date, \n" +
//        "p.gross_salary, p.employee_name,\n" +
//        " p.net_salary, p.department_id, p.manager_id, p.team_id\n" +
//        "               FROM employee p Inner JOIN cte ON\n" +
//        "                  p.manager_id = cte.employee_id\n" +
//        "               \n" +
//        "             )\n" +
//        "             SELECT * FROM cte where manager_id=?1\n)", nativeQuery = true)