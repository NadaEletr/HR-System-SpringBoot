package com.example.demo.Classes;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name="user_account")
public class UserAccount {
    @Id
    private String userName;

    @Column(name="password")
    private String password;

    @OneToOne
    @JoinColumn(name="employee_id")
    private Employee employee;

    @Column(name="roles")
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
    public List<String> getRolesList()
    {
        if(this.roles.toString().length()>0)
        {
            return Arrays.asList(this.roles.toString().split(",")) ;
        }
        return new ArrayList<>();
    }

}
