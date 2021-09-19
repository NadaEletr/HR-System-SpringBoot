package com.example.HR.DTO;

import com.example.HR.Classes.*;
import com.example.HR.Security.UserAccount;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.Set;

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
