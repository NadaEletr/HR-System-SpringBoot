package com.example.demo.IntegerationTests;

import com.example.demo.Classes.Employee;
import com.example.demo.Classes.Earnings;
import com.example.demo.Classes.SalaryDTO;
import com.example.demo.Repositories.EmployeeRepository;
import com.example.demo.Repositories.EarningsRepository;
import com.example.demo.Repositories.SalaryHistoryRepository;
import com.example.demo.Repositories.UserAccountRepository;
import com.example.demo.Security.UserAccount;
import com.example.demo.Security.UserDetailPrincipalService;
import com.example.demo.Services.EmployeeService;
import com.example.demo.Services.SalaryService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DatabaseSetup("/data.xml")
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
public class SalariesTests {
    @Autowired
    EmployeeService employeeService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    SalaryHistoryRepository salaryHistoryRepository;
    @Autowired
    EarningsRepository earningsRepository;
    @Autowired
    SalaryService salaryService;
    @Autowired
    UserAccountRepository userAccountRepository;
    @Autowired
    UserDetailPrincipalService userDetailPrincipalService;

    @Test
    public void getEmployeeActualSalary() throws Exception {
        int id = 1;
        Employee employee = employeeRepository.getById(id);
        SalaryDTO salaryDTO = new SalaryDTO(employee);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(salaryDTO);
        mockMvc.perform(MockMvcRequestBuilders.get("/Salary/get/ActualSalaries").with(httpBasic("nada1", "nada123"))
                .param("id", String.valueOf(employee.getId())))
                .andExpect(status().isOk()).andExpect((content().json(body)));
    }

