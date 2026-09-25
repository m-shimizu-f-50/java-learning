public class Main {
  public static void main(String[] args) {
    EmailAlert emailAlert = new EmailAlert();
    emailAlert.sendNotification();

    SmsAlert smsAlert = new SmsAlert();
    smsAlert.sendNotification();
  }
  
}
