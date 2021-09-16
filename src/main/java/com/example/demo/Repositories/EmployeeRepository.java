package com.example.demo.Repositories;

import com.example.demo.Classes.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {



    @Query("select case when count(t)> 0 then true else false end from Teams t where t.teamId=?1 ")
    boolean existsByTeamId(@Param("teamId") int teamId);


    List<Employee> findAllByTeamTeamId(int teamId);

    @Query("select case when count(e)> 0 then true else false end from Employee e where e.nationalId=?1 ")
    Boolean existsByNationalId(String nationalId);


    List<Employee> findAllByManagerId(int employeeId);

    @Transactional
    @Query(
            value = "with recursive cte(id,national_id,first_name,last_name,employee_degree,leaves,years_of_experience,birthdate, gender,acceptable_leaves, graduation_date, gross_salary,net_salary, department_id, manager_id, team_id ) as ( \n" +
                    "            select     id,national_id,first_name,last_name,employee_degree,leaves,years_of_experience,birthdate, gender,acceptable_leaves, graduation_date, gross_salary,net_salary, department_id, manager_id, team_id  \n" +
                    "\t\t\n" +
                    "              from       employee e\n" +
                    "             where      manager_id =:employeeId\n" +
                    "             union all\n" +
                    "             select    p.id,p.national_id,p.first_name,p.last_name,p.employee_degree,p.leaves,p.years_of_experience,p.birthdate, p.gender,p.acceptable_leaves, p.graduation_date, p.gross_salary,p.net_salary, p.department_id, p.manager_id, p.team_id \n" +
                    "             from       employee p\n" +
                    "            inner join cte\n" +
                    "                   on p.manager_id = cte.id\n" +
                    "            )\n" +
                    "            select * from cte; "
            , nativeQuery = true)
    List<Employee> findAllUnderSomeManager(@Param("employeeId") int employeeId);

    @Transactional
    @Modifying
    @Query(value = "update Employee e set e.leaves= :leaves where e.id= :nationalId")
    void updateMonthlyLeaves(int leaves, int nationalId);

    @Query("select e.id from Employee e")
    List<Integer> getAllByNationalId();

    @Modifying
    @Query(value = "update Employee e set e.leaves=?1")
    void updateYearlyLeaves(int i);

    @Transactional
    @Modifying
    void deleteById(int id);

}



