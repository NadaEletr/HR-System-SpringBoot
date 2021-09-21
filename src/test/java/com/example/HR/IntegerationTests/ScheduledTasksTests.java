package com.example.HR.IntegerationTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskHolder;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ScheduledTasksTests {
    @Autowired
    private ScheduledTaskHolder scheduledTaskHolder;

    @Test
    public void testYearlyCronTaskScheduled() {
        Set<ScheduledTask> scheduledTasks = scheduledTaskHolder.getScheduledTasks();
        scheduledTasks.forEach(scheduledTask -> scheduledTask.getTask().getRunnable().getClass().getDeclaredMethods());
        long count = scheduledTasks.stream()
                .filter(scheduledTask -> scheduledTask.getTask() instanceof CronTask)
                .map(scheduledTask -> (CronTask) scheduledTask.getTask())
                .filter(cronTask -> cronTask.getExpression().equals("0 0 0 1 1 *") && cronTask.toString().equals("com.example.HR.ScheduledTasks.ScheduledTasks.updateYearlyEmployeeLeaves"))
                .count();
        assertThat(count).isEqualTo(1L);
    }

    @Test
    public void testMonthlyCronTaskScheduled() {
        Set<ScheduledTask> scheduledTasks = scheduledTaskHolder.getScheduledTasks();
        scheduledTasks.forEach(scheduledTask -> scheduledTask.getTask().getRunnable().getClass().getDeclaredMethods());
        long count = scheduledTasks.stream()
                .filter(scheduledTask -> scheduledTask.getTask() instanceof CronTask)
                .map(scheduledTask -> (CronTask) scheduledTask.getTask())
                .filter(cronTask -> cronTask.getExpression().equals("0 0 0 25 * *") && cronTask.toString().equals("com.example.HR.Services.SalaryService.generateAllEmployeesMonthlySalary"))
                .count();
        assertThat(count).isEqualTo(1L);
    }


}
