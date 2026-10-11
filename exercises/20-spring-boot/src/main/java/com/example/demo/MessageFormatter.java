package com.example.demo;

import org.springframework.stereotype.Component;

@Component
public class MessageFormatter {
  public String format(String message) {
    return "[INFO] " + message;
  }
}
