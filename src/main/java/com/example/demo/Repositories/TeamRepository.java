package com.example.demo.Repositories;

import com.example.demo.Classes.Department;
import com.example.demo.Classes.Employee;
import com.example.demo.Classes.Teams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Teams, Integer> {

   boolean existsByTeamName(String teamName);
}
