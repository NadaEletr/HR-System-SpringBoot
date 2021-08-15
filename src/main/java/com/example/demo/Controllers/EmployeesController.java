package com.example.demo.Controllers;
import com.example.demo.Classes.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/HR")
public class EmployeesController {
    @Autowired
    EmployeeService employeeService;
    @PostMapping(value = "/add",produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<Employee> addNewEmployee(@RequestBody Employee employee)
    {
        Employee newEmployee = employeeService.saveEmployee(employee);
        return new ResponseEntity<>(newEmployee,HttpStatus.OK);
    }

}
