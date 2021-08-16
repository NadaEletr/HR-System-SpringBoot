package com.example.demo.ServicesTest;

import com.example.demo.Classes.Employee;
import com.example.demo.Services.EmployeeService;
import com.example.demo.DemoApplication;
import javassist.NotFoundException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.sql.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes= DemoApplication.class)
public class EmployeeServiceTest {


//   @MockBean
//    EmployeeRepository employeeRepository;
    @Autowired
    EmployeeService employeeService;



    @Test
    public  void whenAddEmployee_ReturnEmployee() throws NotFoundException {
        Employee employee = new Employee();
        employee.setName("ahmed");
        employee.setGender('M');
        Employee result =  employeeService.saveEmployee(employee);
        assertThat(result).usingRecursiveComparison().isEqualTo(employee);

    }
    @Test
    public void whenGetEmployeeReturnEmployee() throws NotFoundException {

        Employee result = employeeService.getEmployeeInfoByID(1);
        assertEquals(result.getEmployeeId(),1);

    }

   @Test
    public void deleteEmployee() throws NotFoundException {
        Employee employee = employeeService.getEmployeeInfoByID(5);
        employeeService.deleteEmployee(employee.getEmployeeId());
       assertEquals(employeeService.existsById(employee.getEmployeeId()),false);
   }

   @Test

    public void updateEmployee() throws NotFoundException {

        Employee employee = employeeService.getEmployeeInfoByID(8);
        employee.setName("rwan");
        employee.setGender('F');
        Employee updatedEmployee = employeeService.updateEmployee(employee,String.valueOf(employee.getEmployeeId()));
        assertThat(updatedEmployee).usingRecursiveComparison().isEqualTo(employee);
   }







}
