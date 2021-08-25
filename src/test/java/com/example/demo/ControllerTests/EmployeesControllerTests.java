package com.example.demo.ControllerTests;

import com.example.demo.Classes.Employee;
import com.example.demo.Classes.Gender;
import com.example.demo.Services.EmployeeService;
import javassist.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeesControllerTests {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;



    @Test
    public void addEmployee() throws Exception
    {
        Employee employee  = new Employee();
        employee.setName("sara");
        employee.setGender(Gender.Female);
        employee.setEmployeeId(3);
        given(employeeService.saveEmployee(employee)).willReturn(employee);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(employee);
        mockMvc.perform(MockMvcRequestBuilders.post("/HR/add").contentType(MediaType.APPLICATION_JSON)
                .content(body)).andExpect(status().isCreated());
    }

    @Test
    public void getEmployee() throws Exception {
        Employee employee = new Employee();
        employee.setName("sara");
        employee.setGender(Gender.Female);
        employee.setEmployeeId(2);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(employee.getEmployeeId());
        given(employeeService.getEmployeeInfoByID(employee.getEmployeeId())).willReturn(employee);
        mockMvc.perform(MockMvcRequestBuilders.get("/HR/getEmployeeInfo").contentType(MediaType.APPLICATION_JSON).content(body)
                .param("id",String.valueOf(employee.getEmployeeId()))).andExpect(status().isOk())
                .andExpect(jsonPath("employeeId").value(employee.getEmployeeId()));

    }

    @Test
    public void deleteEmployee() throws Exception {
        int id=6;
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(id);
        mockMvc.perform(MockMvcRequestBuilders.delete("/HR/deleteEmp").param("id",String.valueOf(id))
                .contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isOk());
    }






}
