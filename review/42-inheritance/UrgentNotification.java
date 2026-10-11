public class UrgentNotification extends Notification {
  @Override
  public void send(String message) {
    System.out.println("緊急通知: " + message);

    super.send(message);
  }
}
