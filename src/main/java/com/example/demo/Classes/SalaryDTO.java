package com.example.demo.Classes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalaryDTO {
    private double netSalary;
    private double grossSalary;

    public SalaryDTO(Employee employee) {
        if (employee != null) {
            this.netSalary = employee.getNetSalary();
            this.grossSalary = employee.getGrossSalary();
        }
    }

//    public double getNetSalary() {
//        return netSalary;
//    }
//
//    public void setNetSalary(double netSalary) {
//        this.netSalary = netSalary;
//    }
//
//    public double getGrossSalary() {
//        return grossSalary;
//    }

    public void setGrossSalary(double grossSalary) {
        this.grossSalary = grossSalary;
    }
}