    @Test
    public void getEmployeeActualSalaryUnAuthorized() throws Exception {
        int id = 1;
        mockMvc.perform(MockMvcRequestBuilders.get("/Salary/get/ActualSalaries").with(httpBasic("nada111", "mohamed@3")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getEmployeeActualSalaryForbidden() throws Exception {
        int id = 1;
        mockMvc.perform(MockMvcRequestBuilders.get("/Salary/get/ActualSalaries").with(httpBasic("sara3", "mohamed@3"))
                .param("id", String.valueOf(id)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void userGetActualSalary() throws Exception {
        int id = 1;
        Employee employee = employeeRepository.getById(id);
        SalaryDTO salaryDTO = new SalaryDTO(employee);
        ObjectMapper objectMapper = new ObjectMapper();
        String response = objectMapper.writeValueAsString(salaryDTO);
        mockMvc.perform(MockMvcRequestBuilders.get("/Salary/get/UserActualSalaries").with(httpBasic("sara3", "mohamed@3")))
                .andExpect(status().isOk()).andExpect((content().json(response)));
    }

    @Test
    public void userGetActualSalaryUnAuthorized() throws Exception {
        int id = 1;
        Employee employee = employeeRepository.getById(id);
        SalaryDTO salaryDTO = new SalaryDTO(employee);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.get("/Salary/get/UserActualSalaries").with(httpBasic("mariam", "mohamed@3")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getEmployeeActualSalaryWithNonExistingId() throws Exception {
        int id = 17;
        mockMvc.perform(MockMvcRequestBuilders.get("/Salary/get/ActualSalaries").with(httpBasic("nada1", "nada123"))
                .param("id", String.valueOf(id)))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(status().isNotFound()).andExpect(result -> assertEquals("no employee with this ID", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    public void addBonusAndRaise() throws Exception {
        int employeeId = 1;
        Employee employee = employeeService.getEmployeeInfoByID(employeeId);
        Earnings earnings = new Earnings();
        earnings.setBonus(500);
        earnings.setEmployee(employee);
        earnings.setRaise(1000);
        earnings.setId(2);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(earnings);
        mockMvc.perform(MockMvcRequestBuilders.post("/Salary/add/earnings").with(httpBasic("nada1", "nada123")).contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk());
        Earnings earnings1 = earningsRepository.getById(2);
        assertEquals(earnings1.getBonus(), earnings.getBonus());
        assertEquals(earnings1.getRaise(), earnings.getRaise());
        assertEquals(earnings1.getEmployee().getNationalId(), earnings.getEmployee().getNationalId());
        assertEquals(earnings1.getId(), earnings.getId());
    }

    @Test
    public void addBonusAndRaiseUnAuthorized() throws Exception {
        int employeeId = 1;
        Employee employee = employeeService.getEmployeeInfoByID(employeeId);
        Earnings earnings = new Earnings();
        earnings.setBonus(500);
        earnings.setEmployee(employee);
        earnings.setRaise(1000);
        earnings.setId(2);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(earnings);
        mockMvc.perform(MockMvcRequestBuilders.post("/Salary/add/earnings").with(httpBasic("ibrahim", "nada123")).contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void addBonusAndRaiseForbidden() throws Exception {
        int employeeId = 1;
        Employee employee = employeeService.getEmployeeInfoByID(employeeId);
        Earnings earnings = new Earnings();
        earnings.setBonus(500);
        earnings.setEmployee(employee);
        earnings.setRaise(1000);
        earnings.setId(2);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(earnings);
        mockMvc.perform(MockMvcRequestBuilders.post("/Salary/add/earnings").with(httpBasic("sara3", "mohamed@3")).contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isForbidden());
    }

    @Test
    public void addBonusAndRaiseWithNonExistingEmployee() throws Exception {
        int employeeId = 50;
        Employee employee = new Employee();
        employee.setId(employeeId);
        Earnings earnings = new Earnings();
        earnings.setBonus(500);
        earnings.setEmployee(employee);
        earnings.setEmployee(employee);
        earnings.setRaise(1000);
        earnings.setId(2);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(earnings);
        mockMvc.perform(MockMvcRequestBuilders.post("/Salary/add/earnings").with(httpBasic("nada1", "nada123")).contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(status().isNotFound()).andExpect(result -> assertEquals("no employee with this id !", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    public void testWhenBonusIsNegative() throws Exception {
        int employeeId = 2;
        Employee employee = employeeService.getEmployeeInfoByID(employeeId);
        Earnings earnings = new Earnings();
        earnings.setBonus(-500);
        earnings.setEmployee(employee);
        earnings.setId(3);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(earnings);
        mockMvc.perform(MockMvcRequestBuilders.post("/Salary/add/earnings").with(httpBasic("nada1", "nada123")).contentType(MediaType.APPLICATION_JSON)
                .content(body)).andExpect(result -> assertTrue(result.getResolvedException() instanceof ConflictException))
                .andExpect(status().isConflict()).andExpect(result -> assertEquals("bonus must be positive number", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    public void testWhenRaiseIsNegative() throws Exception {
        int employeeId = 3;
        Employee employee = employeeService.getEmployeeInfoByID(employeeId);
        Earnings earnings = new Earnings();
        earnings.setRaise(-100);
        earnings.setEmployee(employee);
        earnings.setId(4);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(earnings);
        mockMvc.perform(MockMvcRequestBuilders.post("/Salary/add/earnings").with(httpBasic("nada1", "nada123")).contentType(MediaType.APPLICATION_JSON)
                .content(body)).andExpect(result -> assertTrue(result.getResolvedException() instanceof ConflictException))
                .andExpect(status().isConflict()).andExpect(result -> assertEquals("raise must be positive number", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void testEmployeeGetSalaryHistory() throws Exception {
        UserAccount userAccount = userAccountRepository.getById("sara3");
        mockMvc.perform(MockMvcRequestBuilders.get("/Salary/get/SalaryHistory")
                .with(httpBasic(userAccount.getUserName(), "mohamed@3")));
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void testEmployeeGetSalaryHistoryUnAuthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/Salary/get/SalaryHistory")
                .with(httpBasic("youssef", "mohamed@3"))).andExpect(status().isUnauthorized());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void testHRGetSalaryHistory() throws Exception {
        UserAccount userAccount = userAccountRepository.getById("nada1");
        mockMvc.perform(MockMvcRequestBuilders.get("/Salary/get/SalaryHistory/1")
                .with(httpBasic(userAccount.getUserName(), "nada123")));
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void testHRGetSalaryHistoryUnAuthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/Salary/get/SalaryHistory/1")
                .with(httpBasic("salwa", "nada123"))).andExpect(status().isUnauthorized());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED, value = "/data.xml")
    public void testHRGetSalaryHistoryForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/Salary/get/SalaryHistory/1")
                .with(httpBasic("sara3", "mohamed@3"))).andExpect(status().isForbidden());
    }

}
