public class BankAccount {
  private double balance;

  public BankAccount(double balance) {
    this.balance = balance;
  }

  public double getBalance() {
    return balance;
  }
    
  public void setBalance(double balance) {
    if (balance < 0) {
      System.out.println("残高はマイナスにできません");
    } else {
      this.balance = balance;
    }
  }
}
