package com.example.HR.Classes;

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

}
