package com.example.demo.Controllers;


import com.example.demo.Classes.Employee;
import com.example.demo.Classes.SalaryDTO;
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
@RequestMapping(value = "/HR/SalaryHistory")
public class SalaryController {
@Autowired
SalaryHistoryService salaryHistoryService;

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<SalaryHistory> getEmployeeSalaryHistory(@RequestParam("id") String id)  {
        return salaryHistoryService.getEmployeeSalaryHistory(Integer.parseInt(id));
    }
    @PostMapping(value = "/add/raises", produces = MediaType.APPLICATION_JSON_VALUE)
    public
    String addNewEmployee(@RequestParam("id") int id,@RequestParam("raises") double raises)
    {
        salaryHistoryService.addRaise(id,raises);
        return "raises is added";
    }

}
