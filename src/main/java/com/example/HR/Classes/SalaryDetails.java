package com.example.HR.Classes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;


@Entity
@Table(name = "salary_history")
@Getter
@Setter
public class SalaryDetails {
    @Id
    @JsonIgnore
    @Column(name = "id")
    private int id;
    @Column(name = "date")
    private Date date;
    @Column(name = "raises")
    private double raises;
    @Column(name = "exceeded_leaves")
    private double exceededLeaves;
    @Column(name = "taxes")
    private double taxes;
    @Column(name = "insurance")
    private double insurance;
    @Column(name = "bonus")
    private double bonus;
    @Column(name = "net_salary")
    private double netSalary;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public SalaryDetails() {
    }


}
