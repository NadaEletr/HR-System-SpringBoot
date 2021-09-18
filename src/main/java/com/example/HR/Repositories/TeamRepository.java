package com.example.HR.Repositories;

import com.example.HR.Classes.Teams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Teams, Integer> {

    boolean existsByTeamName(String teamName);



}
