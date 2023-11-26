package com.moaydogdu.springboottesting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moaydogdu.springboottesting.model.entity.Employee;
import com.moaydogdu.springboottesting.service.EmployeeService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class EmployeeControllerTests {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() {
        // given
        Employee employee = Employee.builder()
                .id(5)
                .firstName("Muhammet")
                .lastName("AYDOGDU")
                .email("moaydogdu@enbsoftware.com")
                .build();

        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));
        // when
        ResultActions response = mockMvc.perform(
                post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee))
        );

        // then
        response.andExpect(status().isCreated())
                .andExpect(jsonPath(
                        "$.firstName",
                        is(employee.getFirstName())))
                .andExpect(jsonPath(
                        "$.lastName",
                        is(employee.getLastName())))
                .andExpect(jsonPath(
                        "$.email",
                        is(employee.getEmail())))
                .andDo(print());
    }

    // JUnit test for Get All employees REST API
    @SneakyThrows
    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeeList() {
        // given
        List<Employee> listOfEmployees = new ArrayList<>();

        listOfEmployees.add(
                Employee.builder()
                        .firstName("Muhammet Oğuzhan")
                        .lastName("AYDOĞDU")
                        .email("moaydogdu@enbsoftware.com")
                        .build()
        );

        listOfEmployees.add(
                Employee.builder()
                        .firstName("Nurettin")
                        .lastName("BAŞTÜRK")
                        .email("nurettinbasturk@enbsoftware.com")
                        .build()
        );

        given(employeeService.getAllEmployees()).willReturn(listOfEmployees);

        // when
        ResultActions response = mockMvc.perform(get("/api/employees"));

        // then
        response.andExpect(status().isOk())
                .andExpect(
                        jsonPath(
                                "$.size()",
                                is(listOfEmployees.size())
                        ))
                .andDo(print());
    }

    // positive scenario - valid employee id
    // JUnit test for GET employee by id from REST API
    @SneakyThrows
    @Test
    public void givenEmployeeId_whenGetEmployeeByID_thenReturnEmployeeObject() {
        // given
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Muhammet Oğuzhan")
                .lastName("AYDOĞDU")
                .email("moaydogdu@enbsoftware.com")
                .build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

        // when
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }


    // positive scenario - valid employee id
    // JUnit test for GET employee by id from REST API
    @SneakyThrows
    @Test
    public void givenUnValidEmployeeId_whenGetEmployeeByID_thenReturnNotFound() {
        // given
        long employeeId = -1L;

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        // when
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        // then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    // JUnit test for update employee REST API -> Positive Scenario
    @SneakyThrows
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() {
        // Given
        final long employeeId = 1L;
        Employee employeeFromDb = Employee.builder()
                .firstName("Muhammet Oğuzhan")
                .lastName("AYDOĞDU")
                .email("moaydogdu@enbsoftware.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .firstName("Muhammet Oğuzhan")
                .lastName("AYDOĞDU")
                .email("m.o.aydogdu@outlook.com")
                .build();

        BDDMockito.given(employeeService.getEmployeeById(employeeId))
                .willReturn(Optional.of(employeeFromDb));

        BDDMockito.given(employeeService.updateEmployee(Mockito.any(Employee.class)))
                .willAnswer(
                        (invocationOnMock) ->
                                invocationOnMock.getArgument(0)
                );

        // When
        ResultActions response = mockMvc.perform(
                put("/api/employees/{id}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmployee))
        );

        // Then
        response
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$.firstName", is(updatedEmployee.getFirstName()))
                )
                .andExpect(
                        jsonPath("$.lastName",is(updatedEmployee.getLastName()))
                )
                .andExpect(
                        jsonPath("$.email",is(updatedEmployee.getEmail()))
                )
                .andDo(print());
    }


    // JUnit test for update employee REST API -> Negative Scenario
    @SneakyThrows
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturn404() {
        // Given
        final long employeeId = 1L;

        Employee updatedEmployee = Employee.builder()
                .firstName("Muhammet Oğuzhan")
                .lastName("AYDOĞDU")
                .email("m.o.aydogdu@outlook.com")
                .build();

        BDDMockito.given(employeeService.getEmployeeById(employeeId))
                .willReturn(Optional.empty());

        // When
        ResultActions response = mockMvc.perform(
                put("/api/employees/{id}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmployee))
        );

        // Then
        response
                .andExpect(
                        status().isNotFound()
                )
                .andDo(print());
    }

    // JUnit test for delete employee REST API -> Positive Scenario
    @SneakyThrows
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() {
        // Given
        final long employeeId = 1L;

        BDDMockito.willDoNothing()
                .given(employeeService)
                .deleteEmployeeById(employeeId);

        // When
        ResultActions response = mockMvc.perform(
                delete("/api/employees/{id}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        response
                .andExpect(
                        status().isOk()
                )
                .andDo(print());
    }

}
