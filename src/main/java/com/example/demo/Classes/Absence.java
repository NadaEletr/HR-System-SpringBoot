package com.example.demo.Classes;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;

@Entity
@Table(name="Absence")
public class Absence {
    @Id
    int id;
    @Column(name="Date")
    Date date;
    @ManyToOne
    @JoinColumn(name="employee_id")
    @JsonIgnore
    private Employee employee;
    public Absence(Date date) {
        this.id = id;
        this.date = date;

    }

    public Absence() {

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

}
