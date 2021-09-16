package com.example.demo.IntegerationTests;

import com.example.demo.Classes.Teams;
import com.example.demo.Repositories.TeamRepository;
import com.example.demo.Repositories.UserAccountRepository;
import com.example.demo.Security.UserAccount;
import com.example.demo.Services.TeamService;
import com.example.demo.errors.ConflictException;
import com.example.demo.errors.NotFoundException;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Before;
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

import javax.inject.Inject;
import javax.sql.DataSource;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "scheduling.enabled=false")
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@DatabaseSetup("/data.xml")
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
    @Autowired
    UserAccountRepository userAccountRepository;


    @Test
    public void addTeam() throws Exception {
        Teams addTeam = new Teams();
        addTeam.setTeamName("a8");
        addTeam.setTeamId(3);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(addTeam);
        mockMvc.perform(MockMvcRequestBuilders.post("/Teams/add").with(httpBasic("nada1", "nada123")).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated()).andReturn();
        Teams resultTeams = teamRepository.getById(addTeam.getTeamId());
        assertEquals(resultTeams.getTeamId(), addTeam.getTeamId());
        assertEquals(resultTeams.getTeamName(), addTeam.getTeamName());
    }

    @Test
    public void addTeamWithoutName() throws Exception {
        Teams addTeam = new Teams();
        addTeam.setTeamId(3);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(addTeam);
        mockMvc.perform(MockMvcRequestBuilders.post("/Teams/add").with(httpBasic("nada1", "nada123")).contentType(MediaType.APPLICATION_JSON).content(body))
              .andExpect(status().isNotFound()).andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals("team name must not be null", result.getResolvedException().getMessage()));;

    }

    @Test
    public void addTeamUnAuthorized() throws Exception {
        Teams addTeam = new Teams();
        addTeam.setTeamName("a8");
        addTeam.setTeamId(3);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(addTeam);
        mockMvc.perform(MockMvcRequestBuilders.post("/Teams/add").with(httpBasic("farah", "nada123")).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isUnauthorized());
    }
    @Test
    public void addTeamForbidden() throws Exception {
        Teams addTeam = new Teams();
        addTeam.setTeamName("a8");
        addTeam.setTeamId(3);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(addTeam);
        mockMvc.perform(MockMvcRequestBuilders.post("/Teams/add").with(httpBasic("sara3", "mohamed@3")).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isForbidden());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void testGetTeam() throws Exception {
        int id =1;
        UserAccount userAccount = userAccountRepository.getById("nada1");
        mockMvc.perform(MockMvcRequestBuilders.get("/Teams/get").param("id", String.valueOf(id))
                .with(httpBasic(userAccount.getUserName(), "nada123")));
    }
    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void testGetTeamUnAuthorized() throws Exception {
        int id =1;
        mockMvc.perform(MockMvcRequestBuilders.get("/Teams/get").param("id", String.valueOf(id))
                .with(httpBasic("salma", "nada123"))).andExpect(status().isUnauthorized());
    }
    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void testGetTeamForbidden() throws Exception {
        int id =1;
        mockMvc.perform(MockMvcRequestBuilders.get("/Teams/get").param("id", String.valueOf(id))
                .with(httpBasic("sara3", "mohamed@3"))).andExpect(status().isForbidden());
    }

    @Test
    public void testDuplicateTeamName() throws Exception {
        UserAccount userAccount = userAccountRepository.getById("nada1");
        Teams addTeam = new Teams();
        addTeam.setTeamName("a2");
        addTeam.setTeamId(3);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(addTeam);
        mockMvc.perform(MockMvcRequestBuilders.post("/Teams/add")
                .with(httpBasic(userAccount.getUserName(), "nada123")).contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(result -> assertTrue(result.getResolvedException() instanceof ConflictException))
                .andExpect(status().isConflict()).andExpect(result -> assertEquals("team already exists !", result.getResolvedException().getMessage()));
    }
}
