package com.example.demo.IntegerationTests;

import com.example.demo.Classes.Department;
import com.example.demo.Repositories.DepartmentRepository;
import com.example.demo.Repositories.UserAccountRepository;
import com.example.demo.Security.UserAccount;
import com.example.demo.Services.DepartmentService;
import com.example.demo.errors.ConflictException;
import com.example.demo.errors.NotFoundException;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@DatabaseSetup("/data.xml")
@ActiveProfiles("test")
@TestExecutionListeners({

        DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
public class DepartmentTests {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    UserAccountRepository userAccountRepository;

    @Test
    public void whenAddDepartmentReturnDepartment() throws Exception {
        Department department = new Department();
        department.setDepartmentName("networks");
        department.setDepartmentId(3);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(department);
        mockMvc.perform(MockMvcRequestBuilders.post("/department/add").with(httpBasic("nada1", "nada123")).contentType(MediaType.APPLICATION_JSON)
                .content(body)).andExpect(status().isCreated());
        Department resultDepartment = departmentRepository.getById(department.getDepartmentId());
        assertEquals(resultDepartment.getDepartmentName(), department.getDepartmentName());
        assertEquals(resultDepartment.getDepartmentId(), department.getDepartmentId());
    }

    @Test
    public void whenAddDepartmentWithoutName() throws Exception {
        Department department = new Department();
        department.setDepartmentId(3);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(department);
        mockMvc.perform(MockMvcRequestBuilders.post("/department/add").with(httpBasic("nada1", "nada123")).contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isNotFound()).andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals("name must not be null", Objects.requireNonNull(result.getResolvedException()).getMessage()));

    }

    @Test
    public void addDepartmentUnAuthorized() throws Exception {
        Department department = new Department();
        department.setDepartmentName("design");
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(department);
        mockMvc.perform(MockMvcRequestBuilders.post("/department/add").with(httpBasic("nada1", "nada124443")).contentType(MediaType.APPLICATION_JSON)
                .content(body)).andExpect(status().isUnauthorized());
    }

    @Test
    public void addDepartmentForbidden() throws Exception {
        Department department = new Department();
        department.setDepartmentName("design");
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(department);
        mockMvc.perform(MockMvcRequestBuilders.post("/department/add").with(httpBasic("sara3", "mohamed@3")).contentType(MediaType.APPLICATION_JSON)
                .content(body)).andExpect(status().isForbidden());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void testGetDepartment() throws Exception {
        int id = 1;
        UserAccount userAccount = userAccountRepository.getById("nada1");
        mockMvc.perform(MockMvcRequestBuilders.get("/department/get").param("id", String.valueOf(id))
                .with(httpBasic(userAccount.getUserName(), "nada123")));
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void testGetDepartmentUnAuthorized() throws Exception {
        int id = 1;
        UserAccount userAccount = userAccountRepository.getById("nada1");
        mockMvc.perform(MockMvcRequestBuilders.get("/department/get").param("id", String.valueOf(id))
                .with(httpBasic(userAccount.getUserName(), "nada1234444"))).andExpect(status().isUnauthorized());
    }
    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void testGetDepartmentForbidden() throws Exception {
        int id = 1;
        mockMvc.perform(MockMvcRequestBuilders.get("/department/get").param("id", String.valueOf(id))
                .with(httpBasic("sara3", "mohamed@3"))).andExpect(status().isForbidden());
    }

    @Test
    public void testDuplicateDepartmentWhenAdding() throws Exception {
        UserAccount userAccount = userAccountRepository.getById("nada1");
        Department department = new Department();
        department.setDepartmentName("sw");
        department.setDepartmentId(3);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(department);
        mockMvc.perform(MockMvcRequestBuilders.post("/department/add")
                .with(httpBasic(userAccount.getUserName(), "nada123")).contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(result -> assertTrue(result.getResolvedException() instanceof ConflictException))
                .andExpect(status().isConflict()).andExpect(result -> assertEquals("department already exists !", result.getResolvedException().getMessage()));
    }
}
