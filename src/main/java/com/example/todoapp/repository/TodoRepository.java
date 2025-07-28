package com.example.todoapp.repository;

import com.example.todoapp.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for managing To-do entity.
 *
 * Extends JpaRepository to provide basic CRUD operations
 * and includes custom query methods for date-range filtering.
 */
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    /**
     * Retrieves all to-do items that were created between the specified start and end timestamps.
     *
     * @param start the start of the creation date range
     * @param end the end of the creation date range
     * @return a list of To-do items created within the given range
     */
    List<Todo> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    /**
     * Retrieves all to-do items that were completed between the specified start and end timestamps.
     *
     * @param start the start of the completion date range
     * @param end the end of the completion date range
     * @return a list of To-do items completed within the given range
     */
    List<Todo> findByCompletedAtBetween(LocalDateTime start, LocalDateTime end);
}