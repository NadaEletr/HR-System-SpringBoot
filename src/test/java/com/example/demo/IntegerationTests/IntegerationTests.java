package com.example.demo.IntegerationTests;

import com.example.demo.Classes.Employee;
import com.example.demo.Classes.SalaryDTO;
import com.example.demo.Services.DepartmentService;
import com.example.demo.Services.EmployeeService;
import javassist.NotFoundException;
import com.example.demo.Classes.Employee;
import com.example.demo.Services.EmployeeService;
import javassist.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class IntegerationTests {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    EmployeeService employeeService;

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


}
