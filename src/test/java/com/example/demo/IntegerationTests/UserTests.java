package com.example.demo.IntegerationTests;


import com.example.demo.errors.InvalidCredentialsException;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
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
public class UserTests {

    @Autowired
    MockMvc mockMvc;
    @Test
    public void invalidLenghtpassword() throws Exception {
        String password = "n00";
        mockMvc.perform(MockMvcRequestBuilders.put("/user/changePassword").with(httpBasic("sara3", "mohamed@3")).contentType(MediaType.APPLICATION_JSON)
                .content(password)).andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidCredentialsException))
                .andExpect(status().isUnauthorized()).andExpect(result -> assertEquals("week password!, password must be at least 4 characters", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    public void changePassword() throws Exception {
        String password = "nadaIbr023";
        ObjectMapper objectMapper = new ObjectMapper();
        String message = "password is changed successfully";
        String body = objectMapper.writeValueAsString(password);
        mockMvc.perform(MockMvcRequestBuilders.put("/user/changePassword")
                .with(httpBasic("sara3", "mohamed@3"))
                .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk()).andExpect(content().string(message));
    }

    @Test
    public void changePasswordUnAuthorized() throws Exception {
        String password = "nadaIbr023";
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(password);
        mockMvc.perform(MockMvcRequestBuilders.put("/user/changePassword")
                .with(httpBasic("salma", "mohamed@3"))
                .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isUnauthorized());
    }

}
