package com.example.demo.Security;

import com.example.demo.Classes.Employee;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "user_account")
@Getter
@Setter
public class UserAccount {
    @Id
    private String userName;

    @Column(name = "password")
    private String password;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "roles")
    @Enumerated(EnumType.STRING)
    private Roles roles;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

//    @JsonIgnore
//    public List<String> getRolesList() {
//        if (this.roles.length() > 0) {
//            return Arrays.asList(this.roles.split(","));
//        }
//        return new ArrayList<>();
//    }
//
//    public String getRoles() {
//        return roles;
//    }
//
//    public void setRoles(String roles) {
//        this.roles = roles;
//    }
}
