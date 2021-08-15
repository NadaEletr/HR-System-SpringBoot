package com.example.demo.ServicesTest;

import com.example.demo.Classes.Employee;
import com.example.demo.Classes.EmployeeRepository;
import com.example.demo.Classes.EmployeeService;
import com.example.demo.DemoApplication;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes= DemoApplication.class)
public class EmployeeServiceTest {
    private MockMvc mvc;

   @MockBean
    EmployeeRepository employeeRepository;
    @Autowired
    EmployeeService employeeService;



    @Test
    public  void whenAddEmployee_ReturnEmployee()
    {
        Employee employee = new Employee();
        employee.setName("ahmed");
        employee.setGender('M');
        employee.setGraduation_date("2009");
        employee.setEmployeeId(5);
        given(employeeRepository.save(employee)).willReturn(employee);

        Employee result =  employeeService.saveEmployee(employee);
        assertEquals(result.getName(),employee.getName());

    }


}
