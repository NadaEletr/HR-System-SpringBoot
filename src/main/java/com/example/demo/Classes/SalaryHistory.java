package com.example.demo.Classes;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name="salary_history")
public class SalaryHistory {
    @Id
    @Column(name="id")
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
    private final double insurance=500;
    @Column(name = "bonus")
    private double bonus;
    @Column(name = "net_salary")
    private double netSalary;
    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee employee;

    public SalaryHistory() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getRaises() {
        return raises;
    }

    public void setRaises(double raises) {
        this.raises = raises;
    }

    public double getExceededLeaves() {
        return exceededLeaves;
    }

    public void setExceededLeaves(double exceededLeaves) {
        this.exceededLeaves = exceededLeaves;
    }

    public double getTaxes() {
        return taxes;
    }

    public void setTaxes(double taxes) {
        this.taxes = taxes;
    }

    public double getInsurance() {
        return insurance;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public double getNetSalary() {
        return netSalary;
    }

    public void setNetSalary(double netSalary) {
        this.netSalary = netSalary;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

}
