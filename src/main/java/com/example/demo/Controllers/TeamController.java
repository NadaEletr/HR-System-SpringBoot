package com.example.demo.Controllers;

import com.example.demo.Classes.Teams;
import com.example.demo.Services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/Teams")
public class TeamController {
    @Autowired
    TeamService teamService;

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<Teams> addNewEmployee(@RequestBody Teams team) {
        Teams newTeam = teamService.addTeam(team);
        return new ResponseEntity<>(newTeam, HttpStatus.CREATED);
    }

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Teams getTeams(@RequestParam("id") String id) {
        return teamService.getTeams(Integer.parseInt(id));
    }

}
