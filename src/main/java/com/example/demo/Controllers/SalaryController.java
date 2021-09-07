package com.example.demo.Controllers;


import com.example.demo.Classes.ExtraPayments;
import com.example.demo.Classes.SalaryDetails;
import com.example.demo.Services.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/HR/SalaryHistory")
public class SalaryController {
@Autowired
SalaryService salaryService;

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<SalaryDetails> getEmployeeSalaryHistory(@RequestParam("id") String id)  {
        return salaryService.getEmployeeSalaryHistory(Integer.parseInt(id));
    }
//    @PostMapping(value = "/add/raises", produces = MediaType.APPLICATION_JSON_VALUE)
//    public
//    String addNewEmployee(@RequestParam("id") int id,@RequestParam("raises") double raises)
//    {
//        salaryHistoryService.addRaise(id,raises);
//        return "raises is added";
//    }
    @PostMapping(value = "/add/extraPayments", produces = MediaType.APPLICATION_JSON_VALUE)
    public
    String addBonusAndRaise(@RequestBody ExtraPayments extraPayments)
    {
        salaryService.addExtraPayments(extraPayments);
        return "extra payments is added!";
    }


}
