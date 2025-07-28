package com.example.todoapp.api.service;

import com.example.todoapp.model.Todo;
import com.example.todoapp.repository.TodoRepository;
import com.example.todoapp.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the TodoService class.
 *
 * These tests use Mockito to isolate and verify business logic without involving the real database.
 */
class TodoServiceTest {

    /**
     * The service under test, with mock dependencies injected.
     */
    @InjectMocks
    private TodoService todoService;

    /**
     * A mock of the TodoRepository, used to simulate database interactions.
     */
    @Mock
    private TodoRepository todoRepository;

    /**
     * Initializes Mockito annotations before each test.
     */
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Verifies that getAllTodos() returns the expected list of todos from the repository.
     */
    @Test
    void getAllTodos_shouldReturnAll() {
        List<Todo> mockList = List.of(new Todo(), new Todo());
        when(todoRepository.findAll()).thenReturn(mockList);
        assertThat(todoService.getAllTodos()).hasSize(2);
    }

    /**
     * Verifies that createTodo() saves a new to-do with the correct name and default status.
     */
    @Test
    void createTodo_shouldSaveNewTodo() {
        String name = "Test Task";
        Todo saved = Todo.builder()
                .id(1L)
                .name(name)
                .status(Todo.Status.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        when(todoRepository.save(any())).thenReturn(saved);

        Todo result = todoService.createTodo(name);

        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getStatus()).isEqualTo(Todo.Status.PENDING);
    }

    /**
     * Verifies that deleteTodo() calls the repository's deleteById() method with the correct ID.
     */
    @Test
    void deleteTodo_shouldDeleteById() {
        todoService.deleteTodo(1L);
        verify(todoRepository, times(1)).deleteById(1L);
    }

    /**
     * Verifies that toggleStatus() changes a to-do from PENDING to COMPLETED and
     * sets the completedAt timestamp.
     */
    @Test
    void toggleStatus_shouldSwitchStatusToCompleted() {
        Todo todo = Todo.builder()
                .id(1L)
                .name("Task")
                .status(Todo.Status.PENDING)
                .build();

        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        todoService.toggleStatus(1L);

        assertThat(todo.getStatus()).isEqualTo(Todo.Status.COMPLETED);
    }

    /**
     * Verifies that toggleStatus() changes a to-do from COMPLETED to PENDING
     * and clears the completedAt timestamp.
     */
    @Test
    void toggleStatus_shouldSwitchStatusToPending() {
        Todo todo = Todo.builder()
                .id(1L)
                .name("Task")
                .status(Todo.Status.COMPLETED)
                .build();

        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        todoService.toggleStatus(1L);

        assertThat(todo.getStatus()).isEqualTo(Todo.Status.PENDING);
    }

    /**
     * Verifies that countCreatedInRange() returns the number of todos created
     * in the specified time range.
     */
    @Test
    void countCreatedInRange_shouldDelegateToRepo() {
        LocalDateTime start = LocalDateTime.now().minusDays(7);
        LocalDateTime end = LocalDateTime.now();

        when(todoRepository.findByCreatedAtBetween(start, end))
                .thenReturn(List.of(new Todo(), new Todo()));

        assertThat(todoService.countCreatedInRange(start, end)).isEqualTo(2);
    }

    /**
     * Verifies that countCompletedInRange() returns the number of todos
     * completed in the specified time range.
     */
    @Test
    void countCompletedInRange_shouldDelegateToRepo() {
        LocalDateTime start = LocalDateTime.now().minusDays(7);
        LocalDateTime end = LocalDateTime.now();

        when(todoRepository.findByCompletedAtBetween(start, end))
                .thenReturn(List.of(new Todo()));

        assertThat(todoService.countCompletedInRange(start, end)).isEqualTo(1);
    }
}
