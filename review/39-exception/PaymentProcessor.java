public class PaymentProcessor  {
  public void process(boolean hasFunds) throws PaymentException {
    if (!hasFunds) {
      throw new PaymentException("残高不足です");
    }
    System.out.println("決済完了");
  }
}
