package com.example.demo.Classes;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;


@Entity
@Table(name = "extra_payments")
@Getter
@Setter
public class ExtraPayments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "bonus")
    private double bonus;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "date")
    private Date date;
    @Column(name = "raise")
    private double raise;

}
