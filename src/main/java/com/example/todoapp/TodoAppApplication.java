package com.example.todoapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the To-do Application.
 *
 * This class bootstraps the Spring Boot application using SpringApplication.
 * It enables component scanning, autoconfiguration, and Spring Boot support via @SpringBootApplication.
 */
@SpringBootApplication
public class TodoAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(TodoAppApplication.class, args);
    }
}