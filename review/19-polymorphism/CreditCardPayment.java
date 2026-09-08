public class CreditCardPayment extends PaymentMethod {
  @Override
  public void pay(int amount) {
    System.out.println("クレジットカードで支払い：" + amount + "円");
  }
  
}
