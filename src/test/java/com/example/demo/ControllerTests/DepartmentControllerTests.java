package com.example.demo.ControllerTests;

import com.example.demo.Classes.Department;
import com.example.demo.Classes.Employee;
import com.example.demo.Services.DepartmentService;
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

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class DepartmentControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentService departmentService;

    @Test
    public void addDepartment() throws Exception
    {
        Department department = new Department();
        department.setDepartmentName("gg");
        given(departmentService.saveEmployee(department)).willReturn(department);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(department);
        mockMvc.perform(MockMvcRequestBuilders.post("/HR/addDep").contentType(MediaType.APPLICATION_JSON)
                .content(body)).andExpect(status().isCreated());
    }


}
