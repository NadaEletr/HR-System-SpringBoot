package com.example.demo.Repositories;

import com.example.demo.Classes.Employee;
import com.example.demo.Classes.Vacations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VacationRepository  extends JpaRepository<Vacations, Integer> {

}
