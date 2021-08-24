package com.example.demo.IntegerationTests;

import com.example.demo.Classes.Teams;
import com.example.demo.Repositories.TeamRepository;
import com.example.demo.Services.TeamService;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@TestExecutionListeners({

        DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
public class TeamTests {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    TeamService teamService;
    @Autowired
    TeamRepository teamRepository;

    @Test
    @DatabaseSetup("/data.xml")
    public void addTeam() throws Exception {
        Teams addTeam = new Teams();
        addTeam.setTeamName("a8");
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(addTeam);
        mockMvc.perform(MockMvcRequestBuilders.post("/HR/Teams/add").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated());
        Teams resultTeams = teamRepository.getById(addTeam.getTeamId());
        assertEquals(resultTeams.getTeamId(),addTeam.getTeamId());
        assertEquals(resultTeams.getTeamName(),addTeam.getTeamName());



    }



}
