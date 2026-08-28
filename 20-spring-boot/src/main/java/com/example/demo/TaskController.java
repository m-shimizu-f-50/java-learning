package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;
import java.util.stream.Collectors;


@RestController
public class TaskController {
  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping("/tasks")
  public List<TaskResponseDto> getAllTasks() {
    List<Task> tasks = taskService.getAll();
    return tasks.stream()
      .map(TaskResponseDto::new)
      .collect(Collectors.toList());
  }

  @GetMapping("/tasks/{id}")
  public TaskResponseDto getTaskById(@PathVariable int id) {
    Task task = taskService.getById(id);
    if (task == null) {
      return null;
    }
    return new TaskResponseDto(task);
  }

  @PostMapping("/tasks")
  public TaskResponseDto createTask(@Valid @RequestBody TaskRequestDto request) {
    Task task = taskService.create(request.getTitle());
    return new TaskResponseDto(task);
  }

  @DeleteMapping("/tasks/{id}")
  public void deleteTask(@PathVariable int id) {
    taskService.delete(id);
  }

  @PutMapping("/tasks/{id}")
  public TaskResponseDto updateTask(@PathVariable int id, @Valid @RequestBody TaskRequestDto request) {
    Task task = taskService.update(id, request.getTitle());
    if (task == null) {
      return null;
    }
    return new TaskResponseDto(task);
  }

}
