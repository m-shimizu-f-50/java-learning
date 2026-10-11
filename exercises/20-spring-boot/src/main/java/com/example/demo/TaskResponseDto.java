package com.example.demo;

// レスポンス専用: クライアントに返してよい項目だけ
public class TaskResponseDto {
  private final int id;
  private final String title;
  private final boolean done;

  public TaskResponseDto(Task task) { // Entity → DTOへの変換
    this.id = task.getId();
    this.title = task.getTitle();
    this.done = task.isDone();
  }

  public int getId() {
    return id;
  }
  
  public String getTitle() {
    return title;
  }

  public boolean isDone() {
    return done;
  }
}
