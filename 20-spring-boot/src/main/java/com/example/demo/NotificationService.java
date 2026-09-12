package com.example.demo;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {
  private final MessageFormatter messageFormatter;

  public NotificationService(MessageFormatter messageFormatter) {
    this.messageFormatter = messageFormatter;
  }

  public String notify(String message) {
    return messageFormatter.format(message);
  }
}
