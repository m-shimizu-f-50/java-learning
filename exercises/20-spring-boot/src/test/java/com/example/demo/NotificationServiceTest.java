package com.example.demo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

  @Test
  void notify_returnsFormattedMessageFromFormatter() {
    // ①本物のMessageFormatterの代わりに使う、偽物（モック）を作る
    MessageFormatter fakeFormatter = mock(MessageFormatter.class);
    // ②「fakeFormatter.format("Hello")が呼ばれたら、"[TEST] Hello"を返して」という台本を設定
    when(fakeFormatter.format("Hello")).thenReturn("[TEST] Hello");

    // ③NotificationServiceのコンストラクタにfakeFormatterを渡す（本物のMessageFormatterは一切登場しない）
    NotificationService notificationService = new NotificationService(fakeFormatter);

    // ④notify("Hello")を呼ぶと、内部でfakeFormatter.format("Hello")が呼ばれ、②で設定した文字列が返ってくる
    String result = notificationService.notify("Hello");

    // ⑤戻り値が台本通りかを確認
    assertEquals("[TEST] Hello", result);
    // ⑥fakeFormatter.format("Hello")が実際に呼ばれたかも確認
    verify(fakeFormatter).format("Hello");
  }
}
