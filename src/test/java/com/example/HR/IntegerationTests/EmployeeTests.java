package com.example.HR.IntegerationTests;

import com.example.HR.Classes.Department;
import com.example.HR.Classes.Employee;
import com.example.HR.Classes.Gender;
import com.example.HR.Classes.Teams;
import com.example.HR.Repositories.*;
import com.example.HR.Security.UserDetailPrincipalService;
import com.example.HR.Services.EmployeeService;
import com.example.HR.errors.ConflictException;
import com.example.HR.errors.NotFoundException;
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

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
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
    public void getEmployeeUnAuthorized() throws Exception {
        int id = 1;
        Employee employee = employeeRepository.getById(id);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.get("/HR/employee/get").with(httpBasic("nada1222", "nada123")).
                param("id", String.valueOf(id))).andExpect(status().isUnauthorized());

    }

    @Test
    public void getEmployeeForbidden() throws Exception {
        int id = 1;
        Employee employee = employeeRepository.getById(id);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.get("/HR/employee/get").with(httpBasic("sara3", "mohamed@3")).
                param("id", String.valueOf(id))).andExpect(status().isForbidden());

    }

    @Test
    public void getEmployeeWithNonExistingId() throws Exception {
        int id = 20;
        mockMvc.perform(MockMvcRequestBuilders.get("/HR/employee/get").with(httpBasic("nada1", "nada123")).param("id", String.valueOf(id))
        ).andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(status().isNotFound()).andExpect(result -> assertEquals("no employee with this ID", Objects.requireNonNull(result.getResolvedException()).getMessage()));

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
    public void addTeamToEmployee() throws Exception {
        int teamId = 1;
        int employeeId=1;
        String message="Team is added !";
        mockMvc.perform(MockMvcRequestBuilders.post("/HR/employee/addTeam").with(httpBasic("nada1", "nada123"))
                .param("teamId", String.valueOf(teamId)).param("employeeId", String.valueOf(employeeId)))
                .andExpect(status().isOk()).andExpect(content().string(message));
        Employee employee = employeeService.getEmployeeInfoByID(employeeId);
        assertEquals(employee.getTeam().getTeamId(),teamId);
    }
    @Test
    public void addTeamToNonExistedEmployee() throws Exception {
        int teamId = 1;
        int employeeId=20;
        String message="Team is added !";
        mockMvc.perform(MockMvcRequestBuilders.post("/HR/employee/addTeam").with(httpBasic("nada1", "nada123"))
                .param("teamId", String.valueOf(teamId)).param("employeeId", String.valueOf(employeeId)))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(status().isNotFound()).andExpect(result -> assertEquals("no employee with this ID", Objects.requireNonNull(result.getResolvedException()).getMessage()));

    }
    @Test
    public void addNonExistedTeamToEmployee() throws Exception {
        int teamId = 10;
        int employeeId=1;
        mockMvc.perform(MockMvcRequestBuilders.post("/HR/employee/addTeam").with(httpBasic("nada1", "nada123"))
                .param("teamId", String.valueOf(teamId)).param("employeeId", String.valueOf(employeeId)))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(status().isNotFound()).andExpect(result -> assertEquals("team does not exists!", Objects.requireNonNull(result.getResolvedException()).getMessage()));

    }
    @Test
    public void addTeamToEmployeeUnAuthorized() throws Exception {
        int teamId = 1;
        int employeeId=1;
        String message="Team is added !";
        mockMvc.perform(MockMvcRequestBuilders.post("/HR/employee/addTeam").with(httpBasic("jjj", "nada123"))
                .param("teamId", String.valueOf(teamId)).param("employeeId", String.valueOf(employeeId)))
                .andExpect(status().isUnauthorized());
    }  @Test
    public void addTeamToEmployeeForbidden() throws Exception {
        int teamId = 1;
        int employeeId=1;
        String message="Team is added !";
        mockMvc.perform(MockMvcRequestBuilders.post("/HR/employee/addTeam").with(httpBasic("sara3", "mohamed@3"))
                .param("teamId", String.valueOf(teamId)).param("employeeId", String.valueOf(employeeId)))
                .andExpect(status().isForbidden());
    }
    @Test
    public void addDepartmentToEmployees() throws Exception {
        int departmentId = 1;
        int employeeId=1;
        String message="Department is added !";
        mockMvc.perform(MockMvcRequestBuilders.post("/HR/employee/addDepartment").with(httpBasic("nada1", "nada123"))
                .param("departmentId", String.valueOf(departmentId)).param("employeeId", String.valueOf(employeeId)))
                .andExpect(status().isOk()).andExpect(content().string(message));
        Employee employee = employeeService.getEmployeeInfoByID(employeeId);
        assertEquals(employee.getDepartment().getDepartmentId(),departmentId);
    }
    @Test
    public void addDepartmentToNonExistedEmployees() throws Exception {
        int departmentId = 1;
        int employeeId=20;
        String message="Department is added !";
        mockMvc.perform(MockMvcRequestBuilders.post("/HR/employee/addDepartment").with(httpBasic("nada1", "nada123"))
                .param("departmentId", String.valueOf(departmentId)).param("employeeId", String.valueOf(employeeId)))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(status().isNotFound()).andExpect(result -> assertEquals("no employee with this ID", Objects.requireNonNull(result.getResolvedException()).getMessage()));

    }
    @Test
    public void addNonExistedDepartmentToEmployees() throws Exception {
        int departmentId = 15;
        int employeeId=1;
        String message="Department is added !";
        mockMvc.perform(MockMvcRequestBuilders.post("/HR/employee/addDepartment").with(httpBasic("nada1", "nada123"))
                .param("departmentId", String.valueOf(departmentId)).param("employeeId", String.valueOf(employeeId)))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(status().isNotFound()).andExpect(result -> assertEquals("department does not exists!", Objects.requireNonNull(result.getResolvedException()).getMessage()));

    }
    @Test
    public void addDepartmentToEmployeesUnAuthorized() throws Exception {
        int departmentId = 1;
        int employeeId=1;
        mockMvc.perform(MockMvcRequestBuilders.post("/HR/employee/addDepartment").with(httpBasic("salwa", "nada123"))
                .param("departmentId", String.valueOf(departmentId)).param("employeeId", String.valueOf(employeeId)))
                .andExpect(status().isUnauthorized());
    }
    @Test
    public void addDepartmentToEmployeesForbidden() throws Exception {
        int departmentId = 1;
        int employeeId=1;
        mockMvc.perform(MockMvcRequestBuilders.post("/HR/employee/addDepartment").with(httpBasic("sara3", "mohamed@3"))
                .param("departmentId", String.valueOf(departmentId)).param("employeeId", String.valueOf(employeeId)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getEmployeesInTeamUnAuthorized() throws Exception {
        int teamId = 1;
        mockMvc.perform(MockMvcRequestBuilders.get("/HR/employee/get/team").with(httpBasic("122dfe", "nada123"))
                .param("id", String.valueOf(teamId)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getEmployeesInTeamForbidden() throws Exception {
        int teamId = 1;
        mockMvc.perform(MockMvcRequestBuilders.get("/HR/employee/get/team").with(httpBasic("sara3", "mohamed@3"))
                .param("id", String.valueOf(teamId)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getEmployeesInNonExistingTeam() throws Exception {
        int teamId = 10;
        mockMvc.perform(MockMvcRequestBuilders.get("/HR/employee/get/team").with(httpBasic("nada1", "nada123"))
                .param("id", String.valueOf(teamId))).andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(status().isNotFound()).andExpect(result -> assertEquals("team does not exists !", Objects.requireNonNull(result.getResolvedException()).getMessage()));
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
    public void addEmployeeWithWrongName() throws Exception {
        Optional<Teams> team = teamRepository.findById(1);
        Optional<Department> department = departmentRepository.findById(1);
        Employee manager = employeeRepository.getById(1);
        Employee employee = new Employee();
        employee.setFirst_name("youssef@-");
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
        mockMvc.perform(MockMvcRequestBuilders.post("/HR/employee/add")
                .with(httpBasic("nada1", "nada123")).
                        contentType(MediaType.APPLICATION_JSON)
                .content(body)).andExpect(status().isNotFound()).andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals(" name must be characters only", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    public void addEmployeeWithNonExistingNationalId() throws Exception {

        Employee employee = new Employee();
        employee.setFirst_name("youssef");
        employee.setLast_name("hhh");
        employee.setId(4);
        employee.setGender(Gender.Male);
        employee.setGrossSalary(1223330d);
        employee.setYearsOfExperience(10);
        employee.setAcceptableLeaves(30);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(employee);
        mockMvc.perform(MockMvcRequestBuilders.post("/HR/employee/add")
                .with(httpBasic("nada1", "nada123")).
                        contentType(MediaType.APPLICATION_JSON)
                .content(body)).andDo(print()).andExpect(status().isNotFound()).andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals("national id must not be null", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    public void addEmployeeWithNonExistingFirstName() throws Exception {

        Employee employee = new Employee();
        employee.setLast_name("hhh");
        employee.setGender(Gender.Male);
        employee.setGrossSalary(1223330d);
        employee.setNationalId("12442");
        employee.setYearsOfExperience(10);
        employee.setAcceptableLeaves(30);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(employee);
        mockMvc.perform(MockMvcRequestBuilders.post("/HR/employee/add")
                .with(httpBasic("nada1", "nada123")).
                        contentType(MediaType.APPLICATION_JSON)
                .content(body)).andDo(print()).andExpect(status().isNotFound()).andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals("name missing !", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    public void addEmployeeWithNonExistingLastName() throws Exception {
        Employee employee = new Employee();
        employee.setFirst_name("hhh");
        employee.setGender(Gender.Male);
        employee.setGrossSalary(1223330d);
        employee.setNationalId("12442");
        employee.setYearsOfExperience(10);
        employee.setAcceptableLeaves(30);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(employee);
        mockMvc.perform(MockMvcRequestBuilders.post("/HR/employee/add")
                .with(httpBasic("nada1", "nada123")).
                        contentType(MediaType.APPLICATION_JSON)
                .content(body)).andDo(print()).andExpect(status().isNotFound()).andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals("name missing !", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    public void addEmployeeWithNonExistingYearsOfExperience() throws Exception {
        Employee employee = new Employee();
        employee.setFirst_name("hhh");
        employee.setGender(Gender.Male);
        employee.setLast_name("uuuu");
        employee.setGrossSalary(1223330d);
        employee.setNationalId("12442");
        employee.setAcceptableLeaves(30);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(employee);
        mockMvc.perform(MockMvcRequestBuilders.post("/HR/employee/add")
                .with(httpBasic("nada1", "nada123")).
                        contentType(MediaType.APPLICATION_JSON)
                .content(body)).andDo(print()).andExpect(status().isNotFound()).andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals("years of experience must not be empty!", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    public void addEmployeeWithNonExistingGrossSalary() throws Exception {
        Employee employee = new Employee();
        employee.setFirst_name("ahmed");
        employee.setGender(Gender.Male);
        employee.setLast_name("mohamed");
        employee.setYearsOfExperience(12);
        employee.setNationalId("12442");
        employee.setAcceptableLeaves(30);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(employee);
        mockMvc.perform(MockMvcRequestBuilders.post("/HR/employee/add")
                .with(httpBasic("nada1", "nada123")).
                        contentType(MediaType.APPLICATION_JSON)
                .content(body)).andDo(print()).andExpect(status().isNotFound()).andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals(" Actual gross salary is missing", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }


    @Test
    public void addEmployeeUnAuthorized() throws Exception {
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
        mockMvc.perform(MockMvcRequestBuilders.post("/HR/employee/add")
                .with(httpBasic("ssssffr", "nada123")).
                        contentType(MediaType.APPLICATION_JSON)
                .content(body)).andDo(print()).andExpect(status().isUnauthorized());
    }

    @Test
    public void addEmployeeForbidden() throws Exception {
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
        mockMvc.perform(MockMvcRequestBuilders.post("/HR/employee/add")
                .with(httpBasic("sara3", "mohamed@3")).
                        contentType(MediaType.APPLICATION_JSON)
                .content(body)).andDo(print()).andExpect(status().isForbidden());
    }


    @Test
    public void duplicateNationalIdWhenAddingEmployee() throws Exception {
        Employee employee = new Employee();
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
                .andExpect(status().isConflict()).andExpect(result -> assertEquals("national id exists before !", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    public void addingEmployeeWithNonExistingManager() throws Exception {
        Employee employee = new Employee();
        employee.setFirst_name("nada");
        employee.setLast_name("ibrahim");
        employee.setGender(Gender.Male);
        employee.setNationalId("12553");
        employee.setGrossSalary(1223330d);
        employee.setYearsOfExperience(10);
        employee.setAcceptableLeaves(30);
        Employee manager = new Employee();
        manager.setId(50);
        employee.setManager(manager);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(employee);
        mockMvc.perform(MockMvcRequestBuilders.post("/HR/employee/add").with(httpBasic("nada1", "nada123")).contentType(MediaType.APPLICATION_JSON)
                .content(body)).andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(status().isNotFound()).andExpect(result -> assertEquals(" manager does not exists!", Objects.requireNonNull(result.getResolvedException()).getMessage()));

    }

    @Test
    public void addingEmployeeWithNegativeGrossSalary() throws Exception {
        Employee employee = new Employee();
        employee.setFirst_name("nada");
        employee.setLast_name("ibrahim");
        employee.setGender(Gender.Male);
        employee.setNationalId("1251563");
        employee.setGrossSalary(-1223330d);
        employee.setYearsOfExperience(10);
        employee.setAcceptableLeaves(30);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(employee);
        mockMvc.perform(MockMvcRequestBuilders.post("/HR/employee/add").with(httpBasic("nada1", "nada123")).contentType(MediaType.APPLICATION_JSON)
                .content(body)).andExpect(result -> assertTrue(result.getResolvedException() instanceof ConflictException))
                .andExpect(status().isConflict()).andExpect(result -> assertEquals("salary must positive", Objects.requireNonNull(result.getResolvedException()).getMessage()));

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
                .andExpect(status().isConflict()).andExpect(result -> assertEquals("this employee is already added", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    public void updateEmployee() throws Exception { // change it
        int employeeId = 1;
        Employee employee = employeeRepository.getById(employeeId);
        Employee updateEmployee = new Employee();
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
    public void updateEmployeeUnAuthorized() throws Exception { // change it
        int employeeId = 1;
        Employee updateEmployee = new Employee();
        updateEmployee.setGender(Gender.Female);
        updateEmployee.setFirst_name("nada");
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(updateEmployee);
        mockMvc.perform(MockMvcRequestBuilders.put("/HR/employee/update")
                .with(httpBasic("saraaw77w", "nada123"))
                .contentType(MediaType.APPLICATION_JSON).content(body)
                .param("id", String.valueOf(employeeId)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void updateEmployeeForbidden() throws Exception { // change it
        int employeeId = 1;
        Employee updateEmployee = new Employee();
        updateEmployee.setGender(Gender.Female);
        updateEmployee.setFirst_name("nada");
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(updateEmployee);
        mockMvc.perform(MockMvcRequestBuilders.put("/HR/employee/update")
                .with(httpBasic("sara3", "mohamed@3"))
                .contentType(MediaType.APPLICATION_JSON).content(body)
                .param("id", String.valueOf(employeeId)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void updateEmployeeWithNonExistingEmployee() throws Exception {
        int employeeId = 30;
        Employee updateEmployee = new Employee();
        updateEmployee.setGender(Gender.Female);
        updateEmployee.setFirst_name("mariam");
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(updateEmployee);
        mockMvc.perform(MockMvcRequestBuilders.put("/HR/employee/update").with(httpBasic("nada1", "nada123"))
                .param("id", String.valueOf(employeeId))
                .contentType(MediaType.APPLICATION_JSON).content(body)
        ).andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(status().isNotFound()).andExpect(result -> assertEquals("no employee with this ID", Objects.requireNonNull(result.getResolvedException()).getMessage()));

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
    public void getEmployeeUnderManagerUnAuthorized() throws Exception {
        Employee employeeManager = employeeRepository.getById(1);
        List<Employee> employeesUnderManger = employeeRepository.findAllByManagerId(employeeManager.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.get("/HR/employee/get/underManager").param("id", String.valueOf(employeeManager.getId()))
                .with(httpBasic("samia", "nada123"))
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void getEmployeeUnderManagerForbidden() throws Exception {
        Employee employeeManager = employeeRepository.getById(1);
        List<Employee> employeesUnderManger = employeeRepository.findAllByManagerId(employeeManager.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.get("/HR/employee/get/underManager").param("id", String.valueOf(employeeManager.getId()))
                .with(httpBasic("sara3", "mohamed@3"))
        ).andExpect(status().isForbidden());
    }

    @Test
    public void getEmployeeUnderNoneExistingManager() throws Exception {
        int id = 20;
        mockMvc.perform(MockMvcRequestBuilders.get("/HR/employee/get/underManager").param("id", String.valueOf(id))
                .with(httpBasic("nada1", "nada123"))
        ).andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(status().isNotFound()).andExpect(result -> assertEquals("no employee with this ID", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    public void getEmployeeUnderSomeManager() throws Exception {
        int managerId = 1;
        List<Employee> employeesUnderManger = employeeRepository.findAllUnderSomeManager(managerId);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(employeesUnderManger);
        mockMvc.perform(MockMvcRequestBuilders.get("/HR/employee/get/SomeManager").param("id", String.valueOf(managerId)).with(httpBasic("nada1", "nada123"))
        ).andExpect(status().isOk()).andExpect(content().json(body));
    }

    @Test
    public void getEmployeeUnderSomeManagerUnAuthorized() throws Exception {
        int managerId = 1;
        List<Employee> employeesUnderManger = employeeRepository.findAllUnderSomeManager(managerId);
        mockMvc.perform(MockMvcRequestBuilders.get("/HR/employee/get/SomeManager").param("id", String.valueOf(managerId))
                .with(httpBasic("sara", "nada123"))
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void getEmployeeUnderSomeManagerForbidden() throws Exception {
        int managerId = 1;
        mockMvc.perform(MockMvcRequestBuilders.get("/HR/employee/get/SomeManager").param("id", String.valueOf(managerId))
                .with(httpBasic("sara3", "mohamed@3"))
        ).andExpect(status().isForbidden());
    }


    @Test
    public void getEmployeeunderNonExistingSomeManager() throws Exception {
        int managerId = 52;
        mockMvc.perform(MockMvcRequestBuilders.get("/HR/employee/get/SomeManager").param("id", String.valueOf(managerId)).with(httpBasic("nada1", "nada123"))
                .with(httpBasic("nada1", "nada123"))
        ).andExpect(status().isNotFound()).andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals("no employee with this ID", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    public void deleteEmployeeWithNoManager() throws Exception { ///dont forget
        int id = 1;
        Employee employeeToDelete = employeeRepository.getById(id);
        mockMvc.perform(MockMvcRequestBuilders.delete("/HR/employee/delete")
                .param("id", String.valueOf(employeeToDelete.getId()))
                .with(httpBasic("nada1", "nada123"))
        ).andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals(" can't delete employee with no manager", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    public void deleteEmployeeWithManager() throws Exception { //check
        int id = 3;
        String message = "employee " + id + " is deleted";
        Employee employeeToDelete = employeeRepository.getById(id);
        mockMvc.perform(MockMvcRequestBuilders.delete("/HR/employee/delete")
                .param("id", String.valueOf(employeeToDelete.getId()))
                .with(httpBasic("nada1", "nada123")))
                .andExpect(status().isOk())
                .andExpect(content().string(message));
        assertEquals(employeeRepository.existsById(3), false);
    }

    @Test
    public void deleteEmployeeWithManagerUnAuthorized() throws Exception { //check
        int id = 3;
        Employee employeeToDelete = employeeRepository.getById(id);
        mockMvc.perform(MockMvcRequestBuilders.delete("/HR/employee/delete")
                .param("id", String.valueOf(employeeToDelete.getId()))
                .with(httpBasic("farah", "farah222")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void deleteEmployeeWithManagerForbidden() throws Exception { //check
        int id = 3;
        Employee employeeToDelete = employeeRepository.getById(id);
        mockMvc.perform(MockMvcRequestBuilders.delete("/HR/employee/delete")
                .param("id", String.valueOf(employeeToDelete.getId()))
                .with(httpBasic("sara3", "mohamed@3")))
                .andExpect(status().isForbidden());
    }


}
