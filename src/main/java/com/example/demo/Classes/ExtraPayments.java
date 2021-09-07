package com.example.demo.Classes;

import javax.persistence.*;
import java.sql.Date;


@Entity
@Table(name="extra_payments")
public class ExtraPayments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="bonus")
    private double bonus;

    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee employee;

    @Column(name="date")
    private Date date;
    @Column(name="raise")
    private double raise;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getRaise() {
        return raise;
    }

    public void setRaise(double raise) {
        this.raise = raise;
    }
}
