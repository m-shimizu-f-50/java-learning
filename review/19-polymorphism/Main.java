public class Main {
  public static void main(String[] args) {
    PaymentMethod[] paymentMethods = {
      new CreditCardPayment(),
      new PayPalPayment()
    };

    for (PaymentMethod paymentMethod : paymentMethods) {
      paymentMethod.pay(1000);
    }
  }
  
}
