package com.moaydogdu.springboottesting.service;

import com.moaydogdu.springboottesting.exception.ResourceNotFoundException;
import com.moaydogdu.springboottesting.model.entity.Employee;
import com.moaydogdu.springboottesting.repository.EmployeeRepository;
import com.moaydogdu.springboottesting.service.impl.EmployeeServiceImpl;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;


    @BeforeEach
    public void setup() {
        //employeeRepository = Mockito.mock(EmployeeRepository.class);
        //employeeService = new EmployeeServiceImpl(employeeRepository);

        employee = Employee.builder()
                .id(1L)
                .firstName("Muhammet Oğuzhan")
                .lastName("AYDOĞDU")
                .email("m.o.aydogdu@outlook.com")
                .build();
    }

    // JUnit test for saveEmployee method
    @DisplayName("JUnit Test : save employee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {

        // given
        given(employeeRepository.findEmployeeByEmail(employee.getEmail()))
                .willReturn(Optional.empty());

        given(employeeRepository.save(employee))
                .willReturn(employee);

        // when
        Employee savedEmployee = employeeService.saveEmployee(employee);

        // then
        assertThat(savedEmployee).isNotNull();

    }

    // JUnit test for saveEmployee method which throws exception
    @DisplayName("JUnit Test : save employee method which throws exception")
    @Test
    public void givenExistinEmail_whenSaveEmployee_thenThrowsException() {

        // given
        given(employeeRepository.findEmployeeByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));

        // when
        org.junit.jupiter.api.Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> employeeService.saveEmployee(employee)
        );

        // then
        Mockito.verify(employeeRepository, Mockito.never())
                .save(any(Employee.class));
    }

    // JUnit test for getAllEmployees method
    @DisplayName("JUnit Test : get all employees method")
    @Test
    public void givenEmployeeList_whenGetAll_thenReturnEmployeeList(){
        // given
        Employee employee2 = Employee.builder()
                .id(2L)
                .firstName("Nurettin")
                .lastName("BAŞTÜRK")
                .email("nurettinbasturk@gmail.com")
                .build();

        given(employeeRepository.findAll())
                .willReturn(List.of(employee,employee2));

        // when
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).hasSize(2);

    }

    // JUnit test for getAllEmployees method (negative scenario)
    @DisplayName("JUnit Test : get all employees method (negative scenario)")
    @Test
    public void givenEmptyEmployeesLists_whenGetAll_thenReturnEmptyEmployeesList(){
        // given

        given(employeeRepository.findAll())
                .willReturn(List.of());

        // when
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then
        assertThat(employeeList).isEmpty();
        assertThat(employeeList).hasSize(0);

    }

    // JUnit test for getEmployeeById method
    @DisplayName("JUnit Test : get employee by id method")
    @Test
    public void givenEmployeeObject_whenGetEmployeeById_thenReturnEmployeeObject(){
        // given
        given(employeeRepository.findById(employee.getId()))
                .willReturn(Optional.of(employee));
        // when
        Employee employeeFromDb = employeeService.getEmployeeById(employee.getId()).get();

        // then
        assertThat(employeeFromDb).isNotNull();
        assertThat(employeeFromDb.getId()).isGreaterThan(0);

    }

    // JUnit test for  updateEmployee method
    @DisplayName("JUnit Test : update employee method")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee(){
        // given
        given(employeeRepository.save(employee))
                .willReturn(employee);
        employee.setEmail("test@gmail.com");

        // when
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        // then
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getEmail()).isEqualTo("test@gmail.com");

    }

    // JUnit test for deleteEmployee method
    @DisplayName("JUnit Test : delete employee method")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenDeleteEmployee(){
        // given
        long employeeId = 1L;

        BDDMockito.willDoNothing()
                .given(employeeRepository).deleteById(employeeId);
        // when

        employeeService.deleteEmployeeById(employeeId);

        // then
        verify(employeeRepository, Mockito.times(1))
                .deleteById(employeeId);

    }


}
