package com.example.demo;

import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TaskRepository {
  private final List<Task> tasks = new ArrayList<>();

  public List<Task> findAll() {
    return tasks;
  }

  public Task findById(int id) {
    for (Task task : tasks) {
      if (task.getId() == id) {
        return task;
      }
    }
    return null;
  }

  public void save(Task task) {
    tasks.removeIf(t -> t.getId() == task.getId()); // 既存に同じidがあれば先にタスクを削除
    tasks.add(task); // 新しいタスクを追加
  }

  public void deleteById(int id) {
    tasks.removeIf(task -> task.getId() == id);
  }
}
