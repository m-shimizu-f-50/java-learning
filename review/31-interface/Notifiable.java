interface Notifiable {
  String getMessage();

  default void sendNotification() {
    System.out.println("通知:" + getMessage());
  }
}