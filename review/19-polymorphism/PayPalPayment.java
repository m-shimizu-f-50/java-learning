public class PayPalPayment extends PaymentMethod {
  @Override
  public void pay(int amount) {
    System.out.println("PayPalで支払い：" + amount + "円");
  }
}
