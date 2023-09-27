package com.moaydogdu.springboottesting.repository;

import com.moaydogdu.springboottesting.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee,String> {

    Optional<Employee> findEmployeeByEmail(String email);
}
