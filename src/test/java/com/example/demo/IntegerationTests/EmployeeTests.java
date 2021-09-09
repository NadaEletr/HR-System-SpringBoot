package com.example.demo.IntegerationTests;

import com.example.demo.Classes.*;
import com.example.demo.Security.UserAccount;
import com.example.demo.Security.UserDetailPrincipalService;
import com.example.demo.Security.UserPrincipal;
import com.example.demo.Repositories.*;
import com.example.demo.Services.EmployeeService;
import com.example.demo.errors.ConflictException;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,properties = "scheduling.enabled=false")
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
    VacationRepository vacationRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    SalaryHistoryRepository salaryHistoryRepository;
    @Autowired
    UserDetailPrincipalService userDetailPrincipalService;
    @Autowired
    UserAccountRepository userAccountRepository;
    @Test
    public void getEmployeeSalary() throws Exception {
        int id=1;
        Employee employee = employeeRepository.getById(id);
        SalaryDTO salaryDTO = new SalaryDTO(employee);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(salaryDTO);
        mockMvc.perform(MockMvcRequestBuilders.get("/HR/employee/get/Salaries").with(httpBasic("nada1","nada123"))
                .param("id", String.valueOf(employee.getNationalId()))).andExpect(authenticated())
                .andExpect(status().isOk()).andExpect((content().json(body)));
    }

    @Test
    public void getEmployee() throws Exception {
        int id = 1;
        Employee employee = employeeRepository.getById(id);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(employee);
        mockMvc.perform(MockMvcRequestBuilders.get("/HR/employee/get").with(httpBasic("nada1","nada123")).
                param("id", String.valueOf(id))).andExpect(status().isOk()).andExpect(authenticated())
                .andExpect((content().json(body)));
        assertEquals(employee.getNationalId(),id);
    }
    @Test
    public void getEmployeesInTeam() throws Exception {
        int teamId = 1;
        List<Employee> employees = employeeService.getEmployeesInTeam(teamId);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(employees);
        mockMvc.perform(MockMvcRequestBuilders.get("/HR/employee/get/team").with(httpBasic("nada1","nada123"))
                .param("id", String.valueOf(teamId))).andExpect(content().json(body)).andExpect(jsonPath("$", hasSize(employees.size()))).andExpect(authenticated())
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
        employee.setGender(Gender.Male);
        employee.setGrossSalary(1223330d);
        employee.setNationalId(4);
       employee.setTeam(team.get());
//      employee.setDepartment(department.get());
        //employee.setManager(manager);
        employee.setYearsOfExperience(10);
        employee.setAcceptableLeaves(30);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(employee);
        employee.setNetSalary((employee.getGrossSalary() * 0.85 - 500));

        mockMvc.perform(MockMvcRequestBuilders.post("/HR/employee/add")
                .with(httpBasic("nada1","nada123")).
                        contentType(MediaType.APPLICATION_JSON)
                .content(body)).andDo(print()).andExpect(status().isCreated()).andExpect(authenticated());
        Employee resultEmployee= employeeRepository.getById(employee.getNationalId());
        assertEquals(resultEmployee.getNationalId(),employee.getNationalId());
        assertEquals(resultEmployee.getFirst_name(),resultEmployee.getFirst_name());
    }

    @Test
    public void updateEmployee() throws Exception {
        int employeeId = 1;
        Employee employee=employeeRepository.getById(employeeId);
        Employee updateEmployee = new Employee();
        updateEmployee.setNationalId(employeeId);
        updateEmployee.setGender(Gender.Female);
        updateEmployee.setFirst_name("sara");
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(updateEmployee);
        mockMvc.perform(MockMvcRequestBuilders.put("/HR/employee/update")
                .with(httpBasic("nada1","nada123"))
                .contentType(MediaType.APPLICATION_JSON).content(body)
                .param("id", String.valueOf(employeeId)))
                .andExpect(authenticated())
                .andExpect(status().isOk());
        assertEquals(employee.getNationalId(),employeeId);
        assertEquals(employee.getFirst_name(),updateEmployee.getFirst_name());
        assertEquals(employee.getGender(),updateEmployee.getGender());
    }

