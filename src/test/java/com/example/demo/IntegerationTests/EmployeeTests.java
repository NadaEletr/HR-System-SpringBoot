package com.example.demo.IntegerationTests;

import com.example.demo.Classes.*;
import com.example.demo.Security.UserAccount;
import com.example.demo.Security.UserDetailPrincipalService;
import com.example.demo.Repositories.*;
import com.example.demo.Services.EmployeeService;
import com.example.demo.errors.ConflictException;
import com.example.demo.errors.NotFoundException;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "scheduling.enabled=false")
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration
@DatabaseSetup("/data.xml")
@TestExecutionListeners({

        DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
public class EmployeeTests {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    private WebApplicationContext context;

    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    AbsenceRepository absenceRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    SalaryHistoryRepository salaryHistoryRepository;
    @Autowired
    UserDetailPrincipalService userDetailPrincipalService;
    @Autowired
    UserAccountRepository userAccountRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void getEmployeeSalary() throws Exception {
        int id = 1;
        Employee employee = employeeRepository.getById(id);
        SalaryDTO salaryDTO = new SalaryDTO(employee);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(salaryDTO);
        mockMvc.perform(MockMvcRequestBuilders.get("/HR/employee/get/Salaries").with(httpBasic("nada1", "nada123"))
                .param("id", String.valueOf(employee.getId())))
                .andExpect(status().isOk()).andExpect((content().json(body)));
    }

    @Test
    public void getEmployee() throws Exception {
        int id = 1;
        Employee employee = employeeRepository.getById(id);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(employee);
        mockMvc.perform(MockMvcRequestBuilders.get("/HR/employee/get").with(httpBasic("nada1", "nada123")).
                param("id", String.valueOf(id))).andExpect(status().isOk())
                .andExpect((content().json(body)));

    }

    @Test
    public void getEmployeesInTeam() throws Exception {
        int teamId = 1;
        List<Employee> employees = employeeService.getEmployeesInTeam(teamId);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(employees);
        mockMvc.perform(MockMvcRequestBuilders.get("/HR/employee/get/team").with(httpBasic("nada1", "nada123"))
                .param("id", String.valueOf(teamId))).andExpect(content().json(body)).andExpect(jsonPath("$", hasSize(employees.size())))
                .andExpect(status().isOk());
    }

    @Test
    public void addEmployee() throws Exception {
        Optional<Teams> team = teamRepository.findById(1);
        Optional<Department> department = departmentRepository.findById(1);
        Employee manager = employeeRepository.getById(1);
        Employee employee = new Employee();
        employee.setFirst_name("youssef");
        employee.setLast_name("hhh");
        employee.setId(4);
        employee.setGender(Gender.Male);
        employee.setGrossSalary(1223330d);
        employee.setNationalId("122");
        employee.setTeam(team.get());
        employee.setDepartment(department.get());
        employee.setManager(manager);
        employee.setYearsOfExperience(10);
        employee.setAcceptableLeaves(30);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(employee);
        employeeService.CalcNetSalary(employee);
        employeeService.generateAcceptedLeave(employee);
        String response = objectMapper.writeValueAsString(employee);
        mockMvc.perform(MockMvcRequestBuilders.post("/HR/employee/add")
                .with(httpBasic("nada1", "nada123")).
                        contentType(MediaType.APPLICATION_JSON)
                .content(body)).andDo(print()).andExpect(status().isCreated())
                .andExpect(content().json(response));
    }

    @Test
    public void duplicateNationalIdWhenAddingEmployee() throws Exception {
        Employee employee = new Employee();
        employee.setId(5);
        employee.setFirst_name("nada");
        employee.setLast_name("ibrahim");
        employee.setGender(Gender.Male);
        employee.setNationalId("123");
        employee.setGrossSalary(1223330d);
        employee.setYearsOfExperience(10);
        employee.setAcceptableLeaves(30);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(employee);
        employeeService.CalcNetSalary(employee);
        employeeService.generateAcceptedLeave(employee);
        mockMvc.perform(MockMvcRequestBuilders.post("/HR/employee/add").with(httpBasic("nada1", "nada123")).contentType(MediaType.APPLICATION_JSON)
                .content(body)).andExpect(result -> assertTrue(result.getResolvedException() instanceof ConflictException))
                .andExpect(status().isConflict()).andExpect(result -> assertEquals("national id exists before !", result.getResolvedException().getMessage()));
    }

    @Test
    public void duplicateIdWhenAddingEmployee() throws Exception {
        Employee employee = new Employee();
        employee.setId(2);
        employee.setFirst_name("nada");
        employee.setLast_name("ibrahim");
        employee.setGender(Gender.Male);
        employee.setNationalId("666");
        employee.setGrossSalary(1223330d);
        employee.setYearsOfExperience(10);
        employee.setAcceptableLeaves(30);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(employee);
        employeeService.CalcNetSalary(employee);
        employeeService.generateAcceptedLeave(employee);
        mockMvc.perform(MockMvcRequestBuilders.post("/HR/employee/add").with(httpBasic("nada1", "nada123")).contentType(MediaType.APPLICATION_JSON)
                .content(body)).andExpect(result -> assertTrue(result.getResolvedException() instanceof ConflictException))
                .andExpect(status().isConflict()).andExpect(result -> assertEquals("this employee is already added", result.getResolvedException().getMessage()));

    }

    @Test
    public void updateEmployee() throws Exception { // change it
        int employeeId = 1;
        Employee employee = employeeRepository.getById(employeeId);
        Employee updateEmployee = new Employee();
        updateEmployee.setId(employeeId);
        updateEmployee.setGender(Gender.Female);
        updateEmployee.setFirst_name("sara");
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(updateEmployee);
        mockMvc.perform(MockMvcRequestBuilders.put("/HR/employee/update")
                .with(httpBasic("nada1", "nada123"))
                .contentType(MediaType.APPLICATION_JSON).content(body)
                .param("id", String.valueOf(employeeId)))
                .andExpect(status().isOk());
        assertEquals(employee.getFirst_name(), updateEmployee.getFirst_name());
        assertEquals(employee.getGender(), updateEmployee.getGender());
    }


    @Test
    public void getEmployeeUnderManager() throws Exception {
        Employee employeeManager = employeeRepository.getById(1);
        List<Employee> employeesUnderManger = employeeRepository.findAllByManagerId(employeeManager.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(employeesUnderManger);
        mockMvc.perform(MockMvcRequestBuilders.get("/HR/employee/get/underManager").param("id", String.valueOf(employeeManager.getId()))
                .with(httpBasic("nada1", "nada123"))
        ).andExpect(status().isOk()).andExpect(content().json(body));
    }

    @Test
    public void getEmployeeUnderSomeManager() throws Exception {
        int managerId = 1;
        List<Employee> employeesUnderManger = employeeRepository.findAllUnderSomeManager(managerId);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(employeesUnderManger);
        mockMvc.perform(MockMvcRequestBuilders.get("/HR/employee/get/SomeManager").param("id", String.valueOf(managerId)).with(httpBasic("nada1", "nada123"))
                .with(httpBasic("nada1", "nada123"))
        ).andExpect(status().isOk()).andExpect(content().json(body));
    }

    @Test
    public void deleteEmployeeWithNoManager() throws Exception { ///dont forget
        int id = 1;
        String message = "employee " + id + " is deleted";
        Employee employeeToDelete = employeeRepository.getById(id);
        mockMvc.perform(MockMvcRequestBuilders.delete("/HR/employee/delete")
                .param("id", String.valueOf(employeeToDelete.getId()))
                .with(httpBasic("nada1", "nada123"))
        ).andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals(" can't delete employee with no manager", result.getResolvedException().getMessage()));
    }

    @Test
//    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expected.xml")
    public void deleteEmployeeWithManager() throws Exception { //check
        int id = 3;
        String message = "employee " + id + " is deleted";
        Employee employeeToDelete = employeeRepository.getById(id);
        mockMvc.perform(MockMvcRequestBuilders.delete("/HR/employee/delete")
                .param("id", String.valueOf(employeeToDelete.getId()))
                .with(httpBasic("nada1", "nada123")))
                .andExpect(status().isOk())
                .andExpect(content().string(message));
        assertEquals(employeeRepository.existsById(3),false);

    }



    @Test
    public void changePassword() throws Exception { //check again
        ObjectMapper objectMapper = new ObjectMapper();
        String message = "password is changed successfully";
        String newPassword = "sara123";
        String username = "sara3";
        String body = objectMapper.writeValueAsString(newPassword);
        mockMvc.perform(MockMvcRequestBuilders.put("/user/changePassword")
                .with(httpBasic(username, "mohamed@3")).contentType(MediaType.APPLICATION_JSON).content(body)
        ).andExpect(status().isOk()).andExpect(content().string(message));
    }


}
