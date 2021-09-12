package com.example.demo.Classes;

import com.example.demo.Security.UserAccount;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "employee")
@JsonIgnoreProperties(value={"hibernateLazyInitializer"})
public class Employee {
    @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(name="national_id")
    private String nationalId;
    @Column(name = "first_name")
    private String first_name;
    @Column(name = "last_name")
    private String last_name;
    @Column(name = "leaves")
    private Integer leaves=0;
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
    @OneToMany(mappedBy = "manager",fetch=FetchType.EAGER)
    private Set<Employee> employees;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="team_id")
    private Teams team;
    @Column(name = "birthdate")
    private Date birthDate;
    @Column(name="gross_salary")
    private double grossSalary;
    @Column(name="net_salary")
    private Double netSalary;
    @Column(name="salary_raise")
    private Double salaryRaise;
    @Column(name="joined_year")
    private Date joinedYear;
    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    private List<Absence> absences;
    @Column(name="acceptable_leaves")
    private Integer acceptableLeaves=0;
    @JsonIgnore
    @OneToMany(mappedBy = "employee")
    private List<SalaryDetails> salaryHistories;
    @OneToOne(mappedBy = "employee",cascade = CascadeType.ALL, orphanRemoval = true)
    private UserAccount userAccount;


    public Employee() {
        this.leaves=0;

    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
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


    public Date getGraduation_date() {
        return graduation_date;
    }

    public void setGraduation_date(Date graduation_date) {
        this.graduation_date = graduation_date;
    }


    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String name) {
        this.first_name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Double getGrossSalary() {
        return grossSalary;
    }

    public void setGrossSalary(Double grossSalary) {
        this.grossSalary = grossSalary;

    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
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



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public Integer getLeaves() {
        return leaves;
    }

    public void setLeaves(Integer leaves) {
        if(leaves==null)
        {
            this.leaves=0;
        }
        else
            this.leaves = leaves;
    }

    public List<SalaryDetails> getSalaryHistories() {
        return salaryHistories;
    }

    public void setSalaryHistories(List<SalaryDetails> salaryHistories) {
        this.salaryHistories = salaryHistories;
    }

    public List<Absence> getAbsences() {
        return absences;
    }

    public void setVacations(List<Absence> vacations) {
        this.absences = vacations;
    }

    public Integer getAcceptableLeaves() {
        return acceptableLeaves;
    }

    public void setAcceptableLeaves(Integer acceptableLeaves) {
        this.acceptableLeaves = acceptableLeaves;
    }

    public Date getJoinedYear() {
        return joinedYear;
    }

    public void setJoinedYear(Date joinedYear) {
        this.joinedYear = joinedYear;
    }

    public Double getSalaryRaise() {
        return salaryRaise;
    }

    public void setSalaryRaise(Double salaryRaise) {
        this.salaryRaise = salaryRaise;
    }

}


