package com.example.demo.Repositories;

import com.example.demo.Classes.Teams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Teams, Integer> {

    boolean existsByTeamName(String teamName);


    List<Teams> getAllByTeamName(String name);
}
