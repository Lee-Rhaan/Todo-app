package com.example.todoapp.api;

import com.example.todoapp.controller.TodoController;
import com.example.todoapp.service.TodoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for TodoController using WebMvcTest.
 *
 * Focuses on verifying the behavior of HTTP endpoints, views, and model
 * attributes without starting the full application context.
 */
@WebMvcTest(TodoController.class)
class TodoControllerTest {

    /**
     * Mock MVC framework for simulating HTTP requests and verifying responses.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Mocked service layer to isolate the controller's logic during tests.
     */
    @MockBean
    private TodoService todoService;

    /**
     * Tests that the index page ("/") loads successfully and returns the correct view.
     */
    @Test
    void shouldLoadIndexPage() throws Exception {
        when(todoService.getAllTodos()).thenReturn(List.of());
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    /**
     * Tests that submitting a POST request to "/add" successfully redirects
     * and triggers the creation of a new to-do item via the service layer.
     */
    @Test
    void shouldAddTodo() throws Exception {
        mockMvc.perform(post("/add").param("name", "New Task"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
        verify(todoService).createTodo("New Task");
    }

    /**
     * Tests that a to-do item can be deleted via a POST request to "/delete/{id}"
     * and that the user is redirected to the index page afterwards.
     */
    @Test
    void shouldDeleteTodo() throws Exception {
        mockMvc.perform(post("/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
        verify(todoService).deleteTodo(1L);
    }

    /**
     * Tests that a to-do item's status can be toggled via a POST request to "/toggle/{id}"
     * and that a redirect to the index page follows.
     */
    @Test
    void shouldToggleTodo() throws Exception {
        mockMvc.perform(post("/toggle/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
        verify(todoService).toggleStatus(1L);
    }

    /**
     * Tests that the analytics page loads correctly with default date range
     * and that the model contains the expected attributes.
     */
    @Test
    void shouldLoadAnalyticsWithDefaultRange() throws Exception {
        when(todoService.countCreatedInRange(any(), any())).thenReturn(2L);
        when(todoService.countCompletedInRange(any(), any())).thenReturn(1L);

        mockMvc.perform(get("/analytics"))
                .andExpect(status().isOk())
                .andExpect(view().name("analytics"))
                .andExpect(model().attributeExists("createdCount"))
                .andExpect(model().attributeExists("completedCount"));
    }
}
