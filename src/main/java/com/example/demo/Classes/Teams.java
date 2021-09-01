package com.example.demo.Classes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="teams")
public class Teams {
    @Id

    private int teamId;

    @Column(name="team_name")
    private String teamName;
    @JsonIgnore
   @OneToMany(mappedBy = "team",cascade = CascadeType.ALL)
    Set<Employee> employees ;

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

}
