package com.example.demo.Classes;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="teams")
public class Teams {
    @Id
    private int teamId;

    @Column(name="team_name")
    private String teamName;

   @OneToMany(mappedBy = "team")
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
