package com.example.demo.Services;

import com.example.demo.Classes.Teams;
import com.example.demo.Repositories.TeamRepository;
import com.example.demo.errors.ConflictException;
import com.example.demo.errors.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamService {
    @Autowired
    public TeamRepository teamRepository;

    public Teams addTeam(Teams team) {
        if (team.getTeamName() == null) {
            throw new NotFoundException("team name must not be null");
        }
        if (teamRepository.existsByTeamName(team.getTeamName())) {
            throw new ConflictException("team already exists !");
        }
        if (teamRepository.existsById(team.getTeamId())) {
            throw new ConflictException("team already exists !");
        }
        return teamRepository.save(team);

    }


    public Teams getTeams(int id) {

        if (!teamRepository.existsById(id)) {
            throw new NotFoundException("team does not exists!");
        }
        return teamRepository.getById(id);
    }
}
