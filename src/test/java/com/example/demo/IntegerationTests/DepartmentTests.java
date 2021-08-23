package com.example.demo.IntegerationTests;

import com.example.demo.Classes.Department;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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
public class DepartmentTests {
    @Autowired
    MockMvc mockMvc;
    @Test
    public  void whenAddDepartmentReturnDepartment() throws Exception {
        Department department  = new Department();
        department.setDepartmentName("design");
//        department.setDepartmentId(5);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(department);
        mockMvc.perform(MockMvcRequestBuilders.post("/HR/department/add").contentType(MediaType.APPLICATION_JSON)
                .content(body)).andExpect(status().isCreated()).andExpect(content().json(body));
    }
}
