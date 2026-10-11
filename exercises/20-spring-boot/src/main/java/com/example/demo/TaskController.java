package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;


@RestController
public class TaskController {
  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping("/tasks")
  public Page<TaskResponseDto> getAllTasks(Pageable pageable) {
    return taskService.getAll(pageable).map(TaskResponseDto::new);
  }

  @GetMapping("/tasks/{id}")
  public TaskResponseDto getTaskById(@PathVariable int id) {
    Task task = taskService.getById(id);
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
    return new TaskResponseDto(task);
  }

}
