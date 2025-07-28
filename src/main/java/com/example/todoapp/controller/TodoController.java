package com.example.todoapp.controller;

import com.example.todoapp.model.Todo;
import com.example.todoapp.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Web controller for handling HTTP requests related to to-do items.
 *
 * Provides endpoints for displaying the task list, adding, deleting, toggling status,
 * and viewing basic analytics.
 */
@Controller
@RequiredArgsConstructor
public class TodoController {

    /**
     * The service layer responsible for to-do business logic.
     */
    private final TodoService todoService;

    /**
     * Redirects the page to default index page after every action.
     */
    private static final String WEB_PAGE_REDIRECT = "redirect:/";

    /**
     * Displays the main to-do list page.
     *
     * @param model the model to pass data to the Thymeleaf template
     * @return the name of the view to render ("index")
     */
    @GetMapping("/")
    public String index(Model model) {
        List<Todo> todos = todoService.getAllTodos();
        model.addAttribute("todos", todos);
        return "index";
    }

    /**
     * Handles submission of a new to-do item via a POST request.
     *
     * @param name the name of the new to-do item
     * @return a redirect to the main page
     */
    @PostMapping("/add")
    public String addTodo(@RequestParam String name) {
        todoService.createTodo(name);
        return WEB_PAGE_REDIRECT;
    }

    /**
     * Handles deletion of a to-do item by its ID.
     *
     * @param id the ID of the to-do item to delete
     * @return a redirect to the main page
     */
    @PostMapping("/delete/{id}")
    public String deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return WEB_PAGE_REDIRECT;
    }

    /**
     * Handles toggling the completion status of a to-do item.
     * If the task is completed, it is marked as pending, and vice versa.
     *
     * @param id the ID of the to-do item to toggle
     * @return a redirect to the main page
     */
    @PostMapping("/toggle/{id}")
    public String toggleTodo(@PathVariable Long id) {
        todoService.toggleStatus(id);
        return WEB_PAGE_REDIRECT;
    }

    /**
     * Displays analytics for to-do item creation and completion counts within a specified date range.
     * If no range is provided, defaults to the past 7 days.
     *
     * @param start optional start date for analytics (default: 7 days ago)
     * @param end   optional end date for analytics (default: now)
     * @param model the model to pass analytics data to the view
     * @return the name of the analytics view ("analytics")
     */
    @GetMapping("/analytics")
    public String analytics(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            Model model
    ) {
        if (start == null) start = LocalDateTime.now().minusDays(7).truncatedTo(ChronoUnit.DAYS);
        if (end == null) end = LocalDateTime.now();

        model.addAttribute("createdCount", todoService.countCreatedInRange(start, end));
        model.addAttribute("completedCount", todoService.countCompletedInRange(start, end));
        model.addAttribute("start", start);
        model.addAttribute("end", end);

        return "analytics";
    }
}