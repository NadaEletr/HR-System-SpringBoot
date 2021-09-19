package com.example.HR.Controllers;

import com.example.HR.Classes.Department;
import com.example.HR.Services.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/department")
public class DepartmentController {
    @Autowired
    DepartmentService departmentService;

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<Department> addNewEmployee(@RequestBody Department department) {
        Department newDepartment = departmentService.saveEmployee(department);
        return new ResponseEntity<>(newDepartment, HttpStatus.CREATED);
    }


    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Department getTeams(@RequestParam("id") String id) {
        return departmentService.getDepartment(Integer.parseInt(id));
    }
}

