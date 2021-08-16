package com.example.demo.ServicesTest;

import com.example.demo.Classes.Employee;
import com.example.demo.Services.EmployeeService;
import com.example.demo.DemoApplication;
import javassist.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes= DemoApplication.class)
public class EmployeeServiceTest {


//   @MockBean
//    EmployeeRepository employeeRepository;
    @Autowired
    EmployeeService employeeService;



    @Test
    public  void whenAddEmployee_ReturnEmployee() throws NotFoundException {
        Employee employee = new Employee();
        employee.setName("youssef");
        employee.setGender('M');
        employee.setGraduation_date("2009");

//        given(employeeRepository.save(employee)).willReturn(employee);
        Employee result =  employeeService.saveEmployee(employee);
        assertEquals(result.getName(),employee.getName());

    }
    @Test
    public void whenAddEmployee_ConflictException()
    {

    }






}
