package com.example.HR.Controllers;

import com.example.HR.Classes.Employee;
import com.example.HR.Services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/HR/employee")
public class EmployeesController {
    @Autowired
    EmployeeService employeeService;


    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured("HR")
    public @ResponseBody
    ResponseEntity<Employee> addNewEmployee(@RequestBody Employee employee) throws Exception {
        Employee newEmployee = employeeService.saveEmployee(employee);
        employeeService.generatePasswordAndUserName(newEmployee);
        return new ResponseEntity<>(newEmployee, HttpStatus.CREATED);
    }


    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<Employee> getEmployeeInfo(@RequestParam("id") String id) {
        Employee newEmployee = employeeService.getEmployeeInfoByID(Integer.parseInt(id));
        return new ResponseEntity<>(newEmployee, HttpStatus.OK);
    }


    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> updateEmployee(@RequestBody Employee EmployeeToModify, @RequestParam String id) {
        Employee originalEmployeeModified = employeeService.getEmployeeInfoByID(Integer.parseInt(id));
        employeeService.updateEmployee(EmployeeToModify, originalEmployeeModified);
        return new ResponseEntity<>(originalEmployeeModified, HttpStatus.OK);
    }


    @GetMapping(value = "/get/team", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<Employee> getEmployeesInTeam(@RequestParam("id") String id) {

        return employeeService.getEmployeesInTeam(Integer.parseInt(id));
    }
    //get department

    @GetMapping(value = "/get/underManager", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<Employee> getEmployeesUnderManager(@RequestParam("id") String id) {

        return employeeService.getEmployeesUnderManger(Integer.parseInt(id));
    }

    @GetMapping(value = "/get/SomeManager", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<Employee> getEmployeesUnderSomeManager(@RequestParam("id") String id) {

        return employeeService.getEmployeesOnSpeceficManger(Integer.parseInt(id));
    }

    @DeleteMapping(value = "/delete")
    public String deleteEmployee(@RequestParam("id") String id) {
        employeeService.delete(Integer.parseInt(id));
        return "employee " + id + " is deleted";

    }


}
