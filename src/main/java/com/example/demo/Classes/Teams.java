package com.example.demo.Classes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "teams")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Teams {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private int teamId;

    @Column(name = "team_name")
    private String teamName;
    @JsonIgnore
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    Set<Employee> employees;



}
