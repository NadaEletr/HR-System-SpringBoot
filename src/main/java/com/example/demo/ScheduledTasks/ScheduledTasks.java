package com.example.demo.ScheduledTasks;

import com.example.demo.Repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class ScheduledTasks {
    @Autowired
    EmployeeRepository employeeRepository;
    @Transactional
    @Scheduled(cron = "0 0 0 1 1 *")
    public void updateYearlyEmployeeLeaves() {
        employeeRepository.updateYearlyLeaves(0);
    }
}
