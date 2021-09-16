package com.example.demo.Classes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;

@Entity
@Table(name = "Absence")
public class Absence {
    @Id
    @Getter
    @Setter
    int id;
    @Column(name = "Date")
    @Getter
    @Setter
    Date date;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    @JsonIgnore
    @Getter
    @Setter
    private Employee employee;

    public Absence(Date date) {
        this.id = id;
        this.date = date;
    }

    public Absence() {
    }

}
