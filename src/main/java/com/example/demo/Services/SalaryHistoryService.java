package com.example.demo.Services;

import com.example.demo.Classes.AllowedVacations;
import com.example.demo.Classes.Employee;
import com.example.demo.Classes.SalaryHistory;
import com.example.demo.Repositories.EmployeeRepository;
import com.example.demo.Repositories.SalaryHistoryRepository;
import com.example.demo.Repositories.VacationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalaryHistoryService {
    @Autowired
    EmployeeService employeeService;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    SalaryHistoryRepository salaryHistoryRepository;

//
//    public void updateSalary(int employeeId)
//    {
//        Employee employee= employeeService.getEmployeeInfoByID(employeeId);
//        SalaryHistory salaryHistory=new SalaryHistory();
//       if(checkExceededLeaves(employee))
//       {
//            salaryHistory.setExceededLeaves(50);
//           salaryHistory.setNetSalary(employee.getNetSalary()-salaryHistory.getExceededLeaves());
//           salaryHistory.setEmployee(employee);
//           salaryHistoryRepository.save(salaryHistory);
//       }
//
//
//    }
//    public boolean checkExceededLeaves(Employee employee)
//    {
//        int countLeaves =employeeRepository.getLeaves(employee.getNationalId());
//        if(employee.getAcceptableLeaves()== AllowedVacations.LessEXPERIENCED&&countLeaves>AllowedVacations.LessEXPERIENCED)
//        {
//            return true;
//        }
//        else if (employee.getAcceptableLeaves()==AllowedVacations.EXPERIENCED &&countLeaves>AllowedVacations.EXPERIENCED)
//        {
//            return true;
//
//        }
//        return false;
//    }



}
