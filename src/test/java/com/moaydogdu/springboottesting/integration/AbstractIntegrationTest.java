package com.moaydogdu.springboottesting.integration;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

public abstract class AbstractIntegrationTest {

    static final MySQLContainer MY_SQL_CONTAINER;

    static {
        MY_SQL_CONTAINER = new MySQLContainer("mysql:8.0.33")
                .withUsername("root")
                .withPassword("rootPassword")
                .withDatabaseName("employee_management");

        MY_SQL_CONTAINER.start();
    }


    @DynamicPropertySource
    public static void dynamicPropertySource(
            DynamicPropertyRegistry dynamicPropertyRegistry
    ){
        dynamicPropertyRegistry.add("spring.datasource.url",
                MY_SQL_CONTAINER::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username",
                MY_SQL_CONTAINER::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password",
                MY_SQL_CONTAINER::getPassword);
    }
}
