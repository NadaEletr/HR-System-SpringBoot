package com.example.demo.Classes;

import com.example.demo.Security.UserAccount;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "employee")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
@Getter
@Setter
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "national_id")
    private String nationalId;
    @Column(name = "first_name")
    private String first_name;
    @Column(name = "last_name")
    private String last_name;
    @Column(name = "leaves")
    private Integer leaves = 0;
    @Column(name = "employee_degree")
    @Enumerated(EnumType.STRING)
    private Degree degree;
    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;
    @Column(name = "graduation_date")
    private Date graduation_date;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
    @OneToMany(mappedBy = "employee")
    private List<ExtraPayments> extraPayments;
    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;
    @JsonIgnore
    @OneToMany(mappedBy = "manager", fetch = FetchType.EAGER)
    private Set<Employee> employees;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "team_id")
    private Teams team;
    @Column(name = "birthdate")
    private Date birthDate;
    @Column(name = "gross_salary")
    private double grossSalary;
    @Column(name = "net_salary")
    private Double netSalary;
    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    private List<Absence> absences;
    @Column(name = "acceptable_leaves")
    private Integer acceptableLeaves = 0;
    @JsonIgnore
    @OneToMany(mappedBy = "employee")
    private List<SalaryDetails> salaryHistories;
    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserAccount userAccount;
    public Employee() {
        this.leaves = 0;
    }

}


