package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;


@RestController
public class TaskController {
  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping("/tasks")
  public List<Task> getAllTasks() {
    return taskService.getAll();
  }

  @GetMapping("/tasks/{id}")
  public Task getTaskById(@PathVariable int id) {
    return taskService.getById(id);
  }

  @PostMapping("/tasks")
  public Task createTask(@RequestBody Task task) {
    return taskService.create(task.getTitle());
  }

  @DeleteMapping("/tasks/{id}")
  public void deleteTask(@PathVariable int id) {
    taskService.delete(id);
  }

  @PutMapping("/tasks/{id}")
  public Task updateTask(@PathVariable int id, @RequestBody Task task) {
    return taskService.update(id, task.getTitle());
  }

}
