package com.example.todoapp.service;

import com.example.todoapp.model.Todo;
import com.example.todoapp.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service layer for managing to-do items.
 *
 * Provides business logic for creating, retrieving, deleting, updating status,
 * and analyzing to-do tasks.
 */
@Service
@RequiredArgsConstructor
public class TodoService {

    /**
     * Repository for accessing and managing To-do entities in the database.
     */
    private final TodoRepository todoRepository;

    /**
     * Retrieves all to-do items from the database.
     *
     * @return a list of all To-do entries
     */
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    /**
     * Creates a new to-do item with the given name.
     * The item is marked as 'PENDING' and the creation timestamp is set to the current time.
     *
     * @param name the name or title of the new to-do item
     * @return the saved To-do entity
     */
    public Todo createTodo(String name) {
        Todo todo = Todo.builder()
                .name(name)
                .createdAt(LocalDateTime.now())
                .status(Todo.Status.PENDING)
                .build();
        return todoRepository.save(todo);
    }

    /**
     * Deletes a to-do item by its ID.
     * If the ID does not exist, this method will silently do nothing.
     *
     * @param id the ID of the to-do item to delete
     */
    public void deleteTodo(Long id) {
        todoRepository.deleteById(id);
    }

    /**
     * Toggles the status of a to-do item:
     *
     * If it's currently 'COMPLETED', it becomes 'PENDING' and the completion
     * date is cleared.
     * If it's 'PENDING', it becomes 'COMPLETED' and the current time is set
     * as the completion date.
     *
     * @param id the ID of the to-do item whose status should be toggled
     */
    public void toggleStatus(Long id) {
        todoRepository.findById(id).ifPresent(todo -> {
            if (todo.getStatus() == Todo.Status.COMPLETED) {
                todo.setStatus(Todo.Status.PENDING);
                todo.setCompletedAt(null);
            } else {
                todo.setStatus(Todo.Status.COMPLETED);
                todo.setCompletedAt(LocalDateTime.now());
            }
            todoRepository.save(todo);
        });
    }

    /**
     * Counts how many to-do items were created within the specified time range.
     *
     * @param start the start of the date range
     * @param end the end of the date range
     * @return the number of To-do items created in the given range
     */
    public long countCreatedInRange(LocalDateTime start, LocalDateTime end) {
        return todoRepository.findByCreatedAtBetween(start, end).size();
    }

    /**
     * Counts how many to-do items were completed within the specified time range.
     *
     * @param start the start of the date range
     * @param end the end of the date range
     * @return the number of To-do items completed in the given range
     */
    public long countCompletedInRange(LocalDateTime start, LocalDateTime end) {
        return todoRepository.findByCompletedAtBetween(start, end).size();
    }
}