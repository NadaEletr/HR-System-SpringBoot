package com.example.demo.Services;

import com.example.demo.Classes.Employee;
import com.example.demo.Repositories.ExtraPaymentsRepository;
import com.example.demo.errors.ConflictException;
import com.example.demo.errors.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;


@Service
@Slf4j
public class ExtraPaymentsService {
    @Autowired
     ExtraPaymentsRepository extraPaymentRepository;

     public double getBonus(Employee employee, Date date) throws Exception {

         if(extraPaymentRepository.existsByEmployee(employee)){
             try{
                 return extraPaymentRepository.getBonusByDateAndEmployee(employee,date);
             }catch (Exception e)
             {
                 e.printStackTrace();
             }
         }
         return 0.0;
     }



    public double getRaise(Employee employee, Date date){
        if(extraPaymentRepository.existsByEmployee(employee))
        {
            try{
                return extraPaymentRepository.getRaiseByDateAndEmployee(employee,date);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return 0.0;

     }


}
