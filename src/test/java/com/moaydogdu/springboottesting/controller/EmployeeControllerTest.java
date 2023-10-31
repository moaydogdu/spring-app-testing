package com.moaydogdu.springboottesting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moaydogdu.springboottesting.model.entity.Employee;
import com.moaydogdu.springboottesting.service.EmployeeService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
public class EmployeeControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee(){
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
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeeList(){
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



}
