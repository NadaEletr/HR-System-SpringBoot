package com.example.demo.ServicesTest;

import com.example.demo.Classes.Department;
import com.example.demo.Classes.Employee;
import com.example.demo.DemoApplication;
import com.example.demo.Services.DepartmentService;
import com.example.demo.Services.EmployeeService;
import javassist.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes= DemoApplication.class)
public class DepartmentServiceTest {

    @Autowired
    DepartmentService departmentService;



    @Test
    public  void whenAddDepartmentReturnDepartment() throws NotFoundException {
        Department department = new Department();
        department.setDepartmentName("design");
        Department result =  departmentService.saveEmployee(department);
        assertEquals(result,department);

    }

}
