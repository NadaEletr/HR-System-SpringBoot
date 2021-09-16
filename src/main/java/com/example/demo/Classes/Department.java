package com.example.demo.Classes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "department")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int departmentId;
    @Column(name = "department_name")
    String departmentName;
    @JsonIgnore
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    Set<Employee> employees;

    public Department() {

    }

}
