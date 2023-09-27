package com.moaydogdu.springboottesting.repository;

import com.moaydogdu.springboottesting.model.entity.Employee;
import org.assertj.core.api.Assertions;
import org.hibernate.query.sqm.mutation.internal.cte.CteInsertStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setup(){
        employee = Employee.builder()
                .firstName("Muhammet Oğuzhan")
                .lastName("AYDOĞDU")
                .email("m.o.aydogdu@outlook.com")
                .build();
    }

    // JUnit test for save employee operation.
    @Test
    @DisplayName("JUnit test for save employee operation.")
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
        // given - precondition or setup

        // when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.save(employee);


        // then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isNotEmpty();
        assertThat(employeeRepository.findAll().size()).isEqualTo(1);
    }

    // JUnit test for get all employees operation.
    @DisplayName("JUnit test for get all employees operation.")
    @Test
    public void givenEmployeesList_whenFindAll_thenEmployeesList() {
        // given
        Employee employee2 = Employee.builder()
                .firstName("Nurettin")
                .lastName("BAŞTÜRK")
                .email("nurettinbasturk@gmail.com")
                .build();

        employeeRepository.save(employee);
        employeeRepository.save(employee2);

        // when
        List<Employee> employeeList = employeeRepository.findAll();

        // then
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    // JUnit test for get employee by id operation.
    @DisplayName("JUnit test for get employee by id operation.")
    @Test
    public void givenEmployeeObject_whenFindEmployeeById_thenReturnEmployeeObjectFromDb() {
        // given

        employeeRepository.save(employee);

        // when
        Employee employeeFromDb = employeeRepository
                .findById(employee.getId())
                .get();

        // then
        assertThat(employeeFromDb).isNotNull();
    }

    // JUnit test for get employee by email operation.
    @DisplayName("JUnit test for get employee by email operation.")
    @Test
    public void givenEmployeeObject_whenFindEmployeeByEmail_thenReturnEmployeeObjectFromDb() {
        // given
        employeeRepository.save(employee);

        // when
        Employee employeeFromDb = employeeRepository
                .findEmployeeByEmail(employee.getEmail())
                .get();

        // then
        assertThat(employeeFromDb).isNotNull();
    }

    // JUnit test for update employee operation.
    @DisplayName("JUnit test for update employee operation.")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployeeObjectFromDb() {
        // given
        employeeRepository.save(employee);

        // when
        Employee employeeEntityFromDb = employeeRepository
                .findById(employee.getId())
                .get();

        employeeEntityFromDb.setLastName("GÜNDOĞDU");
        employeeRepository.save(employeeEntityFromDb);

        Employee updatedEmployeeEntityFromDb = employeeRepository
                .findById(employee.getId())
                .get();

        // then
        assertThat(updatedEmployeeEntityFromDb.getLastName()).isEqualTo("GÜNDOĞDU");

    }

    // JUnit test for delete employee operation.
    @DisplayName("JUnit test for delete employee operation.")
    @Test
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee() {
        // given
        employeeRepository.save(employee);

        // when
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> employeeOptional = employeeRepository
                .findById(employee.getId());

        // then
        assertThat(employeeOptional).isEmpty();
    }

    // JUnit test for custom query using JPQL with index params.
    @DisplayName("JUnit test for custom query using JPQL with index params.")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQL_thenRetunEmployeeObject() {
        // given
        employeeRepository.save(employee);

        String firstName = "Muhammet Oğuzhan";
        String lastName = "AYDOĞDU";

        // when
        Employee savedEmployee = employeeRepository
                .findByJPQL(firstName, lastName);

        // then
        assertThat(savedEmployee).isNotNull();
    }

    // JUnit test for custom query using JPQL with named params.
    @DisplayName("JUnit test for custom query using JPQL with named params.")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenRetunEmployeeObject() {
        // given
        employeeRepository.save(employee);

        String firstName = "Muhammet Oğuzhan";
        String lastName = "AYDOĞDU";

        // when
        Employee savedEmployee = employeeRepository
                .findByJPQLNamedParams(firstName, lastName);

        // then
        assertThat(savedEmployee).isNotNull();
    }

    // JUnit test for custom query using native SQL.
    @DisplayName("JUnit test for custom query using native SQL.")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQL_thenRetunEmployeeObject() {
        // given
        employeeRepository.save(employee);

        //String firstName = "Muhammet Oğuzhan";
        //String lastName = "AYDOĞDU";

        // when
        Employee savedEmployee = employeeRepository
                .findByNativeSQL(employee.getFirstName(), employee.getLastName());

        // then
        assertThat(savedEmployee).isNotNull();
    }

    // JUnit test for custom query using native SQL with named params.
    @DisplayName("JUnit test for custom query using native SQL with named params.")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQLWithParams_thenRetunEmployeeObject() {
        // given
        employeeRepository.save(employee);

        //String firstName = "Muhammet Oğuzhan";
        //String lastName = "AYDOĞDU";

        // when
        Employee savedEmployee = employeeRepository
                .findByNativeSQLNamedParams(employee.getFirstName(), employee.getLastName());

        // then
        assertThat(savedEmployee).isNotNull();
    }



}
