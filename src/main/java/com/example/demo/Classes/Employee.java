package com.example.demo.Classes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.sql.Date;
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
    private java.util.Date graduation_date;
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

    public java.util.Date getGraduation_date() {
        return graduation_date;
    }

    public void setGraduation_date(java.util.Date graduation_date) {
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


