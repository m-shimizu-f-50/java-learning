package com.example.demo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// リクエスト専用：クライアントが指定してよい項目だけ
public class TaskRequestDto {
  @NotBlank(message = "titleは必須です")
  @Size(max = 100, message = "titleは100文字以内で入力してください")
  private String title;

  public String getTitle() {
    return title;
  }
}
