package com.example.demo.Services;

import com.example.demo.Classes.Department;
import com.example.demo.Classes.Teams;
import com.example.demo.Repositories.EmployeeRepository;
import com.example.demo.Repositories.TeamRepository;
import com.example.demo.errors.ConflictException;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamService {
    @Autowired
    public TeamRepository teamRepository;

    public Teams addTeam(Teams team) {
        if(teamRepository.existsById(team.getTeamId()))
        {
            throw new ConflictException("team already exists !");
        }

        return   teamRepository.save(team);

    }
}
