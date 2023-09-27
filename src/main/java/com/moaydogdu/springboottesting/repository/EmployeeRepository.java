package com.moaydogdu.springboottesting.repository;

import com.moaydogdu.springboottesting.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee,String> {

    Optional<Employee> findEmployeeByEmail(String email);

    // define custom query using JPQL with index params
    @Query("SELECT e FROM Employee e WHERE e.firstName = ?1 AND e.lastName = ?2")
    Employee findByJPQL(
            String firstName,
            String lastName
    );


    // define custom query using JPQL with named params
    @Query("SELECT e FROM Employee e " +
            "WHERE e.firstName = :firstName AND e.lastName = :lastName")
    Employee findByJPQLNamedParams(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName
    );

    // define custom query using native query
    @Query(
            value = "SELECT * FROM employees e " +
            "WHERE e.first_name = ?1 AND e.last_name = ?2",
            nativeQuery = true
    )
    Employee findByNativeSQL(
            String firstName,
            String lastName
    );

    // define custom query using Native SQL with named params
    @Query(
            value = "SELECT * FROM employees e " +
            "WHERE e.first_name = :firstName AND e.last_name = :lastName",
            nativeQuery = true
    )
    Employee findByNativeSQLNamedParams(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName
    );
}
