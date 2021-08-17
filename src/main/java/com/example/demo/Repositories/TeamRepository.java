package com.example.demo.Repositories;

import com.example.demo.Classes.Department;
import com.example.demo.Classes.Teams;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Teams, Integer> {
}
