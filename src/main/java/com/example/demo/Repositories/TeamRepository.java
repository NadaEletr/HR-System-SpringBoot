package com.example.demo.Repositories;

import com.example.demo.Classes.Teams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Teams, Integer> {

    boolean existsByTeamName(String teamName);



}
