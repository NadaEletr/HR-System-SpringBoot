package com.example.demo.IntegerationTests;

import com.example.demo.ScheduledTasks.ScheduledTasks;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskHolder;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.Calendar;
import java.util.Set;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)

public class ScheduledTasksTests {
    @Autowired
    private ScheduledTaskHolder scheduledTaskHolder;
    @Test
    public void testYearlyCronTaskScheduled()
    {
//
    }


}
    //Set<ScheduledTask> scheduledTasks = scheduledTaskHolder.getScheduledTasks();
//        scheduledTasks.forEach(scheduledTask -> scheduledTask.getTask().getRunnable().getClass().getDeclaredMethods());
//        long count = scheduledTasks.stream()
//                .filter(scheduledTask -> scheduledTask.getTask() instanceof CronTask)
//                .map(scheduledTask -> (CronTask) scheduledTask.getTask())
//                .filter(cronTask -> cronTask.getExpression().equals("0 0 0 1 1 *") && cronTask.toString().equals("nl.benooms.scheduledannotationplayground.FixedDateSchedule.OnceAYearCron"))
//                .count();
//        assertThat(count).isEqualTo(1L);
