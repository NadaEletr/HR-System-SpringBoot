package com.example.demo.IntegerationTests;

import com.example.demo.Classes.Absence;
import com.example.demo.Repositories.AbsenceRepository;
import com.example.demo.Repositories.UserAccountRepository;
import com.example.demo.Security.UserAccount;
import com.example.demo.errors.ConflictException;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.org.apache.commons.lang.time.DateUtils;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DatabaseSetup("/data.xml")
@TestExecutionListeners({

        DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
public class AbsenceTests {
    @Autowired
    UserAccountRepository userAccountRepository;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    AbsenceRepository absenceRepository;

    @Test
    public void recordLeaves() throws Exception {
        UserAccount userAccount = userAccountRepository.getById("mariam2");
        ObjectMapper objectMapper = new ObjectMapper();
        String message = "your Absence are " + (userAccount.getEmployee().getLeaves() + 1);
        String body = objectMapper.writeValueAsString(message);
        mockMvc.perform(MockMvcRequestBuilders.post("/absence/record")
                .with(httpBasic(userAccount.getUserName(), "ahmed@2"))
        ).andExpect(status().isOk()).andExpect(content().string(message));
        Absence absence = absenceRepository.findByEmployee(userAccount.getEmployee());
        assertEquals(absence.getEmployee().getLeaves(), userAccount.getEmployee().getLeaves() + 1);
    }


//    @Test
//    public void recordLeaveDuplicate() throws Exception { //check date
//        UserAccount userAccount = userAccountRepository.getById("sara3");
//        mockMvc.perform(MockMvcRequestBuilders.post("/absence/record")
//                .with(httpBasic(userAccount.getUserName(), "mohamed@3"))
//        ).andExpect(result -> assertTrue(result.getResolvedException() instanceof ConflictException))
//                .andExpect(status().isConflict())
//                .andExpect(result -> assertEquals("you are already recorded this day !", result.getResolvedException().getMessage()));
//    }

    @Test
    public void getLeaves() throws Exception {
        UserAccount userAccount = userAccountRepository.getById("sara3");
        ObjectMapper objectMapper = new ObjectMapper();
        List<Absence> absences = absenceRepository.findAllByEmployee_Id(userAccount.getEmployee().getId());
        String expectedJson = objectMapper.writeValueAsString(absences);
        mockMvc.perform(MockMvcRequestBuilders.get("/absence/get")
                .with(httpBasic(userAccount.getUserName(), "mohamed@3"))
        ).andExpect(content().json(expectedJson));

    }

}
