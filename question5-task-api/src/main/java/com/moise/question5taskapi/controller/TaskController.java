package com.moise.question5taskapi.controller;

import com.moise.question5taskapi.model.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private List<Task> taskList = new ArrayList<>();

    public TaskController() {

        taskList.add(new Task(1L, "Complete Assignment", "Finish software project", false, "HIGH", "2026-02-15"));
        taskList.add(new Task(2L, "Grocery Shopping", "Buy milk, eggs, and bread", true, "MEDIUM", "2026-02-10"));
        taskList.add(new Task(3L, "Workout", "1 hour cardio", false, "LOW", "2026-02-12"));
        taskList.add(new Task(4L, "Read Book", "Read 50 pages of Clean Code", false, "MEDIUM", "2026-02-20"));
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskList);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long taskId) {
        return taskList.stream()
                .filter(t -> t.getTaskId().equals(taskId))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status")
    public ResponseEntity<List<Task>> getTasksByStatus(@RequestParam boolean completed) {
        List<Task> results = taskList.stream()
                .filter(t -> t.isCompleted() == completed)
                .collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<Task>> getTasksByPriority(@PathVariable String priority) {
        List<Task> results = taskList.stream()
                .filter(t -> t.getPriority().equalsIgnoreCase(priority))
                .collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task newTask) {
        newTask.setTaskId((long) (taskList.size() + 1));
        taskList.add(newTask);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @RequestBody Task updatedTask) {
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).getTaskId().equals(taskId)) {
                updatedTask.setTaskId(taskId);
                taskList.set(i, updatedTask);
                return ResponseEntity.ok(updatedTask);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{taskId}/complete")
    public ResponseEntity<Task> markTaskCompleted(@PathVariable Long taskId) {
        for (Task t : taskList) {
            if (t.getTaskId().equals(taskId)) {
                t.setCompleted(true);
                return ResponseEntity.ok(t);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        boolean removed = taskList.removeIf(t -> t.getTaskId().equals(taskId));
        if (removed) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