//    @Test
//    public void deleteEmployee() throws Exception {
//        int id = 2;
//        String message = "employee is deleted";
//        Employee employeeToDelete=employeeRepository.getById(id);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String body = objectMapper.writeValueAsString(message);
//        mockMvc.perform(MockMvcRequestBuilders.delete("/HR/employee/delete").param("id", String.valueOf(id))
//        ).andExpect(status().isOk()).andExpect(content().string(message));
//        assertEquals(employeeService.existsById(employeeToDelete.getEmployeeId()),false);
//    }

    @Test
    public void getEmployeeUnderManager() throws Exception {
        Employee employeeManager = employeeRepository.getById(1);
        List<Employee> employeesUnderManger = employeeRepository.findAllByManagerNationalId(employeeManager.getNationalId());
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(employeesUnderManger);
        mockMvc.perform(MockMvcRequestBuilders.get("/HR/employee/get/underManager").param("id", String.valueOf(employeeManager.getNationalId()))
                .with(httpBasic("nada1","nada123"))
        ).andExpect(status().isOk()).andExpect(content().json(body));
    }

    @Test
    public void getEmployeeUnderSomeManager() throws Exception  {
        int managerId = 1;
        List<Employee> employeesUnderManger = employeeRepository.findAllUnderSomeManager(managerId);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(employeesUnderManger);
        mockMvc.perform(MockMvcRequestBuilders.get("/HR/employee/get/SomeManager").param("id", String.valueOf(managerId)).with(httpBasic("nada1","nada123"))
                .with(httpBasic("nada1","nada123"))
        ).andExpect(status().isOk()).andExpect(content().json(body));
    }

    @Test
//    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/expected.xml")
    public void deleteEmployee() throws Exception { ///dont forget
        int id = 2;
        String message = "employee " + id + " is deleted";
        Employee employeeToDelete=employeeRepository.getById(id);
        mockMvc.perform(MockMvcRequestBuilders.delete("/HR/employee/delete")
                .param("id", String.valueOf(employeeToDelete.getNationalId()))
                .with(httpBasic("nada1","nada123"))
        ).andExpect(status().isOk())
               .andExpect(content().string(message)).andExpect(authenticated());
//        assertEquals(employeeRepository.existsById(id),false);
    }


    @Test
    public void recordLeaves() throws Exception {
        UserAccount userAccount = userAccountRepository.getById("sara3");
        ObjectMapper objectMapper = new ObjectMapper();
        String message = "your absence are " + (userAccount.getEmployee().getLeaves() + 1);
        String body = objectMapper.writeValueAsString(message);
        mockMvc.perform(MockMvcRequestBuilders.post("/user/record/leave")
                .with(httpBasic("sara3", "mohamed@3"))
        ).andExpect(status().isOk()).andExpect(content().string(message));
        Vacations vacation = vacationRepository.findByEmployee(userAccount.getEmployee());
        assertEquals(vacation.getEmployee().getLeaves(), userAccount.getEmployee().getLeaves() + 1);
    }
    @Test
    public void recordLeaveDuplicate() throws Exception {
        UserAccount userAccount = userAccountRepository.getById("mariam2");
        ObjectMapper objectMapper = new ObjectMapper();
        String message = "your absence are " + (userAccount.getEmployee().getLeaves() + 1);
        String body = objectMapper.writeValueAsString(message);
        mockMvc.perform(MockMvcRequestBuilders.post("/user/record/leave")
                .with(httpBasic("mariam2", "ahmed@2"))
        ).andExpect(result -> assertTrue(result.getResolvedException() instanceof ConflictException))
                .andExpect(status().isConflict())
                .andExpect(result -> assertEquals("you are already recorded this day !",result.getResolvedException().getMessage()));

    }


}
