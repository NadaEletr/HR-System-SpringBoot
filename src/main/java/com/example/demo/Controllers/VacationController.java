package com.example.demo.Controllers;

import com.example.demo.Classes.Employee;
import com.example.demo.Classes.SalaryDTO;
import com.example.demo.Classes.Teams;
import com.example.demo.Repositories.EmployeeRepository;
import com.example.demo.Services.EmployeeService;
import com.example.demo.Services.VacationService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/HR/Record/leave")
public class VacationController {
    @Autowired
    VacationService vacationService;
    @Autowired
    EmployeeService employeeService;
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String
    recordLeave(@RequestParam("id")int id) throws NotFoundException {
        vacationService.recordLeave(id);
        Employee employee =employeeService.getEmployeeInfoByID(id);
        return "your absence are "+employee.getLeaves();
    }

}
