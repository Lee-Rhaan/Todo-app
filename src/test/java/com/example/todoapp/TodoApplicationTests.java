package com.example.todoapp;

import com.example.todoapp.controller.TodoController;
import com.example.todoapp.repository.TodoRepository;
import com.example.todoapp.service.TodoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Basic sanity test to verify that the Spring application context loads correctly,
 * and all primary beans are present.
 */
@SpringBootTest
@ActiveProfiles("junit") // Activates the "junit" profile for loading test-specific configuration
@TestPropertySource("classpath:application-junit.properties") // Loads H2 or in-memory DB settings
class TodoApplicationTests {

    /**
     * Injected controller bean for verifying presence in the application context.
     */
    @Autowired
    private TodoController todoController;

    /**
     * Injected service bean for verifying presence in the application context.
     */
    @Autowired
    private TodoService todoService;

    /**
     * Injected repository bean for verifying presence in the application context.
     */
    @Autowired
    private TodoRepository todoRepository;

    /**
     * Verifies that the application context loads without throwing any exceptions.
     * This confirms the basic configuration of Spring Boot is valid.
     */
    @Test
    void contextLoads() {
        // Verifies that the application context loads without exceptions
        // No assertions needed — if this test runs without failure, context is valid
    }

    /**
     * Verifies that key application beans are correctly instantiated and injected.
     * Helps ensure wiring and component scanning are working as expected.
     */
    @Test
    void allBeansAreLoaded() {
        assertThat(todoController).isNotNull();
        assertThat(todoService).isNotNull();
        assertThat(todoRepository).isNotNull();
    }

}
