package com.example.demo.IntegerationTests;

import com.example.demo.Classes.Employee;
import com.example.demo.Classes.SalaryDTO;
import com.example.demo.Classes.Teams;
import com.example.demo.Repositories.EmployeeRepository;
import com.example.demo.Services.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.org.apache.commons.lang.ObjectUtils;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class EmployeeTests {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    EmployeeService employeeService;

    @Autowired
    EmployeeRepository employeeRepository;

    @Test
    public void getEmployeeSalary() throws Exception {
        Employee employee = employeeService.getEmployeeInfoByID(23);
        SalaryDTO salaryDTO = new SalaryDTO(employee);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(salaryDTO);
        mockMvc.perform(MockMvcRequestBuilders.get("/HR/getSalaries")
                .param("id", String.valueOf(employee.getEmployeeId())))
                .andExpect(status().isOk()).andExpect((content().json(body)));
    }
    @Test
    public void getEmployeesInTeam() throws Exception {

        int teamId=1;
        List<Employee> employees = employeeService.getEmployeesInTeam(teamId);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(employees);
        mockMvc.perform(MockMvcRequestBuilders.get("/HR/getEmpInTeam")
                .param("id", String.valueOf(teamId))).andExpect(content().json(body)).andExpect(jsonPath("$",hasSize(employees.size())))
                .andExpect(status().isOk());

    }
    @Test
    public void addEmployee() throws Exception
    {
        Employee employee  = new Employee();
        employee.setName("sara");
        employee.setGender('F');
        employee.setGrossSalary(120000d);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(employee);
        mockMvc.perform(MockMvcRequestBuilders.post("/HR/add").contentType(MediaType.APPLICATION_JSON)
                .content(body)).andExpect(status().isCreated()).andExpect(content().json(body));
    }

    @Test
    public void deleteEmployee() throws Exception {
        int id=28;
        String message ="employee is deleted";
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(message);
        mockMvc.perform(MockMvcRequestBuilders.delete("/HR/deleteEmp").param("id",String.valueOf(id))
        ).andExpect(status().isOk()).andExpect(content().string(message));

    }

    @Test
    public void getEmployeeUnderManager() throws Exception {

        Employee employeeManager= employeeRepository.getById(13);
        List<Employee> employeesUnderManger= employeeRepository.findAllByManagerId(employeeManager.getEmployeeId());
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(employeesUnderManger);
        mockMvc.perform(MockMvcRequestBuilders.get("/HR/getEmployees/underManager").param("id",String.valueOf(employeeManager.getEmployeeId()))
        ).andExpect(status().isOk()).andExpect(content().json(body));


    }




}
