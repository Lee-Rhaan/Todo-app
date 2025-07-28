package com.example.todoapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Represents a task or item in the to-do list.
 *
 * This entity is mapped to the 'to-do' table in the database.
 * It tracks basic metadata about a task such as name, creation/completion timestamps, and its current status.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Todo {

    /**
     * The unique identifier for the to-do item.
     * It is auto-generated using the database's identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name or title of the to-do item.
     */
    private String name;

    /**
     * The timestamp when the to-do item was created.
     */
    private LocalDateTime createdAt;

    /**
     * The timestamp when the to-do item was marked as completed.
     * Can be null if the item is still pending.
     */
    private LocalDateTime completedAt;

    /**
     * The current status of the to-do item.
     * Stored in the database as a String (e.g., "PENDING", "COMPLETED").
     */
    @Enumerated(EnumType.STRING)
    private Status status;

    /**
     * Enumeration of possible statuses for a to-do item.
     */
    public enum Status {
        PENDING,   // Task has not yet been completed
        COMPLETED   // Task has been completed
    }
}