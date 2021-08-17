package com.example.demo.IntegerationTests;

import com.example.demo.Classes.Teams;
import com.example.demo.Controllers.TeamController;
import com.example.demo.Services.EmployeeService;
import com.example.demo.Services.TeamService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class TeamTests {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    TeamService teamService;

    @Test
    public void addTeam() throws Exception {
        Teams addTeam = new Teams();
        addTeam.setTeamName("abc");
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(addTeam);

        mockMvc.perform(MockMvcRequestBuilders.post("/HR/addTeam").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated()).andExpect(content().json(body));
    }



}
