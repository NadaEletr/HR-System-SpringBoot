package com.example.HR.Services;

import com.example.HR.Classes.Employee;
import com.example.HR.Repositories.EarningsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
@Slf4j
public class EarningsService {
    @Autowired
    EarningsRepository earningsRepository;

    public double getBonus(Employee employee, Date date) {

        if (earningsRepository.existsByEmployee(employee)) {
            try {
                return earningsRepository.getBonusByDateAndEmployee(employee, date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0.0;
    }

    public double getRaise(Employee employee, Date date) {
        if (earningsRepository.existsByEmployee(employee)) {
            try {
                return earningsRepository.getRaiseByDateAndEmployee(employee, date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0.0;
    }

    public void deleteEarnings(Employee employee) {
        try{
            earningsRepository.deleteAllByEmployee(employee);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
