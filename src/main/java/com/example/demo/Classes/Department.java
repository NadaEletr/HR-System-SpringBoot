package com.example.demo.Classes;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "department")
public class Department {
    @Id
    @GeneratedValue(strategy =GenerationType.AUTO)
    int departmentId;

    @Column(name = "department_name")
    String departmentName;
    @JsonIgnore
    @OneToMany(mappedBy = "department")
    Set<Employee> employees;

    public Department()
    {

    }


    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }
}
