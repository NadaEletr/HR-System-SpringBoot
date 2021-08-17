package com.example.demo.Repositories;

import com.example.demo.Classes.Department;
import com.example.demo.Classes.Employee;
import com.example.demo.Classes.Teams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface TeamRepository extends JpaRepository<Teams, Integer> {


    @Query(value="select case when count (t)>0 then true else false end from Teams  t where t.teamName=?1 ")
    boolean findByTeamName(@Param("teamName")String teamName);
}
