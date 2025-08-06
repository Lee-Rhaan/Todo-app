package com.example.todoapp.api.integration;

import com.example.todoapp.model.Todo;
import com.example.todoapp.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for the To-do application.
 *
 * These tests validate real HTTP interactions and database operations within
 * a full Spring Boot context.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("junit")
class TodoIntegrationTest {

    /**
     * Used to perform REST calls against the running application.
     * Injected with a random port for test isolation.
     */
    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * Repository for direct access to the database during test setup and validation.
     */
    @Autowired
    private TodoRepository todoRepository;

    /**
     * Verifies that a to-do item can be added to the database and toggled from PENDING to COMPLETED
     * using the /toggle/{id} endpoint.
     */
    @Test
    void testAddAndToggleTodo() {
        // Create and save a new to-do
        Todo todo = Todo.builder()
                .name("Integration Task")
                .status(Todo.Status.PENDING)
                .build();
        todoRepository.save(todo);

        // Ensure the to-do has been saved
        assertThat(todoRepository.findAll()).isNotEmpty();

        // Simulate toggling the status via HTTP POST
        restTemplate.postForEntity("/toggle/" + todo.getId(), null, String.class);

        // Fetch the updated to-do and verify its status
        Todo updated = todoRepository.findById(todo.getId()).get();
        assertThat(updated.getStatus()).isEqualTo(Todo.Status.COMPLETED);
    }

    /**
     * Verifies that the /analytics endpoint returns a successful HTTP 200 response.
     */
    @Test
    void testAnalyticsEndpoint() {
        ResponseEntity<String> response = restTemplate.getForEntity("/analytics", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    /**
     * Verifies that a new to-do item can be added via the /add endpoint
     * and appears in the database afterwards.
     */
    @Test
    void testAddTodoViaHttp() {
        // Prepare request payload as application/x-www-form-urlencoded
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> request = new HttpEntity<>("name=New Task", headers);

        // Perform POST to /add
        ResponseEntity<String> response = restTemplate.postForEntity("/add", request, String.class);

        // Assert redirect and check the item was added
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND); // 302 redirect
        List<Todo> todos = todoRepository.findAll();
        assertThat(todos).extracting(Todo::getName).contains("New Task");
    }

    /**
     * Verifies that a to-do item can be deleted via the /delete/{id} endpoint.
     */
    @Test
    void testDeleteTodoViaHttp() {
        // Save a to-do to delete
        Todo todo = Todo.builder().name("To Be Deleted").status(Todo.Status.PENDING).build();
        todo = todoRepository.save(todo);

        // Perform POST to /delete/{id}
        ResponseEntity<String> response = restTemplate.postForEntity("/delete/" + todo.getId(), null, String.class);

        // Assert redirect and confirm deletion
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND); // 302 redirect
        assertThat(todoRepository.findById(todo.getId())).isEmpty();
    }

    /**
     * Verifies that the index page ("/") loads successfully and contains the to-do items in the HTML.
     */
    @Test
    void testIndexPageLoadsTodos() {
        // Add a sample item
        Todo todo = Todo.builder().name("Visible Task").status(Todo.Status.PENDING).build();
        todoRepository.save(todo);

        // Perform GET to /
        ResponseEntity<String> response = restTemplate.getForEntity("/", String.class);

        // Assert page loads and contains the task name
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Visible Task");
    }
}
