package com.example.demo;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaskService {

  private final TaskRepository taskRepository;

  public TaskService(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  // 全件取得するメソッド
  public List<Task> getAll() {
    return taskRepository.findAll();
  }

  // 指定のIDのタスクを取得するメソッド
  public Task getById(int id) {
    return taskRepository.findById(id);
  }

  // タスクを追加するメソッド
  public Task create(String title) {
    int id = taskRepository.findAll().size() + 1; // IDを自動生成する
    Task task = new Task(id, title, false);
    taskRepository.save(task);
    return task;
  }

  // タスクを削除するメソッド
  public void delete(int id) {
    taskRepository.deleteById(id);
  }

  // タスクを更新するメソッド
  public Task update(int id, String title) {
    Task task = getById(id);
    if (task != null) {
      task.setTitle(title);
      taskRepository.save(task);
    }
    return task;
  }
}
