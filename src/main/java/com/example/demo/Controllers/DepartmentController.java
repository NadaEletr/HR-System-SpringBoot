package com.example.demo.Controllers;

import com.example.demo.Classes.Department;
import com.example.demo.Services.DepartmentService;
import javassist.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/HR/department")
public class DepartmentController {
    @Autowired
    DepartmentService departmentService;

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<Department> addNewEmployee(@RequestBody Department department) throws NotFoundException {
        Department newDepartment = departmentService.saveEmployee(department);
        return new ResponseEntity<>(newDepartment, HttpStatus.CREATED);
    }

}
