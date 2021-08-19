package com.example.demo.Controllers;

import com.example.demo.Classes.Employee;
import com.example.demo.Classes.Teams;
import com.example.demo.Services.EmployeeService;
import com.example.demo.Services.TeamService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/HR")
public class TeamController {
    @Autowired
    TeamService teamService;
    @PostMapping(value = "/addTeam", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<Teams> addNewEmployee(@RequestBody Teams team) throws NotFoundException {
        Teams newTeam = teamService.addTeam(team);
        return new ResponseEntity<>(newTeam, HttpStatus.CREATED);
    }

}
