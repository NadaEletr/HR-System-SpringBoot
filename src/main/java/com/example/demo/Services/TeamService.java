package com.example.demo.Services;

import com.example.demo.Classes.Department;
import com.example.demo.Classes.Employee;
import com.example.demo.Classes.Teams;
import com.example.demo.Repositories.EmployeeRepository;
import com.example.demo.Repositories.TeamRepository;
import com.example.demo.errors.ConflictException;
import com.example.demo.errors.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {
    @Autowired
    public TeamRepository teamRepository;

    public Teams addTeam(Teams team) {
        if (teamRepository.existsByTeamName(team.getTeamName())) {
            throw new ConflictException("team already exists !");
        }

        return teamRepository.save(team);

    }


    public List<Teams> getTeams(String name) {

        if (!teamRepository.existsByTeamName(name)) {
            throw new NotFoundException("team does not exists!");
        }
        return teamRepository.getAllByTeamName(name);
    }
}
