package com.example.demo.Controllers;


import com.example.demo.Classes.Employee;
import com.example.demo.Classes.SalaryHistory;
import com.example.demo.Services.SalaryHistoryService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/HR/salaries")
public class SalaryController {
@Autowired
SalaryHistoryService salaryHistoryService;






}
