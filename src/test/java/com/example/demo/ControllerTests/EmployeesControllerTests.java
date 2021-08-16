package com.example.demo.ControllerTests;

import com.example.demo.Classes.Employee;
import com.example.demo.Services.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
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
        employee.setGender('F');
        employee.setGraduation_date("2008");
        given(employeeService.saveEmployee(employee)).willReturn(employee);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(employee);
        mockMvc.perform(MockMvcRequestBuilders.post("/HR/add").contentType(MediaType.APPLICATION_JSON)
                .content(body)).andExpect(status().isCreated());
    }

    @Test
    public void deleteEmployee()
    {

    }



}
