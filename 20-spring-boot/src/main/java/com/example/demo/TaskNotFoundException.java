package com.example.demo;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(int id) {
        super("Taskが見つかりません: id=" + id);
    }
}
