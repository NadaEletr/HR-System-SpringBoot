package com.example.HR.DTO;

import com.example.HR.Classes.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class EmployeeDTO {
    private int id;
    private String nationalId;
    private String firstName;
    private String lastName;
    private Degree degree;
    private Integer yearsOfExperience;
    private Date graduation_date;
    private Department department;
    private Gender gender;
    private Teams team;
    private Date birthDate;
    private double grossSalary;
    private Double netSalary;
    private Integer acceptableLeaves = 0;

    public static void setEmployeeToDTO(Employee employee, EmployeeDTO DTO)
    {
        ModelMapperGenerator.getModelMapperSingleton().map(employee, DTO);
    }
}
