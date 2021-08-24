package com.example.demo.IntegerationTests;

import com.example.demo.Classes.Department;
import com.example.demo.Classes.Employee;
import com.example.demo.Classes.SalaryDTO;
import com.example.demo.Classes.Teams;
import com.example.demo.Repositories.DepartmentRepository;
import com.example.demo.Repositories.EmployeeRepository;
import com.example.demo.Repositories.TeamRepository;
import com.example.demo.Services.EmployeeService;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
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

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
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
    EmployeeRepository employeeRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Test
    public void getEmployeeSalary() throws Exception {
        int id=1;
        Employee employee = employeeRepository.getById(id);
        SalaryDTO salaryDTO = new SalaryDTO(employee);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(salaryDTO);
        mockMvc.perform(MockMvcRequestBuilders.get("/HR/employee/get/Salaries")
                .param("id", String.valueOf(employee.getEmployeeId())))
                .andExpect(status().isOk()).andExpect((content().json(body)));
    }

    @Test
    public void getEmployee() throws Exception {
        int id = 2;
        Employee employee = employeeRepository.getById(id);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(employee);
        mockMvc.perform(MockMvcRequestBuilders.get("/HR/employee/get")
                .param("id", String.valueOf(id))).andExpect(status().isOk())
                .andExpect((content().json(body)));
        assertEquals(employee.getEmployeeId(),id);
    }
    @Test
    public void getEmployeesInTeam() throws Exception {
        int teamId = 1;
        List<Employee> employees = employeeService.getEmployeesInTeam(teamId);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(employees);
        mockMvc.perform(MockMvcRequestBuilders.get("/HR/employee/get/team")
                .param("id", String.valueOf(teamId))).andExpect(content().json(body)).andExpect(jsonPath("$", hasSize(employees.size())))
                .andExpect(status().isOk());
    }

    @Test
    public void addEmployee() throws Exception {
        Optional<Teams> team = teamRepository.findById(2);
        Optional<Department> department = departmentRepository.findById(1);
        Employee manager = employeeRepository.getById(1);
        Employee employee = new Employee();
        employee.setName("youssef");
        employee.setGender('M');
        employee.setGrossSalary(1223330d);
        employee.setTeam(team.get());
        employee.setDepartment(department.get());
        employee.setManager(manager);
        System.out.println(employee.getTeam());
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(employee);
        employee.setNetSalary((employee.getGrossSalary() * 0.85 - 500));
        String Result = objectMapper.writeValueAsString(employee);
        mockMvc.perform(MockMvcRequestBuilders.post("/HR/employee/add").contentType(MediaType.APPLICATION_JSON)
                .content(body)).andExpect(status().isCreated())
                .andExpect(content().json(Result));
        Employee resultEmployee= employeeRepository.getById(employee.getEmployeeId());
        assertEquals(resultEmployee.getEmployeeId(),employee.getEmployeeId());
        assertEquals(resultEmployee.getName(),resultEmployee.getName());
    }

    @Test
    public void updateEmployee() throws Exception {
        int employeeId = 1;
        Employee employee=employeeRepository.getById(employeeId);
        Employee updateEmployee = new Employee();
        updateEmployee.setEmployeeId(employeeId);
        updateEmployee.setGender('F');
        updateEmployee.setName("sara");
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(updateEmployee);
        mockMvc.perform(MockMvcRequestBuilders.put("/HR/employee/update")
                .contentType(MediaType.APPLICATION_JSON).content(body)
                .param("id", String.valueOf(employeeId)))
                .andExpect(status().isOk());
        assertEquals(employee.getEmployeeId(),employeeId);
        assertEquals(employee.getName(),updateEmployee.getName());
        assertEquals(employee.getGender(),updateEmployee.getGender());
    }

    @Test
    public void deleteEmployee() throws Exception {
        int id = 2;
        String message = "employee is deleted";
        Employee employeeToDelete=employeeRepository.getById(id);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(message);
        mockMvc.perform(MockMvcRequestBuilders.delete("/HR/employee/delete").param("id", String.valueOf(id))
        ).andExpect(status().isOk()).andExpect(content().string(message));
        assertEquals(employeeService.existsById(employeeToDelete.getEmployeeId()),false);
    }

    @Test
    public void getEmployeeUnderManager() throws Exception {
        Employee employeeManager = employeeRepository.getById(1);
        List<Employee> employeesUnderManger = employeeRepository.findAllByManagerEmployeeId(employeeManager.getEmployeeId());
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(employeesUnderManger);
        mockMvc.perform(MockMvcRequestBuilders.get("/HR/employee/get/SomeManager").param("id", String.valueOf(employeeManager.getEmployeeId()))
        ).andExpect(status().isOk()).andExpect(content().json(body));
    }

//    @Test
//    public void getEmployeeUnderSomeManager() throws Exception {
//        int managerId = 3;
//        List<Employee> employeesUnderManger = employeeRepository.findAllUnderSomeManager(managerId);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String body = objectMapper.writeValueAsString(employeesUnderManger);
//        mockMvc.perform(MockMvcRequestBuilders.get("/HR/employee/get/SomeManager").param("id", String.valueOf(managerId))
//        ).andExpect(status().isOk()).andExpect(content().json(body));
//    }
//
//    @Test
//    public void deleteManagerAndUpdateEmployee() throws Exception {
//        int id = 8;
//        String message = "manager " + id + " is deleted";
//        ObjectMapper objectMapper = new ObjectMapper();
//        String body = objectMapper.writeValueAsString(message);
//        mockMvc.perform(MockMvcRequestBuilders.delete("/HR/employee/delete/manager").param("id", String.valueOf(id))
//        ).andExpect(status().isOk()).andExpect(content().string(message));
//    }


}
