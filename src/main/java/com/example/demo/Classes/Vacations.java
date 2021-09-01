package com.example.demo.Classes;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;

@Entity
@Table(name="vacations")
public class Vacations {
    @Id
    int id;
    @Column(name="Date")
    Date date;
    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee employee;
    @Column(name="exceeded")
    private int exceeded;
    public Vacations(Date date) {
        this.id = id;
        this.date = date;

    }

    public Vacations() {

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

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public int getExceeded() {
        return exceeded;
    }

    public void setExceeded(int exceeded) {
        this.exceeded = exceeded;
    }
}
