package com.moaydogdu.springboottesting.service.impl;

import com.moaydogdu.springboottesting.exception.ResourceNotFoundException;
import com.moaydogdu.springboottesting.model.entity.Employee;
import com.moaydogdu.springboottesting.repository.EmployeeRepository;
import com.moaydogdu.springboottesting.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
//@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    // Constructor Injection
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {

        Optional<Employee> savedEmployee = employeeRepository
                .findEmployeeByEmail(employee.getEmail());

        if (savedEmployee.isPresent()){
            throw new ResourceNotFoundException(
                    "Employee already exists with given email: " + employee.getEmail()
            );
        }

        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployeeById(long id) {
        employeeRepository.deleteById(id);
    }
}
