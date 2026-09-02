package com.example.demo;


import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Service
public class TaskService {

  private final TaskRepository taskRepository;

  public TaskService(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  // 全件取得するメソッド
  public Page<Task> getAll(Pageable pageable) {
    return taskRepository.findAll(pageable);
  }

  // 指定のIDのタスクを取得するメソッド
  public Task getById(int id) {
    return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
  }

  // タスクを追加するメソッド
  public Task create(String title) {
    Task task = new Task(title, false);
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
    task.setTitle(title);
    taskRepository.save(task);
    
    return task;
  }
}
