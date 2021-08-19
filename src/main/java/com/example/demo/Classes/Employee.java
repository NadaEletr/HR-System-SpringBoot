package com.example.demo.Classes;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "employee")
@JsonIgnoreProperties(value={"hibernateLazyInitializer"})
public class Employee {
    @Id
    @Column(name="employee_id")
    private int employeeId;
    @Column(name = "employee_name")
    private String name;
    //@Column(name = "Birth_date")
    //private String Birthdate; // comment
    @Column(name = "graduation_date")
    private Date graduation_date;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "gender")
    private char gender;
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;
    @JsonIgnore
    @OneToMany(mappedBy = "manager")
    private Set<Employee> employees;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="team_id")
    private Teams team;
    @Column(name = "birthdate")
    private Date birthDate;
    @Column(name="gross_salary")
    private double grossSalary;
    @Column(name="net_salary")
    private double netSalary;

    public static void transferEmployee (Employee updateEmployee, Employee originalEmployee)
    {
        if(updateEmployee.name !=null)
        {
            originalEmployee.setName(updateEmployee.name);
        }
        if(updateEmployee.employees!=null)
        {
            originalEmployee.setEmployees(updateEmployee.getEmployees());
        }
        if(updateEmployee.graduation_date!=null)
        {
            originalEmployee.setGraduation_date(updateEmployee.graduation_date);
        }
        if(updateEmployee.birthDate!= null)
        {
            originalEmployee.setBirthDate(updateEmployee.birthDate);
        }
        if(updateEmployee.manager!=null)
        {
            originalEmployee.setManager(updateEmployee.manager);
        }
        if(updateEmployee.gender!='\0')
        {
            originalEmployee.setGender(updateEmployee.gender);
        }
        if(updateEmployee.department !=null)
        {
            originalEmployee.setDepartment(updateEmployee.department);
        }
        if(updateEmployee.team !=null)
        {
            originalEmployee.setTeam(updateEmployee.team);
        }
        if(updateEmployee.grossSalary!=0d)
        {
            originalEmployee.setGrossSalary(updateEmployee.grossSalary);

        }

    }

    public Employee() {

    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Department getDepartment() {
        return department;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public Date getGraduation_date() {
        return graduation_date;
    }

    public void setGraduation_date(Date graduation_date) {
        this.graduation_date = graduation_date;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public Double getGrossSalary() {
        return grossSalary;
    }

    public void setGrossSalary(Double grossSalary) {

        this.grossSalary = grossSalary;
        this.netSalary=0.85*grossSalary-500;
    }

    public Double getNetSalary() {
        return netSalary;
    }

    public Teams getTeam() {
        return team;
    }

    public void setTeam(Teams team) {
        this.team = team;
    }

    public void setNetSalary(Double netSalary) {
        this.netSalary = netSalary;
    }
}


